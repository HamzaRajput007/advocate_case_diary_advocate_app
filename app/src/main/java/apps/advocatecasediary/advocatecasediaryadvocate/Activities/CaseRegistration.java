package apps.webscare.advocatecasediaryadvocate.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import apps.webscare.advocatecasediaryadvocate.R;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class CaseRegistration extends AppCompatActivity {

    EditText caseFilerName, caseFilerDescription, casefilePhone, casefilerCnic, caseFilerAddress, opponentName, opponentPhone, opponentAddress;
    Spinner actsListSpinner;
    Button registerCaseBtn;
    ProgressBar progressBar;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;

    String uID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_registration);

        mAuth = FirebaseAuth.getInstance();
        uID = mAuth.getUid();

        firebaseFirestore = FirebaseFirestore.getInstance();

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
        actsListSpinner.setAdapter(aa);

        firebaseFirestore.collection("Advocates").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

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

                    /*firebaseFirestore.collection("Advocates").document(uID).collection("Cases").document(uID).set(caseValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(CaseRegistration.this, "Case Registered Succefully", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CaseRegistration.this, "Failed to  Register Case", Toast.LENGTH_SHORT).show();
                        }
                    });*/
                }
            }
        });


    }
}