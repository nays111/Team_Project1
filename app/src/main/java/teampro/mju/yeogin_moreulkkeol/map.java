package teampro.mju.yeogin_moreulkkeol;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link map.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link map#newInstance} factory method to
 * create an instance of this fragment.
 */
public class map extends Fragment implements OnMapReadyCallback {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;

    // 네트워크 사용유무
    boolean isNetworkEnabled = false;

    // GPS 상태값
    boolean isGetLocation = false;
    Context context;
    Location location;
    double lat; // 위도
    double lon; // 경도

    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    private Button btnShowLocation;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    // GPSTracker class
    private GoogleMap googleMap;
    private GpsInfo gps;
    public map() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment map.
     */
    // TODO: Rename and change types and number of parameters
    public static map newInstance(String param1, String param2) {
        map fragment = new map();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        GPSConnetion();
    }

    private MapView mapView = null;
    View v;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        container.removeAllViews();
        try {
        v = inflater.inflate(R.layout.fragment_map, container, false);
        } catch (InflateException e) {

        }
//        mapView = layout.findViewById(R.id.fmap);
//        mapView.onCreate(savedInstanceState);
//
//        mapView.onResume();
//
//        mapView.getMapAsync(this);
        FragmentManager fm = getFragmentManager();
        SupportMapFragment fragment = (SupportMapFragment) fm.findFragmentById(R.id.fmap);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.fmap, fragment).commit();
            fragment.getMapAsync(this);
        }

//        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager()
//                .findFragmentById(R.id.fmap);
//        mapFragment.getMapAsync(this);

