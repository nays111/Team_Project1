package teampro.mju.yeogin_moreulkkeol;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    Context context;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRootRef;
    DatabaseReference ChildRef;
    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;

    // 네트워크 사용유무
    boolean isNetworkEnabled = false;

    // GPS 상태값
    boolean isGetLocation = false;

    Location location;
    double lat; // 위도
    double lon; // 경도

    // 최소 GPS 정보 업데이트 거리 10미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    // 최소 GPS 정보 업데이트 시간 밀리세컨이므로 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    protected LocationManager locationManager;

    private Button btnShowLocation;
    private TextView txtLat;
    private TextView txtLon;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private boolean isPermission = false;

    // GPSTracker class
    private GpsInfo gps;
    EditText et_searchWord;
    Toolbar toolbar;

    List<Item> items;
    Item[] restaurantItem;
    final int ITEM_SIZE = 20;

    RecyclerAdapter adapter;

    public Home() {
        // Required empty public constructor

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        items = new ArrayList<>();
        adapter = new RecyclerAdapter(context, items, R.layout.item_cardview);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        LayoutInflater layoutInflater = (LayoutInflater)(context).getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = layoutInflater.inflate(R.layout.fragment_home, null);
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);


        //검색부분
        et_searchWord = rootView.findViewById(R.id.searchWord);
        et_searchWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //검색어를 받아 음식점 정보를 카드뷰에 뿌려준다

                String query = et_searchWord.getText().toString();

                Toast.makeText(context, query + "", Toast.LENGTH_LONG).show();
                et_searchWord.setText(null);


            }
        });

        //음식점 리스트 부분
        RecyclerView recyclerView = rootView.findViewById(R.id.recycleview1);
        recyclerView.setHasFixedSize(true);
//        GridLayoutManager mLayoutManager = new GridLayoutManager(context,2);
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.addItemDecoration(new CustomItemDecorator(20));

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRootRef = firebaseDatabase.getReference();


        restaurantItem = new Item[ITEM_SIZE];

        recyclerView.setAdapter(adapter);
//        recyclerView.addItemDecoration(new RecyclerViewDecoration(25));

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this);


        //FireBase에서 데이터 가져오기
        getDateToFirebaseDB();
        //위치정보 가져오기
        GPSConnetion();

        return rootView;
    }

    void getDateToFirebaseDB() {

        //        mRootRef.push().setValue("shopList");
        //mRootRef.child("users").child("1").child("2").setValue("v");


        //FirebaseDB에서 데이터 가져오기
//        ChildRef = mRootRef.child("0");
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String address = "", name = "", image = "", menu = "";
                double x = 0, y = 0;
                int date=0;
//                Map<String, Object> map = dataSnapshot.getValue();
//                key = dataSnapshot.getKey();


                adapter.items.clear();

                // 가져온 데이터를 리스트에 음식점리스트에 추가한다.
                for (int i = 1; i < ITEM_SIZE; i++) {
                    DataSnapshot ds = dataSnapshot.child(i + "");
                    address = ds.child("address").getValue(String.class);
                    image = ds.child("image").getValue(String.class);
                    menu = ds.child("menu").getValue(String.class);
                    name = ds.child("name").getValue(String.class);
                    date = ds.child("open_date").getValue(Integer.class);
                    x = ds.child("x").getValue(Double.class);
                    y = ds.child("y").getValue(Double.class);

                    String re = ds.child("review1").child("comment").getValue(String.class);
//                    item[i] = new Item("https://www.google.co.kr/maps/uv?hl=ko&pb=!1s0x357b4935cb351885:0x5793b3bf178f8603!2m22!2m2!1i80!2i80!3m1!2i20!16m16!1b1!2m2!1m1!1e1!2m2!1m1!1e3!2m2!1m1!1e5!2m2!1m1!1e4!2m2!1m1!1e6!3m1!7e115!4shttps://lh5.googleusercontent.com/p/AF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v%3Dw325-h218-n-k-no!5z66eb7KeRIC0gR29vZ2xlIOqygOyDiQ&imagekey=!1e10!2sAF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v",
//                            "안골식당","2018-11-03","한식","경기도 용인시 기흥구 고매동 227-1번지 ",true);
                    restaurantItem[i] = new Item(image, name, date, menu, address, false, x, y);
                    restaurantItem[i].setLat(x);
                    restaurantItem[i].setLon(y);
                    items.add(restaurantItem[i]);

                    Log.d("DB-data", name + " / " + re);
                }

                // recyclevuew 갱신
                adapter.notifyDataSetChanged();

//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
////                    a = ds.getValue(String.class);
//
//                    if(ds.getKey()=="address"){
//                        address = ds.getValue(String.class);
//                    }
////                    address = ds.child("address").getValue(String.class);
////                    name = ds.child("name").getValue(String.class);
////                    a = ds.child("0").getValue(String.class);
//                    Log.d("TAG", address + " / " + name+ " / "+ a);
//
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();

        //위치정보 가져오기
        GPSConnetion();

        //FireBase에서 데이터 가져오기
        getDateToFirebaseDB();

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
            et_searchWord.setText(address);

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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
