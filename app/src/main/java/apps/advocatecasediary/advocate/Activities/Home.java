package apps.advocatecasediary.advocate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import apps.advocatecasediary.advocate.Models.CaseModel;
import apps.advocatecasediary.advocate.Adapters.CasesRecyclerViewAdapter;
import apps.webscare.advocatecasediaryadvocate.R;

public class Home extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.settingsMenuItemId:
                Intent toSettingsPage = new Intent(Home.this, Settings.class);
                startActivity(toSettingsPage);
                return true;
            case R.id.downloadScheduleItemId:
                Intent toDonwloadScheule = new Intent(Home.this, DownloadSchedule.class);
                startActivity(toDonwloadScheule);
                return true;
            case R.id.aboutUsMenuItemId:
                Intent toAboutUs = new Intent(Home.this, AboutUs.class);
                startActivity(toAboutUs);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    Button caseFileBtn , btnSendSMS;
    RecyclerView casesListRecyclerView;
    CasesRecyclerViewAdapter casesRecyclerViewAdapter;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;

    ArrayList<CaseModel> caseModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();


        caseModels = new ArrayList<>();

        caseFileBtn = findViewById(R.id.caseFileBtnId);
        casesListRecyclerView = findViewById(R.id.casesListRecyclerViewId);
        casesListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        casesListRecyclerView.setHasFixedSize(true);
        caseFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toFileCase = new Intent(Home.this, CaseRegistration.class);
                startActivity(toFileCase);
            }
        });


        firebaseFirestore.collection("Cases Registered").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                casesRecyclerViewAdapter = new CasesRecyclerViewAdapter(caseModels , getApplicationContext());
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        HashMap<String , Object> dataRecieved = (HashMap<String, Object>) documentChange.getDocument().getData();
                        CaseModel caseModel = new CaseModel();
                        caseModel.setCaseActAppllied(dataRecieved.get("Case Act Appllied").toString());
//                        caseModel.setAdvocateUD(dataRecieved.get("Advocate UD").toString());
                        caseModel.setAdvocateName(dataRecieved.get("Advocate name ").toString());
                        caseModel.setCaseFilerName(dataRecieved.get("Case Filer Name ").toString());
                        caseModel.setCaseFilerAddress(dataRecieved.get("Case Filer Address ").toString());
//                        caseModel.setAdvocateUD(dataRecieved.get("Advocate UD").toString());
                        caseModel.setCaseFilerCNIC(dataRecieved.get("Case Filer CNIC ").toString());
                        caseModel.setCaseFilerPhone(dataRecieved.get("Case Filer Phone ").toString());
                        caseModel.setCaseFullDescription(dataRecieved.get("Case Full Description ").toString());
                        caseModel.setJudgeName(dataRecieved.get("Judge Name ").toString());
                        caseModel.setOpponentName(dataRecieved.get("Opponent Name ").toString());
                        caseModel.setOpponentAddress(dataRecieved.get("Opponent Address ").toString());
                        caseModel.setOpponentPhone(dataRecieved.get("Opponent  Phone  ").toString());
                        caseModels.add(caseModel);
                        casesRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
                casesListRecyclerView.setAdapter(casesRecyclerViewAdapter);
            }
        });
    }

    /*protected void sendSMSMessage() {
//        phoneNo = casefilePhone.getText().toString();
//        message = "Dear " + caseFilerName.getText().toString()+  " Your application against " + opponentName.getText().toString() + " has been Registered Successfully!, now you can access the schedules through the app";

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage("03046282866", null, "This is a message from advocate case diary", null, null);
                Toast.makeText(getApplicationContext(), "SMS sent.",
                        Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }else {
            Toast.makeText(this, "There is some problem in sending the sms to the client", Toast.LENGTH_SHORT).show();
        }
    }*/
    public void sendSMSMessage(String phoneNo, String msg) {
       /* try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }*/


        //Getting intent and PendingIntent instance
        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        PendingIntent pi= PendingIntent.getActivity(getApplicationContext(), 0, intent,0);

        //Get the SmsManager instance and call the sendTextMessage method to send message
        SmsManager sms=SmsManager.getDefault();
        sms.sendTextMessage(phoneNo, null, msg, pi,null);

        Toast.makeText(getApplicationContext(), "Message Sent successfully!",
                Toast.LENGTH_LONG).show();
    }
}