//        FragmentManager fragmentManager = getFragmentManager();
//        MapFragment mapFragment = (MapFragment)fragmentManager
//                .findFragmentById(R.id.fmap);
//        mapFragment.getMapAsync(this);
//        SupportMapFragment mapFragment = (SupportMapFragment) getFragmentManager().findFragmentById(R.id.fmap);
//        mapFragment.getMapAsync(this);


        return v;
    }
    public void onDestroyView() {
        super.onDestroyView();
        if( v != null ) {
            ViewGroup parent = (ViewGroup)v.getParent();
            if(parent != null) {
                parent.removeView(v);
            }
        }
    }
    private void replaceFragment(Fragment newFragment) {

        FragmentTransaction trasection =
                getFragmentManager().beginTransaction();
        if(!newFragment.isAdded()){
            try{
                //FragmentTransaction trasection =
                getFragmentManager().beginTransaction();
                trasection.replace(R.id.fmap, newFragment);
                trasection.addToBackStack(null);
                trasection.commit();

            }catch (Exception e) {
                // TODO: handle exception
                //AppConstants.printLog(e.getMessage());

            }
        }else
            trasection.show(newFragment);

    }


    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        if (context instanceof Home.OnFragmentInteractionListener) {
            mListener = (map.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    private int timer = 0;

    private LatLng ex_point;

    private LatLng current_point;

    private boolean isInit = false;

    private boolean mapsSupported = true;


    private boolean isBtnClickStart = false; // Start클릭이먼저 클릭되었는가? 안그러면 hanlder 오류

    Handler handler;

    Handler time_handler;

    private void initializeMap() {

//        if (googleMap == null && mapsSupported) {
//
//            mapView = (MapView) getActivity().findViewById(R.id.map);
//
//            googleMap = mapView();
//
//            GpsInfo gps = new GpsInfo(getActivity().getApplicationContext());
//
//            //gps.getLocation();
//
//            // GPS 사용유무 가져오기
//
//            if (gps.isGetLocation()) {
//
//                double latitude = gps.getLatitude();
//
//                double longitude = gps.getLongitude();
//
//                // Creating a LatLng object for the current location
//
//                //LatLng latLng = new LatLng(37.28,126.97243608534338);
//
//                LatLng latLng = new LatLng(latitude, longitude);
//
//                ex_point = latLng;
//
//                // Showing the current location in Google Map                              googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//
//                // Map 을 zoom 합니다.
//
//                googleMap.animateCamera(CameraUpdateFactory.zoomTo(13));
//
//                // 마커 설정.
//
//                MarkerOptions optFirst = new MarkerOptions();
//
//                optFirst.alpha(0.5f);
//
//                optFirst.anchor(0.5f, 0.5f);
//
//                optFirst.position(latLng);// 위도 • 경도
//
//                optFirst.title("현재 지점");// 제목 미리보기           optFirst.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
//
//                googleMap.addMarker(optFirst).showInfoWindow();
//
//                isInit = true;
//
//            }
//
//        }

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

//액티비티가 처음 생성될 때 실행되는 함수

        super.onActivityCreated(savedInstanceState);
//        if(mapView != null)
//        {
//            mapView.onCreate(savedInstanceState);
//        }
//        initializeMap();

    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && context.checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }
    void GPSConnetion() {

        // 권한 요청을 해야 함
        if (!isPermission) {
            callPermission();
            return;
        }
        Log.d("home GPSConnetion", "isPermission : " + isPermission);


        gps = new GpsInfo(context);
        Log.d("home GPSConnetion", "location" + location);

        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {


            //gps정보를 주소로 변환
            String address = getAddress(context, gps.getLatitude(), gps.getLongitude());
//            et_searchWord.setText(address);

//            Toast.makeText(
//                    getApplicationContext(),
//                    gps.getLatitude()+" / "+gps.getLongitude(),
//                    Toast.LENGTH_LONG).show();
//

//            Toast.makeText(
//                    context,
//                    address,
//                    Toast.LENGTH_LONG).show();


        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
//        mapView.onResume();

//        initializeMap();
        //위치정보 가져오기
        GPSConnetion();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }
    @Override

    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

//        mapView.onSaveInstanceState(outState);

    }
    @Override

    public void onPause() {

        super.onPause();

//        mapView.onPause();

    }
    @Override
    public void onStart() {
        super.onStart();
//        mapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mapView.onStop();
    }
    @Override

    public void onDestroy() {

        super.onDestroy();

//        mapView.onDestroy();

    }

    @Override

    public void onLowMemory() {

        super.onLowMemory();

//        mapView.onLowMemory();

    }
    //gps정보를 주소로 변환
    public static String getAddress(Context mContext, double lat, double lng) {
        String nowAddress = "현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);

        List<Address> address;
        try {
            if (geocoder != null) {

                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);
                Log.d("geocoder", "getFromLocation");
                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0);
                    nowAddress = currentLocationAddress;
                    Log.d("geocoder", nowAddress);
                }
            } else {
                Log.e("geocoder", null);
            }


        } catch (IOException e) {
            Log.e("getAddress()", "주소를 가져 올 수 없습니다.");
            nowAddress = "주소를 가져 올 수 없습니다.";
            e.printStackTrace();
        }
        return nowAddress;
    }
    @Override
    public void onMapReady(GoogleMap map) {

        MapsInitializer.initialize(getContext());

        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        LatLng RESTRAUNT = new LatLng(gps.getLatitude(), gps.getLongitude()); // 서울 위도 (37.56, 126.97)  제대로 나오는데 x, y 좌표로 하면 잘 안나온다. 몇 개 해본 결과 Firebase의 위도 경도 좌표가 잘못돼 있는 거 같다.
//
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(RESTRAUNT);
//        markerOptions.title("서울");
//        markerOptions.snippet("< 여긴 모를껄? > ");
//        map.addMarker(markerOptions);
//
//
//        map.animateCamera(CameraUpdateFactory.zoomTo(15));
        if(gps!=null){
            map.addMarker(new MarkerOptions()
                    .position(new LatLng(gps.getLatitude(), gps.getLongitude()))
                    .title("현재위치").snippet(gps.getLatitude() + " / "+ gps.getLongitude()));
            map.moveCamera(CameraUpdateFactory.newLatLng(RESTRAUNT));
            map.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
//        map.addMarker(new MarkerOptions()
//                .position(new LatLng(0, 0))
//                .title("Marker"));

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
