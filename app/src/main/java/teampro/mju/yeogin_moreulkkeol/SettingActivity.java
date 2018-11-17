package teampro.mju.yeogin_moreulkkeol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button editProfileButton = (Button) findViewById(R.id.editProfileButton);
        Button registerFavoriteButton = (Button) findViewById(R.id.registerFavoriteButton);
        Button myFavoriteButton = (Button) findViewById(R.id.myFavoriteButton);
        Button logoutButton = (Button) findViewById(R.id.logoutButton);

        ImageButton backToMainButton = (ImageButton) findViewById(R.id.backToMainButton);


        /*뒤로가기 버튼을 클릭하면 메인화면으로 돌아감.*/
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,MainActivity.class);
                SettingActivity.this.startActivity(intent);
            }
        });

        //로그아웃 버튼을 클릭하면 로그인화면으로 돌아감*/
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
                SettingActivity.this.startActivity(intent);
            }
        });
    }
}
