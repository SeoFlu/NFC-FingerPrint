package com.e.softwaredesign;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private FirebaseDatabase mFirebaseDatabase;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    private TextView NFCvalue,textStu;
    private Button NFCregist,NFCfingerprint;

    private String nfcName,nfcClass;
    private boolean b_NFC;
    private int i_Time;
    private String str_weekend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NFCvalue = (TextView)findViewById(R.id.nfcTXT);
        textStu = (TextView)findViewById(R.id.stuNameTXT);
        NFCregist = (Button)findViewById(R.id.buttonRegistNFC);
        NFCfingerprint =(Button)findViewById(R.id.buttonFingerprint);
        nfcAdapter = NfcAdapter.getDefaultAdapter(this); // 디폴트 nfcadapter객체를 가져옴 근데 단말기에서 지원 안하면 null을 반환
        Intent intent = new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); // 태그를 계속 인식할때 새로운 activity 를 만들지 않고, 현재 activity에서 intent를 받기 위함
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0); // 이건 모르겠음
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        Calendar cal = new GregorianCalendar();
        i_Time = cal.get(cal.HOUR);
        str_weekend = new SimpleDateFormat("E").format(System.currentTimeMillis());
        Intent stuValueIntent = new Intent(this.getIntent());
        textStu.setText(stuValueIntent.getStringExtra("stu_info"));

        NFCregist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registNFCIntent = new Intent(MainActivity.this , NFCRegistActivity.class);
                startActivity(registNFCIntent);
            }
        });

        NFCfingerprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!b_NFC){
                    Toast.makeText(getApplicationContext(),"NFC를 먼저 입력해주세요",Toast.LENGTH_LONG).show();
                }else{
                    Intent fingerprintIntent = new Intent(MainActivity.this,FingerAuthActivity.class);
                    fingerprintIntent.putExtra("className",nfcClass);
                    fingerprintIntent.putExtra("professorName",nfcName);
                    startActivity(fingerprintIntent);
                }
            }
        });

    }

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
        super.onNewIntent(intent); // 인식되면 일로 온데요;

        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        // nfcadapter.Extra_Tag 이름으로 tag에 넣으면 nfc태그인식인지 확인가능함 EXTRA_TAG쓰면 태그 읽어옴
        if (tag != null){
            String strTagid = toHexString(tag.getId());
            mFirebaseDatabase.getReference("Data/NFC/"+strTagid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    NFCtagProfessor NFCtag = dataSnapshot.getValue(NFCtagProfessor.class);
                    if((str_weekend.equals(NFCtag.getStr_Day()))&&(i_Time>=NFCtag.getStartTime()&&i_Time<=NFCtag.getEndTime())){
                        nfcName = NFCtag.getProfessorName();
                        nfcClass = NFCtag.getStr_className();
                        NFCvalue.setText(nfcName+" 교수님\n인식된 강의 : "+nfcClass);
                        b_NFC = true;
                    }else{
                        Toast.makeText(getApplicationContext(),"현재 진행중인 강의가 아닙니다.",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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
