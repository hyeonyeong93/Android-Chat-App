package edu.skku.HyeonYeong_Jeong.SWPPA2;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ChatroomActivity extends AppCompatActivity {

    ListView listView;
    Button send_button;
    EditText editText;

    ArrayList<String> str_arrayList;

    private DatabaseReference rootNode_DB;

    String roomName;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);

        rootNode_DB = FirebaseDatabase.getInstance().getReference(); // root node 를 넣어줌

        // View

        listView = findViewById(R.id.chatRoom_listView);
        send_button = findViewById(R.id.send_button);
        editText = findViewById(R.id.input_msg_EditText);

        str_arrayList = new ArrayList<>();

        // ArrayAdapter 객체 <- ArrayList 객체를 연결
        arrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1
                , str_arrayList);

        updateMsg_to_ArrList(rootNode_DB); // update Data from DB to ArrayList

        // listView 에 ArrayList 값을 보여준다



        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                arrayAdapter.notifyDataSetChanged();
                listView.setAdapter(arrayAdapter);

            }
        }, 500);// 0.5초 정도 딜레이를 준 후 시작



        // 이전 Activity 에서 data 받아오기
        Intent intent = getIntent();
        roomName = intent.getStringExtra("roomName") + "_room";













        // send_button 버튼 이벤트 처리

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                str_arrayList.clear();


                String input_str = editText.getText().toString();

                if (input_str.replace(" ", "").equals(""))
                    return;

                if(check_hash(input_str)){

                    String search_str = "";

                    int size = input_str.length();

                    for (int i=1; i<size; i++){

                        char cur_char = input_str.charAt(i);
                        search_str += cur_char;

                    }



                    Toast.makeText(ChatroomActivity.this, "네이버에 " + search_str + " 검색", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://search.naver.com/search.naver?sm=top_hty&fbm=1&ie=utf8&query="
                    + search_str));

                    startActivity(intent);


                    return;
                }



                rootNode_DB.child("room_list").child(roomName).child("messages").push().setValue(input_str);

                updateMsg_to_ArrList(rootNode_DB); // update Data from DB to ArrayList

                // flush
                editText.setText("");
                str_arrayList.clear();


            }
        });







    }

    // check if hash? ( use NAVER API )
    private boolean check_hash(String input_str) {

        char first_char = input_str.charAt(0);

        if(first_char == '#')
            return true;
        else
            return false;


    }

    private void updateMsg_to_ArrList(DatabaseReference rootNode_db) {



        rootNode_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                str_arrayList.clear();
                
                for (DataSnapshot ds : dataSnapshot.child("room_list").child(roomName).child("messages").getChildren()){

                    try {
                        // DB 에서 받아온 메시지들
                        String input_msg = ds.getValue().toString();

                        str_arrayList.add(input_msg); // ArrayList 에 추가
                    }
                    catch (Exception e){
                        Toast.makeText(ChatroomActivity.this, "onDataChange 에서 오류 발생", Toast.LENGTH_SHORT).show();
                    }
                   
                   
                }

                arrayAdapter.notifyDataSetChanged();
                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
