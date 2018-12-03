package teampro.mju.yeogin_moreulkkeol;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText idText;
    EditText passwordText;
    String pwd;
    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        idText = (EditText) findViewById(R.id.et_registerId);
        passwordText = (EditText) findViewById(R.id.et_registerPassword);

        Button signupButton = (Button) findViewById(R.id.signupButton);

        firebaseAuth = firebaseAuth.getInstance();


//        /*뒤로가기 버튼을 클릭하면 로그인화면으로 돌아감.*/
//        backToLoginButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
//                RegisterActivity.this.startActivity(intent);
//            }
//        });

        //회원가입완료 버튼을 클릭하면 로그인화면으로 돌아감*/
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (idText != null && passwordText != null) {
                    if (idText.getText().toString() != "" && passwordText.getText().toString() != "") {

                         email = idText.getText().toString().trim();
                         pwd = passwordText.getText().toString().trim();

                        firebaseAuth.createUserWithEmailAndPassword(email, pwd)
                                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if (task.isSuccessful()) {
                                            Intent intent = new Intent(RegisterActivity.this,
                                                    LoginActivity.class);
                                            RegisterActivity.this.startActivity(intent);
                                            Toast.makeText(RegisterActivity.this, "회원가입 완료", Toast.LENGTH_SHORT).show();

                                            // 로그인정보를 로컬에 저장
                                            SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("email", email);
                                            editor.putString("pw", pwd);
                                            editor.commit();

                                            Log.d(this.getClass().getName(),"email : "+email+" / pw : "+pwd);
                                            finish();
                                        } else {
                                            Toast.makeText(RegisterActivity.this, "등록 에러", Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                    }
                                });

                    }

                }
            }
        });

    }
}
