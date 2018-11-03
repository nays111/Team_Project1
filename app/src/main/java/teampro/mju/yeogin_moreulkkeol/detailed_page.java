package teampro.mju.yeogin_moreulkkeol;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class detailed_page extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_page);

        Button button = (Button)findViewById(R.id.map);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.co.kr/maps/place/%EA%B0%88%ED%92%8D%EC%A7%91/@37.2980556,127.0458433,15z/data=!4m2!3m1!1s0x0:0x882fc63fa4d35b41?ved=2ahUKEwiHgfzdlLjeAhXHerwKHWpSAxcQ_BIwCnoECAYQCA"));
                startActivity(intent);
            }
        });

        String[] items = {"dnr2144\n 맛있다", "Sasd2018\n 음식이 너무 짜다", "kkh88234\n 음식을 먹을수록 고통스럽다"};
        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        ListView listView = (ListView) findViewById(R.id.reviewList);
        listView.setAdapter(adapter);

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
