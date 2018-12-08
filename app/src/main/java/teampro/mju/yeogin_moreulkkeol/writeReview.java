package teampro.mju.yeogin_moreulkkeol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.UUID;

public class writeReview extends AppCompatActivity {

    EditText et_review;
    Button btn_close;
    Button btn_ok;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase fDB;
    DatabaseReference DBrf;
    int position;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);
        setTitle("리뷰작성");

        Intent intent = getIntent();
        position= intent.getIntExtra("position",0);
        title = intent.getStringExtra("title");

        et_review = findViewById(R.id.et_review);
        btn_close = findViewById(R.id.btn_close);
        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String reviewStr = et_review.getText().toString();
                if(reviewStr!=null&&reviewStr!=""){
                    writeRivew(reviewStr);
                    finish();
                }
            }
        });
        firebaseAuth = firebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance();
        DBrf = fDB.getReference();
        String UID = firebaseAuth.getUid();
        Email = firebaseAuth.getCurrentUser().getEmail();
        String email[] = Email.split("@", 0);
        id = email[0];
    }
    String id;
    String Email;
    private void writeRivew(String review) {
        String uuid = UUID.randomUUID().toString();
        Date data = new Date();

        DatabaseReference rf = DBrf.child(position+"").child("review").child(data.getTime()+uuid);
        rf.child("comment").setValue(review);
        rf.child("id").setValue(id);

    }
}
