package edu.skku.HyeonYeong_Jeong.SWPPA2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    // View
    Button log_in_Button;
    EditText id_editText;
    EditText password_editText;


    // variables
    String input_id;
    String input_password;


    // Firebase
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // View
        log_in_Button = findViewById(R.id.login_button);
        id_editText = findViewById(R.id.id_editText);
        password_editText = findViewById(R.id.password_editText);

        mDatabase = FirebaseDatabase.getInstance().getReference(); // root node 를 넣어줌



        // log_in_Button 이벤트
        log_in_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 아이디와 비밀번호 받아온다
                input_id = id_editText.getText().toString();
                input_password = password_editText.getText().toString();


                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (check_userInfo(dataSnapshot) == true) {

                            // 자동 로그인
                            SaveSharedPreference.setUserName(LoginActivity.this, input_id);



                            Intent intent = new Intent(LoginActivity.this, ChatListActivity.class);
                            intent.putExtra("id", input_id); // 다음 intent 로 정보 넘기기
                            Toast.makeText(LoginActivity.this, "로그인 성공 ! \nID : " + input_id, Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        } else {
                            Toast.makeText(LoginActivity.this, "비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }

    private boolean check_userInfo(DataSnapshot dataSnapshot) {

        try {
            for (DataSnapshot ds : dataSnapshot.getChildren()) {

                User user = new User();

                if (ds.child(input_id).getValue(User.class).getUser_id() == null) {
                    Toast.makeText(this, "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                    return false;
                }

                user.setUser_password(ds.child(input_id).getValue(User.class).getUser_password());

                if (input_password.equals(user.user_password)) {

                    // Toast.makeText(this, "True !!", Toast.LENGTH_SHORT).show();
                    return true;
                } else {
                    return false;
                }

            }
        } catch (Exception e) {

            Toast.makeText(this, "아이디와 비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return false;

    }


}
