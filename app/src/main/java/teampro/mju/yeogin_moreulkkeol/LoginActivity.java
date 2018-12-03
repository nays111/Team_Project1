package teampro.mju.yeogin_moreulkkeol;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.SharedMemory;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    EditText idText;
    EditText passwordText;
    String email;
    String pwd;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        idText = (EditText) findViewById(R.id.et_LoginId);
        passwordText = (EditText) findViewById(R.id.et_LoginPassword);
        Button loginButton = (Button) findViewById(R.id.loginButton);
        TextView registerButton = (TextView) findViewById(R.id.registerButton);

        firebaseAuth = firebaseAuth.getInstance();

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(intent);
            }
        });


//        Toast.makeText(LoginActivity.this,
//                "email : "+idText.getText().toString(),
//                Toast.LENGTH_SHORT).show();


        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (idText != null && passwordText != null) {

                    email = idText.getText().toString().trim();
                    pwd = passwordText.getText().toString().trim();

                    Log.d(this.getClass().getName() + " email : ",
                            email);
                    Log.d(this.getClass().getName() + " pwd : ",
                            pwd);


                    if (email.getBytes().length > 0 &&
                            pwd.getBytes().length > 0) {

                        firebaseAuth.signInWithEmailAndPassword(email, pwd)
                                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            // 로그인정보를 로컬에 저장
                                            SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = pref.edit();
                                            editor.putString("email", email);
                                            editor.putString("pw", pwd);
                                            editor.commit();

                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                            // 로그인페이지를 스택에서 지운다
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(LoginActivity.this, "아이디 또는 비밀번호를 잘못 입력하셨습니다", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                    } else {
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();

                    }
                }


            }
        });
    }

    @Override
    protected void onResume() {

        super.onResume();
        // 아이디 패스워드 불러오기
        SharedPreferences pref = getSharedPreferences("user", MODE_PRIVATE);
        String e = pref.getString("email", "");
        String p = pref.getString("pw", "");
        idText.setText(e);
        passwordText.setText(p);

        Log.d(this.getClass().getName(), "[ onResume ] email : " + e + "/ pw : " + p);


    }
}