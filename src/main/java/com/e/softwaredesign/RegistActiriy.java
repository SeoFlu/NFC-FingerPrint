package com.e.softwaredesign;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;

public class RegistActiriy extends AppCompatActivity {

    FirebaseDatabase mFirebaseDatabase;

    Button okButton,cancelButton;
    EditText idEdit,pwEdit,nameEdit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        okButton = (Button)findViewById(R.id.buttonOK);
        cancelButton = (Button)findViewById(R.id.buttonCancel);
        idEdit = (EditText)findViewById(R.id.editID);
        pwEdit = (EditText)findViewById(R.id.editPW);
        nameEdit = (EditText)findViewById(R.id.editName);

        mFirebaseDatabase = FirebaseDatabase.getInstance();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //데이터베이스안에 집어넣기
                Client newClient = new Client();
                newClient.setID(idEdit.getText().toString());
                newClient.setPW(pwEdit.getText().toString());
                newClient.setStuName(nameEdit.getText().toString());


                mFirebaseDatabase.getReference("Data/ClientList/"+idEdit.getText().toString()).setValue(newClient);
                //그리고 끝낸다.
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //해당 뷰를 끝낸다.
                finish();
            }
        });
    }
}
