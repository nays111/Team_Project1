package teampro.mju.yeogin_moreulkkeol;
import android.content.Context;
import android.location.LocationManager;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements Home.OnFragmentInteractionListener,map.OnFragmentInteractionListener{
    Toolbar toolbar;
    // FrameLayout에 각 메뉴의 Fragment를 바꿔 줌
    private FragmentManager fragmentManager = getSupportFragmentManager();
    // 4개의 메뉴에 들어갈 Fragment들
    Fragment home_fragment =new Home();
    map m =new map();
    private Home.OnFragmentInteractionListener mListener;

    Context context;
    DatabaseReference mRootRef;

    EditText et_searchWord;

    ArrayList<Item> items;
    Item[] restaurantItem;
    final int ITEM_SIZE = 20;
    ImageButton btn_search;
    RecyclerAdapter adapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
//
//            transaction.add(R.id.frame_layout, home_fragment);
//            transaction.commit();

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    transaction.replace(R.id.frame_layout,home_fragment )
                            .commitAllowingStateLoss();
                     return true;
                case R.id.navigation_dashboard:

                    m.items = items;
                    transaction.replace(R.id.frame_layout, m)
                            .commitAllowingStateLoss();
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigation);
//        // 첫 화면 지정
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, home_fragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        mRootRef = firebaseDatabase.getReference();

        items = new ArrayList<>();
        restaurantItem = new Item[ITEM_SIZE];

        getDateToFirebaseDB();
    }



    @Override
    protected void onResume() {
        super.onResume();
        getDateToFirebaseDB();

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

                items.clear();
//                adapter.items.clear();

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
                    restaurantItem[i] = new Item(image, name, date, menu, address, false, x, y);
                    restaurantItem[i].setLat(x);
                    restaurantItem[i].setLon(y);
                    items.add(restaurantItem[i]);
                    Log.d("DB-data", name + " / " + re);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}


