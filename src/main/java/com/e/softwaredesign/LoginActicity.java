package com.e.softwaredesign;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActicity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;

    private EditText idEdit,pwEdit;
    private Button loginButton,registButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button)findViewById(R.id.buttonLogin);
        registButton = (Button)findViewById(R.id.buttonRegist);
        idEdit = (EditText)findViewById(R.id.editID);
        pwEdit = (EditText)findViewById(R.id.editPW);
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        registButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registIntent = new Intent(LoginActicity.this,RegistActiriy.class);
                registIntent.putExtra("IDValue",idEdit.getText().toString());
                startActivity(registIntent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    mFirebaseDatabase.getReference("Data/ClientList/"+idEdit.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Client client = dataSnapshot.getValue(Client.class);
                            boolean b_id,b_pw;
                            String idvalue,pwvalue,stuValue;
                            stuValue = client.getStuName();
                            idvalue = idEdit.getText().toString();
                            pwvalue = pwEdit.getText().toString();
                            b_id = idvalue.equals(client.getID());
                            b_pw = pwvalue.equals(client.getPW());
                            if(b_id && b_pw){
                                //로그인 성공
                                Intent loginIntent = new Intent(LoginActicity.this,MainActivity.class);
                                loginIntent.putExtra("stu_info",idEdit.getText().toString()+"\n"+stuValue+"님 안녕하세요.");
                                startActivity(loginIntent);
                            }else{
                                //비밀번호 오류
                                Toast.makeText(getApplicationContext(),"비밀번호가 틀렸습니다.",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getApplicationContext(),"아이디 값이 없습니다\n등록해주세요",Toast.LENGTH_SHORT).show();
                        }

                    });

                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),"아이디 값이 없습니다\n등록해주세요",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}
