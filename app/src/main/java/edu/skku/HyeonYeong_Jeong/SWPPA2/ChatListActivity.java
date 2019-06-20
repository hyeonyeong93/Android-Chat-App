package edu.skku.HyeonYeong_Jeong.SWPPA2;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ChatListActivity extends AppCompatActivity {

    ListView chatroom_listview;
    Button chatroom_creating_button;
    EditText chatroom_name_EditText;
    private String chatroom_name;
    private DatabaseReference rootNode_DB;


    ArrayList<String> chatRoom_ArrList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);





        // View

        chatroom_listview = findViewById(R.id.chat_listview);
        chatroom_creating_button = findViewById(R.id.button_createChatroom);
        chatroom_name_EditText = findViewById(R.id.chatroom_editText);

        rootNode_DB = FirebaseDatabase.getInstance().getReference(); // root node 를 넣어줌

        chatRoom_ArrList = new ArrayList<>();


        // ArrayAdapter 객체를 사용 -> ArrayAdapter 객체와 ArrayList 객체를 연결
        final ArrayAdapter<String> arr_adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1
                , chatRoom_ArrList);

        // update chatRoomList to ArrayList from Firebase
        updateChatRoomList_to_ArrList(rootNode_DB);

        // Show ArrayList to ListView
        try {
            chatroom_listview.setAdapter(arr_adapter);
        }catch (Exception e){
            Toast.makeText(this, "Array Adapter 에서 오류 발생", Toast.LENGTH_SHORT).show();
        }

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                arr_adapter.notifyDataSetChanged(); // adapter 에 반영하기
            }
        }, 500);// 0.5초 정도 딜레이를 준 후 시작







        // 버튼 onClickListener
        chatroom_creating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // DB 에 data 추가해주기
                chatroom_name = chatroom_name_EditText.getText().toString();
                rootNode_DB.child("chat_list").child(chatroom_name).child("chatRoom_name").setValue(chatroom_name);

                // DB -> ArrayList
                updateChatRoomList_to_ArrList(rootNode_DB);


                chatroom_name_EditText.setText(""); // flsuh

                new Handler().postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        arr_adapter.notifyDataSetChanged(); // adapter 에 반영하기
                    }
                }, 500);// 0.5초 정도 딜레이를 준 후 시작



                

            }
        });








        // ListView 의 OnItemClickListener
        chatroom_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // intent 객체 생성 ( ChatroomActivity 로 넘어갈 예정 )
                Intent intent = new Intent(ChatListActivity.this, ChatroomActivity.class);

                String roomName = parent.getAdapter().getItem(position).toString();

                // 클릭한 ListView 의 item 의 내용 ( roomName )
                Toast.makeText(ChatListActivity.this, ""+roomName + "방 입장", Toast.LENGTH_SHORT).show();

                intent.putExtra("roomName", roomName);






                // 다음 activity 로 넘어가라
                startActivity(intent);





            }
        });


    }

    private void updateChatRoomList_to_ArrList(DatabaseReference rootNode_DB ) {
        // DB 에서 data 받아오기
        rootNode_DB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String chatRoomName = "";

                for (DataSnapshot ds : dataSnapshot.child("chat_list").getChildren()) {

                    // DB 에서 chatRoom name 받아오기
                    chatRoomName = ds.getValue(Chatroom.class).getChatRoom_name();

                    // ArrayList 에 추가하기
                    try {
                        if (!chatRoom_ArrList.contains(chatRoomName)) {
                            chatRoom_ArrList.add(chatRoomName);
                            Toast.makeText(ChatListActivity.this, "로딩 완료", Toast.LENGTH_SHORT).show();
                        }


                    } catch (Exception e) {
                        Toast.makeText(ChatListActivity.this, "ArrayList 에 추가하기 에서 오류발생", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
