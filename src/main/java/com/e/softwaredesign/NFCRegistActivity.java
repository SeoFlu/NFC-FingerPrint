package com.e.softwaredesign;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.FirebaseDatabase;


public class NFCRegistActivity extends AppCompatActivity{

    private FirebaseDatabase mFirebaseDatabase;

    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private String str_TagNFC;

    private EditText editProName, editStartTime,editEndTime,editClass,editDay;
    private Button okButton,cancelButton;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registnfc);
        editProName = (EditText) findViewById(R.id.editNFCname);
        editStartTime = (EditText) findViewById(R.id.editNFCstartTIme);
        editEndTime = (EditText) findViewById(R.id.editNFCendTime);
        okButton = (Button) findViewById(R.id.buttonNFCOK);
        cancelButton = (Button) findViewById(R.id.buttonNFCCancel);
        editClass = (EditText)findViewById(R.id.editNFCclassName);
        editDay = (EditText)findViewById(R.id.editNFCday);
        mFirebaseDatabase = FirebaseDatabase.getInstance();


        nfcAdapter = NfcAdapter.getDefaultAdapter(this); // default객체를 가져옴 단말기 지원 안할시 null;
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // 태그를 계속 인식할때 새로운 activity 를 만들지 않고, 현재 activity에서 intent를 받기 위함
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

    }

    @Override
    protected void onResume() { // start 이후에 시작되는 생명주기 activity가 보일때만 인식하기 위해서 enableForegroundDispatch를 선언?
        super.onResume(); // 이건 오버라이드 할때 넣어주는건가봐요
        if (nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
            /*
             * 엑티비티 - 인텐트를 전달받을 엑티비티
             * pendingIntent - 나중에 전달할 엑티비티*/
            // 해당 어댑터에서 인식되어있는 인탠트를 가져옴 this
        }
    }

    @Override
    protected void onPause() { // 다른 엑티비티가 앞으로 올경우 실행되는 생명주기
        super.onPause();
        if (nfcAdapter != null) { // 이것도 activity가 보일때만 인식하기 위해서 disableForegroundDispatch를 선언함?
            nfcAdapter.disableForegroundDispatch(this);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) { // 인터럽트처럼 끼어들때 생기는 엑티비티 생명주기?
        super.onNewIntent(intent); // 인식되면 일로 온데요 ;

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG); // nfcadapter.Extra_Tag 이름으로 tag에 넣으면 nfc태그인식인지 확인가능함
        if (tag != null) { // 그런데 null값이 아니라면
            NFCtagProfessor newNFC = new NFCtagProfessor();
            newNFC.setProfessorName(editProName.getText().toString());
            newNFC.setStartTime(Integer.parseInt(editStartTime.getText().toString()));
            newNFC.setEndTime(Integer.parseInt(editEndTime.getText().toString()));
            newNFC.setStr_className(editClass.getText().toString());
            newNFC.setStr_Day(editDay.getText().toString());

            str_TagNFC = toHexString(tag.getId());
            newNFC.setNFCkey(str_TagNFC);

            mFirebaseDatabase.getReference("Data/NFC/"+str_TagNFC).setValue(newNFC);
            finish();
        }
    }

    public static final String CHARS = "0123456789ABCDEF"; // 고정값

    public static String toHexString(byte[] data) { // 그냥함수 해당 id값이나 무슨 값에 + CHARS를 더해줌
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < data.length; ++i) { // 포오오오무우우운
            sb.append(CHARS.charAt((data[i]>>4)&0x0f)).append(CHARS.charAt(data[i]&0x0f)); // 암호?
        }
        return sb.toString();// 아무튼 해당 id값을 이러쿵 저러쿵해서 문자열로 반환시켜주는 역활을 함
    }

}
