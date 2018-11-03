package teampro.mju.yeogin_moreulkkeol;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
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

<<<<<<< HEAD
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

=======
import java.io.IOException;
>>>>>>> origin/master
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference mRef;

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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final int ITEM_SIZE = 5;


        //검색부분
        et_searchWord = findViewById(R.id.searchWord) ;


        //음식점 리스트 부분
        RecyclerView recyclerView = findViewById(R.id.recyclerview);

        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLayoutManager);

        firebaseDatabase = FirebaseDatabase.getInstance();
        mRef = firebaseDatabase.getReference("이름");



        List<Item> items = new ArrayList<>();
        Item[] item = new Item[ITEM_SIZE];
        item[0] = new Item("https://www.google.co.kr/maps/uv?hl=ko&pb=!1s0x357b4935cb351885:0x5793b3bf178f8603!2m22!2m2!1i80!2i80!3m1!2i20!16m16!1b1!2m2!1m1!1e1!2m2!1m1!1e3!2m2!1m1!1e5!2m2!1m1!1e4!2m2!1m1!1e6!3m1!7e115!4shttps://lh5.googleusercontent.com/p/AF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v%3Dw325-h218-n-k-no!5z66eb7KeRIC0gR29vZ2xlIOqygOyDiQ&imagekey=!1e10!2sAF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v",
         "안골식당","2018-11-03","한식","경기도 용인시 기흥구 고매동 227-1번지 ",true);
        item[1] = new Item("https://www.google.co.kr/maps/uv?hl=ko&pb=!1s0x357b4935cb351885:0x5793b3bf178f8603!2m22!2m2!1i80!2i80!3m1!2i20!16m16!1b1!2m2!1m1!1e1!2m2!1m1!1e3!2m2!1m1!1e5!2m2!1m1!1e4!2m2!1m1!1e6!3m1!7e115!4shttps://lh5.googleusercontent.com/p/AF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v%3Dw325-h218-n-k-no!5z66eb7KeRIC0gR29vZ2xlIOqygOyDiQ&imagekey=!1e10!2sAF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v",
                "안골식당","2018-11-03","한식","경기도 용인시 기흥구 고매동 227-1번지 ",true);
        item[2] = new Item("https://www.google.co.kr/maps/uv?hl=ko&pb=!1s0x357b4935cb351885:0x5793b3bf178f8603!2m22!2m2!1i80!2i80!3m1!2i20!16m16!1b1!2m2!1m1!1e1!2m2!1m1!1e3!2m2!1m1!1e5!2m2!1m1!1e4!2m2!1m1!1e6!3m1!7e115!4shttps://lh5.googleusercontent.com/p/AF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v%3Dw325-h218-n-k-no!5z66eb7KeRIC0gR29vZ2xlIOqygOyDiQ&imagekey=!1e10!2sAF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v",
                "안골식당","2018-11-03","한식","경기도 용인시 기흥구 고매동 227-1번지 ",true);
        item[3] = new Item("https://www.google.co.kr/maps/uv?hl=ko&pb=!1s0x357b4935cb351885:0x5793b3bf178f8603!2m22!2m2!1i80!2i80!3m1!2i20!16m16!1b1!2m2!1m1!1e1!2m2!1m1!1e3!2m2!1m1!1e5!2m2!1m1!1e4!2m2!1m1!1e6!3m1!7e115!4shttps://lh5.googleusercontent.com/p/AF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v%3Dw325-h218-n-k-no!5z66eb7KeRIC0gR29vZ2xlIOqygOyDiQ&imagekey=!1e10!2sAF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v",
                "안골식당","2018-11-03","한식","경기도 용인시 기흥구 고매동 227-1번지 ",true);
        item[4] = new Item("https://www.google.co.kr/maps/uv?hl=ko&pb=!1s0x357b4935cb351885:0x5793b3bf178f8603!2m22!2m2!1i80!2i80!3m1!2i20!16m16!1b1!2m2!1m1!1e1!2m2!1m1!1e3!2m2!1m1!1e5!2m2!1m1!1e4!2m2!1m1!1e6!3m1!7e115!4shttps://lh5.googleusercontent.com/p/AF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v%3Dw325-h218-n-k-no!5z66eb7KeRIC0gR29vZ2xlIOqygOyDiQ&imagekey=!1e10!2sAF1QipMrAAWz2YV4zEWe1ckrrkQEKQy59GK2N2dKx8_v",
                "안골식당","2018-11-03","한식","경기도 용인시 기흥구 고매동 227-1번지 ",true);
        for (int i = 0; i < ITEM_SIZE; i++) {
            items.add(item[i]);
        }

        recyclerView.setAdapter(new RecyclerAdapter(getApplicationContext(), items, R.layout.activity_main));


        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,this);


        //위치정보 가져오기
        GPSConnetion();
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

    //gps정보를 주소로 변환
    public static String getAddress(Context mContext,double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);

        List <Address> address;
        try {
            if (geocoder != null) {

                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
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


