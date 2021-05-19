package apps.advocatecasediary.advocate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import apps.advocatecasediary.advocate.Models.AdminModel;
import apps.webscare.advocatecasediaryadvocate.R;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.annotation.AnnotationTypeMismatchException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CaseRegistration extends AppCompatActivity {

    EditText caseFilerName, caseFilerDescription, casefilePhone, casefilerCnic, caseFilerAddress, opponentName, opponentPhone, opponentAddress;
    TextView advocateNameTv , judgeNameTv;
    Spinner actsListSpinner;
    Button registerCaseBtn;
    ProgressBar progressBar;
    int judngeIndex = 0; 
//    SharedPreferences sharedPreferences;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String judgeNumber = "JudgeNumber";

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;

    String uID, advocateName, judgeName;
    ArrayList<AdminModel> adminModels ;
    private String message;
    private String phoneNo;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_registration);

        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getUid();

        adminModels = new ArrayList<>();

        firebaseFirestore = FirebaseFirestore.getInstance();

        final SharedPreferences sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putInt(judgeNumber , 0);
        editor.commit();

        firebaseFirestore.collection("Admins").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for (DocumentChange documentChange : value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        HashMap<String , Object> dataRecieved = (HashMap<String, Object>) documentChange.getDocument().getData();
                        AdminModel adminModel = new AdminModel();
                        adminModel.setCNIC(dataRecieved.get("CNIC").toString());
                        adminModel.setName(dataRecieved.get("name").toString());
                        adminModel.setPhone(dataRecieved.get("phone").toString());
                        adminModel.setType(dataRecieved.get("type").toString());
                    }
                }
            }
        });

        String[] actsList = {"--Select An Act--", "Act 001 (A)", "Act 001 (B)", "Act 002 (A)", "Act 002 (B)", "Act 003 (A)", "Act 004 (A)",};
//        SpinnerAdapter arrayAdapter = new SpinnerAdapter(this , R.layout.support_simple_spinner_dropdown_item , actsList);

        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, actsList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        caseFilerName = findViewById(R.id.caseFilerNameId);
        casefilePhone = findViewById(R.id.caseFilerPhoneNumberId);
        casefilerCnic = findViewById(R.id.caseFilerCnicId);
        caseFilerAddress = findViewById(R.id.caseFilerAddressId);
        opponentName = findViewById(R.id.caseOpponentNameId);
        opponentPhone = findViewById(R.id.caseOpponentPhoneNumberId);
        opponentAddress = findViewById(R.id.caseOpponentAddressId);
        caseFilerDescription = findViewById(R.id.caseFilerDescriptionID);
        registerCaseBtn = findViewById(R.id.registerCaseBtnId);
        actsListSpinner = findViewById(R.id.actsSpinner);
        progressBar = findViewById(R.id.progressbarIdCaseRegisteration);
        advocateNameTv = findViewById(R.id.caseAdvocateName);
        judgeNameTv = findViewById(R.id.caseJudgeName);
        actsListSpinner.setAdapter(aa);

        firebaseFirestore.collection("Advocates").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                advocateName = documentSnapshot.get("name").toString();
                if (advocateName != null){
                    advocateNameTv.setText(advocateName);
                }
            }
        });

        firebaseFirestore.collection("Admins").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                try {
                    for (DocumentChange documentChange : value.getDocumentChanges()){
                        if (documentChange.getType() == DocumentChange.Type.ADDED){
                            String uid = documentChange.getDocument().getId();
                            HashMap<String , Object> recievedData = (HashMap<String, Object>) documentChange.getDocument().getData();
                            AdminModel adminModel  = documentChange.getDocument().toObject(AdminModel.class).withId(uid);
                            adminModel.setUserId(uid);
                            adminModels.add(adminModel);
//                            Toast.makeText(CaseRegistration.this, adminModels.size(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (adminModels.size() != 0) {
                        Toast.makeText(CaseRegistration.this, "Before : " + String.valueOf(judngeIndex), Toast.LENGTH_SHORT).show();
                        judngeIndex = sharedpreferences.getInt(judgeNumber , -1);
                        judgeNameTv.setText(adminModels.get(judngeIndex++).getName());
//                        Toast.makeText(CaseRegistration.this, String.valueOf(judngeIndex), Toast.LENGTH_SHORT).show();
                        if (judngeIndex < adminModels.size()){
                            SharedPreferences.Editor editor1 = sharedpreferences.edit();
                            editor1.putInt(judgeNumber , judngeIndex);
                            editor.commit();
                            Toast.makeText(CaseRegistration.this,"After : " + String.valueOf(sharedpreferences.getInt(judgeNumber,-1)), Toast.LENGTH_SHORT).show();
                        }
                    }
                }catch (Exception exception){
                    Log.d("TAG_EXCEPTION", "onEvent: " + error.getMessage());
                    Toast.makeText(CaseRegistration.this, "Error : " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerCaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (caseFilerName.getText().toString().isEmpty() ||
                        casefilePhone.getText().toString().isEmpty() ||
                        casefilerCnic.getText().toString().isEmpty() ||
                        caseFilerAddress.getText().toString().isEmpty() ||
                        opponentName.getText().toString().isEmpty() ||
                        opponentPhone.getText().toString().isEmpty() ||
                        opponentAddress.getText().toString().isEmpty() ||
                        caseFilerDescription.getText().toString().isEmpty()
                ) {
                    Toast.makeText(CaseRegistration.this, "Invalid Input ", Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    Map<String, Object> caseValues = new HashMap<>();
                    caseValues.put("Advocate UD" , uID);
                    caseValues.put("Case Filer Name ", caseFilerName.getText().toString());
                    caseValues.put("Case Filer Phone ", casefilePhone.getText().toString());
                    caseValues.put("Case Filer CNIC ", casefilerCnic.getText().toString());
                    caseValues.put("Case Filer Address ", caseFilerAddress.getText().toString());
                    caseValues.put("Opponent Name ", opponentName.getText().toString());
                    caseValues.put("Opponent  Phone  ", opponentPhone.getText().toString());
                    caseValues.put("Opponent Address ", opponentAddress.getText().toString());
                    caseValues.put("Case Full Description ", caseFilerDescription.getText().toString());
                    caseValues.put("Case Act Appllied", actsListSpinner.getSelectedItem().toString());
                    caseValues.put("Advocate name " , "Naqash");
                    caseValues.put("Judge Name " , "Adbul Rehman Ghumman");

                    firebaseFirestore.collection("Cases Registered").document(casefilerCnic.getText().toString()+ " - " + opponentPhone.getText().toString()).set(caseValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            caseFilerName.setText("");
                            casefilePhone.setText("");
                            casefilerCnic.setText("");
                            caseFilerAddress.setText("");
                            opponentName.setText("");
                            opponentPhone.setText("");
                            opponentAddress.setText("");
                            caseFilerDescription.setText("");
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CaseRegistration.this, "Case Filed Successfully!", Toast.LENGTH_SHORT).show();
                            Intent toHome = new Intent(CaseRegistration.this , Home.class);
                            startActivity(toHome);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CaseRegistration.this, "Failed To File Case Online", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            Log.d("FiledFailed", "onFailure: " + e.getMessage());
                        }
                    });

                }
            }
        });


    }

    /*protected void sendSMSMessage() {
        phoneNo = casefilePhone.getText().toString();
        message = "Dear " + caseFilerName.getText().toString()+  " Your application against " + opponentName.getText().toString() + " has been Registered Successfully!, now you can access the schedules through the app";

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phoneNo, null, message, null, null);
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

    /*@Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(phoneNo, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }*/
}