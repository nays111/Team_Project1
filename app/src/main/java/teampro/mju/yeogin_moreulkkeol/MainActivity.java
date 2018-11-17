package teampro.mju.yeogin_moreulkkeol;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

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
    Item[] item;
    final int ITEM_SIZE = 28;

    RecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        //검색부분
        et_searchWord = findViewById(R.id.searchWord) ;


        //음식점 리스트 부분
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRootRef = firebaseDatabase.getReference();


        items = new ArrayList<>();
        item = new Item[ITEM_SIZE];

        adapter = new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_main);
        recyclerView.setAdapter(adapter);


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this);


        // FireBase에서 데이터 가져오기
        getDateToFirebaseDB();

    }

    @Override
    protected void onStart() {
        super.onStart();
        //위치정보 가져오기
        GPSConnetion();


    }

    void getDateToFirebaseDB(){

        //        mRootRef.push().setValue("shopList");
        //mRootRef.child("users").child("1").child("2").setValue("v");


        //FirebaseDB에서 데이터 가져오기
//        ChildRef = mRootRef.child("0");
        mRootRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String address="" ,name="" ,image="", menu="";
                double date = 0, x = 0, y = 0;
//                Map<String, Object> map = dataSnapshot.getValue();
//                key = dataSnapshot.getKey();


                adapter.items.clear();

                // 가져온 데이터를 리스트에 음식점리스트에 추가한다.
                for (int i = 1; i < ITEM_SIZE; i++) {
                    DataSnapshot ds = dataSnapshot.child(i+"");
                    address = ds.child("address").getValue(String.class);
                    image = ds.child("image").getValue(String.class);
                    menu = ds.child("menu").getValue(String.class);
                    name = ds.child("name").getValue(String.class);
                    date = ds.child("open_date").getValue(Double.class);
                    x = ds.child("x").getValue(Double.class);
                    y = ds.child("y").getValue(Double.class);

//                    item[i] = new Item("https://www.google.co.kr/maps/uv?hl=ko&pb=!1s0x357b4935cb351885:0x5793b3bf178f8603!2m22!2m2!1i80!2i80!3m1!2i20!16m16!1b1!2m2!1m1!1e1!2m2!1m1!1e3!2m2!1m1!1e5!2m2!1m1!1e4!2m2!1m1!1e6!3m1!7e115!4shttps://lh5.googleusercontent.com/p/AF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v%3Dw325-h218-n-k-no!5z66eb7KeRIC0gR29vZ2xlIOqygOyDiQ&imagekey=!1e10!2sAF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v",
//                            "안골식당","2018-11-03","한식","경기도 용인시 기흥구 고매동 227-1번지 ",true);
                    item[i] = new Item(image, name, String.valueOf(date), menu, address,false);
                    items.add(item[i]);
                }

                // recyclerView 갱신
                adapter.notifyDataSetChanged();

//                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    a = ds.getValue(String.class);
//
//                    if(ds.getKey()=="address"){
//                        address = ds.getValue(String.class);
//                    }
//                    address = ds.child("address").getValue(String.class);
//                    name = ds.child("name").getValue(String.class);
//                    a = ds.child("0").getValue(String.class);
//                    Log.d("TAG", address + " / " + name+ " / "+ a);
//
//                }
                Log.d("TAG", address + " / " + name);
                Toast.makeText(getApplicationContext(), address + " / " + name, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    //검색어를 받아 음식점 정보를 카드뷰에 뿌려준다
    public void  onClickSearch(View v){

        String query = et_searchWord.getText().toString();

        Toast.makeText(this,query+"",Toast.LENGTH_LONG).show();
        et_searchWord.setText(null);


    }


    @Override
    protected void onResume() {
        super.onResume();


        //위치정보 가져오기
        GPSConnetion();

        //FireBase에서 데이터 가져오기
        getDateToFirebaseDB();
    }

    void GPSConnetion(){

        // 권한 요청을 해야 함
        if (!isPermission) {
            callPermission();
            return;
        }
        Log.d("Main onResume","isPermission : "+isPermission);


        gps = new GpsInfo(this);
        Log.d("main onResume","location"+location);

        // GPS 사용유무 가져오기
        if (gps.isGetLocation()) {



            //gps정보를 주소로 변환
            String address = getAddress(getApplicationContext(),gps.getLatitude(),gps.getLongitude());
            et_searchWord.setText(address);

//            Toast.makeText(
//                    getApplicationContext(),
//                    gps.getLatitude()+" / "+gps.getLongitude(),
//                    Toast.LENGTH_LONG).show();
//

//            Toast.makeText(
//                    getApplicationContext(),
//                    address,
//                    Toast.LENGTH_LONG).show();



        } else {
            // GPS 를 사용할수 없으므로
            gps.showSettingsAlert();
        }

    }

    //gps 정보를 주소로 변환
    public static String getAddress(Context mContext,double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);

        List <Address> address;
        try {
            if (geocoder != null) {

                // 세 번째 파라미터는 좌표에 대해 주소를 리턴 받는 개수로
                // 한 좌표에 대해 두 개 이상의 이름이 존재할 수 있으므로 주소 배열을 리턴받기위해 최대 개수 설정
                address = geocoder.getFromLocation(lat, lng, 1);
                Log.d("geocoder","getFromLocation");
                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0);
                    nowAddress  = currentLocationAddress;
                    Log.d("geocoder",nowAddress);
                }
            }else{
                Log.e("geocoder",null);
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
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);


        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }


    //ToolBar에 menu.xml을 인플레이트
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //return super.onCreateOptionsMenu(menu);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // 설정 버튼 이벤트 처리
        //return super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                Toast.makeText(getApplicationContext(), "환경설정 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                Toast.makeText(getApplicationContext(), "나머지 버튼 클릭됨", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);

        }
    }
}


