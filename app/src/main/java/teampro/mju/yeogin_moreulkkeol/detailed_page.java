package teampro.mju.yeogin_moreulkkeol;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.net.URL;

public class detailed_page extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Button btn_writeComment, mapButton;
        TextView Title, Categoty, Date, Address;
        ImageView ImageSrc;
        Intent intent;
        String title = "", category = "", address = "", imageSrc = "", date = "";



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_page);

        Title = (TextView)findViewById(R.id.Title);
        Categoty = (TextView)findViewById(R.id.Category);
        Date = (TextView)findViewById(R.id.Date);
        Address = (TextView)findViewById(R.id.Address);
        ImageSrc = (ImageView) findViewById(R.id.image);

        intent = getIntent();
        title = intent.getStringExtra("title");
        address = intent.getStringExtra("address");
        imageSrc = intent.getStringExtra("Image");
        category = intent.getStringExtra("category");
        date = intent.getStringExtra("date") + " 개업";

        Title.setText(title);
        Categoty.setText(category);
        Address.setText(address);
        Date.setText(date);

        Glide.with(getApplicationContext())
                .load(imageSrc)
                .into(ImageSrc);


        mapButton = (Button)findViewById(R.id.map);
        btn_writeComment = findViewById(R.id.review_button);

        // "리뷰 쓰기" 버튼 눌렀을 때.
        btn_writeComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(detailed_page.this, writeReview.class);
                startActivity(intent);
            }
        });

        // "구글지도로 길찾기" 버튼 눌렀을 때.
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/maps/place/%EA%B0%88%ED%92%8D%EC%A7%91/@37.2980556,127.0458433,15z/data=!4m2!3m1!1s0x0:0x882fc63fa4d35b41?ved=2ahUKEwiHgfzdlLjeAhXHerwKHWpSAxcQ_BIwCnoECAYQCA"));
                startActivity(intent);
            }
        });

        String[] items = {"dnr2144\n 맛있다.", "Sasd2018\n음식이 너무 짜다.", "kkh88234\n음식을 먹을수록 고통스럽다.", "seong21\n서비스가 별로다.", "kasd21\n주인이 서비스 마인드가 없다.", "retw21\n먹자마자 욕밖에 안 나온다.", "cyj7723\n밑반찬 리필이 안 된다.", "cju721\n물을 사먹어야 해서 다시는 안 올 거 같다."};

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        ListView listView = (ListView) findViewById(R.id.reviewList);
        listView.setAdapter(adapter);

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
}
