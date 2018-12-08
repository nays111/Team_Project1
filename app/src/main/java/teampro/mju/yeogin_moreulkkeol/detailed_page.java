package teampro.mju.yeogin_moreulkkeol;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import android.app.FragmentManager;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class detailed_page extends AppCompatActivity implements OnMapReadyCallback {

    Button btn_writeComment;
    TextView Title;
    TextView Categoty;
    TextView Date, Address;
    ImageView IV_title;
    Intent intent;
    String title = "";
    String category = "";
    String address = "";
    String imageSrc = "";
    String date = "";
    double x;
    double y;
    FragmentManager fragmentManager;
    ScrollView sc;
    ListView reviewList;
    FirebaseDatabase fDB;
    DatabaseReference DBrf;
    ArrayAdapter adapter;
    ArrayList<String> list;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_page);

        Title = (TextView) findViewById(R.id.Title);
        Categoty = (TextView) findViewById(R.id.Category);
        Date = (TextView) findViewById(R.id.Date);
        Address = (TextView) findViewById(R.id.Address);
        IV_title = (ImageView) findViewById(R.id.Detail_Image);
        sc = findViewById(R.id.sc_Detail);

        intent = getIntent();
        title = intent.getStringExtra("title");
        address = intent.getStringExtra("address");
        imageSrc = intent.getStringExtra("Image");
        category = intent.getStringExtra("category");
        date = intent.getIntExtra("date", 0) + " 개업";
        x = intent.getDoubleExtra("X", 0.0D);
        y = intent.getDoubleExtra("Y", 0.0D);
        position = intent.getIntExtra("position", 0) + 1;
//        Toast.makeText(getApplicationContext(), "detailed_page.position "
//                + intent.getIntExtra("position", 0), Toast.LENGTH_SHORT).show();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.fitCenter();

            Glide.with(getApplicationContext())
                    .load(imageSrc)
                    .apply(requestOptions)
                    .into(IV_title);
        }


        fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Title.setText(title);
        Categoty.setText(category);
        Address.setText(address);
        Date.setText(date);


        Log.d("Detail_imageSrc", imageSrc);
        btn_writeComment = findViewById(R.id.review_button);

        // "리뷰 쓰기" 버튼 눌렀을 때.
        btn_writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detailed_page.this, writeReview.class);
                intent.putExtra("position", position);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        });

        list = new ArrayList<>();


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        ListView listView = findViewById(R.id.reviewList);
        listView.setAdapter(adapter);

        fDB = FirebaseDatabase.getInstance();
        DBrf = fDB.getReference();
        switch(title){
            case "청춘일기": position =1; break;
            case "장어가두근두근": position =2; break;
            case "짬뽕킹": position =3; break;
            case "양평해장국": position =4; break;
            case "올리앤 용인명지대점": position =5; break;
            case "손가네족발": position =6; break;
        }

        DBrf.child(position+"").child("review").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                adapter.clear();

//                // 리뷰 데이터
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    reviewDTO reviewObj = snapshot.getValue(reviewDTO.class);
                    String coment = reviewObj.getComment();
                    String id = reviewObj.getId();
                    list.add(id + " \n " + coment);
                    Log.d("detailPage ", "id " + id + " / coment " + coment);
                }
//
//                for (int i = 0; i < 30; i++) {
//                    DataSnapshot ds = dataSnapshot.child(i + "").child("review");
//                    if (ds != null) {
//                        Toast.makeText(getApplicationContext(),"title : "+title+" ds.getValue() : "+ds.child("name").getValue(),Toast.LENGTH_LONG).show();
//                        if (ds.child("name").getValue().equals(title)) {
//                            for (int l = 0; l < 30; l++) {
//
//                                String coment = ds.child("comment").getValue(String.class);
//                                String id = ds.child("id").getValue(String.class);
//
//                                if (coment != null && id != null && coment != "" && id != "")
//                                    list.add(id + " \n " + coment);
//
//                                Log.d("detailPage ", "id " + id + " / coment " + coment);
//
//                            }
//
//                        }
//                    }
//
//                }


                // recyclevuew 갱신
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // 리스트뷰 아이템 중 하나 클릭했을 때.
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = String.valueOf(parent.getItemAtPosition(position));
                        Toast.makeText(detailed_page.this, item, Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    public void onMapReady(final GoogleMap map) {

        LatLng RESTRAUNT = new LatLng(x, y); // 서울 위도 (37.56, 126.97)  제대로 나오는데 x, y 좌표로 하면 잘 안나온다. 몇 개 해본 결과 Firebase의 위도 경도 좌표가 잘못돼 있는 거 같다.

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(RESTRAUNT);
        markerOptions.title(title);
        markerOptions.snippet("< 여긴 모를껄? > ");
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(RESTRAUNT));
        map.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
}
