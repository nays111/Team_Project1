package teampro.mju.yeogin_moreulkkeol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton searchButton = (ImageButton) findViewById(R.id.searchButton);
        ImageButton settingButton = (ImageButton) findViewById(R.id.settingButton);
        ImageButton backToLoginButton = (ImageButton) findViewById(R.id.backToLoginButton);

        /*Search 아이콘 클릭했을시 Main2Activity로 이동*/
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main2Intent = new Intent(Main2Activity.this, SearchActivity.class);
                Main2Activity.this.startActivity(main2Intent);
            }
        });

        /*Setting 아이콘 클릭했을시 SettingActivity로 이동*/
        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(Main2Activity.this, SettingActivity.class);
                Main2Activity.this.startActivity(settingIntent);
            }
        });



        /*뒤로가기 버튼을 클릭하면 로그인화면으로 돌아감.*/
        backToLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,LoginActivity.class);
                Main2Activity.this.startActivity(intent);
            }
        });
    }
}