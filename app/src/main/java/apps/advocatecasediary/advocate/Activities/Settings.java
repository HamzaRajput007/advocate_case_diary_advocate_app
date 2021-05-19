
package apps.advocatecasediary.advocate.Activities;

import androidx.appcompat.app.AppCompatActivity;
import apps.webscare.advocatecasediaryadvocate.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    ImageView profilePictreImageView;
    int PICK_IMAGE = 1;
    EditText nameEt , emailET , phoneET , cityET , experienceEt , cnicET , educationET , passwordET;
    Button submitBtn;
    TextView selectProfileImageText;
    ProgressBar progressBar;

    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    String uID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        uID = mAuth.getCurrentUser().getUid();

        nameEt = findViewById(R.id.advocateNameIDSettings);
        emailET = findViewById(R.id.EMAILNameIDSettings);
        cityET = findViewById(R.id.cityNameID);
        experienceEt = findViewById(R.id.experienceNameID);
        cnicET = findViewById(R.id.cnicNameID);
        educationET = findViewById(R.id.educationNameID);
        passwordET = findViewById(R.id.advocatePasswordId);
        submitBtn = findViewById(R.id.addAdvocateBtnId);
        phoneET = findViewById(R.id.phoneNumberID);
        progressBar = findViewById(R.id.progressBar);
        selectProfileImageText  = findViewById(R.id.selectImageTextViewID);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseFirestore.collection("Advocates").document(uID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (nameEt.getText().toString().isEmpty()
                                || emailET.getText().toString().isEmpty()
                                || cityET.getText().toString().isEmpty()
                                || experienceEt.getText().toString().isEmpty()
                                || passwordET.getText().toString().isEmpty()
                                || phoneET.getText().toString().isEmpty()
                                || cnicET.getText().toString().isEmpty()) {
                            nameEt.setError("This Field is Required");
                            emailET.setError("This Field is Required");
                            cityET.setError("This Field is Required");
                            cnicET.setError("This Field is Required");
                            experienceEt.setError("This Field is Required");
                            phoneET.setError("This Field is Required");
                            progressBar.setVisibility(View.GONE);
                        } else {
                            final Map<String, Object> advocateDataMap = new HashMap<>();
                            advocateDataMap.put("name", nameEt.getText().toString());
                            advocateDataMap.put("email", emailET.getText().toString());
                            advocateDataMap.put("city", cityET.getText().toString());
                            advocateDataMap.put("experience", experienceEt.getText().toString());
                            advocateDataMap.put("cnic", cnicET.getText().toString());
                            advocateDataMap.put("phone_number", phoneET.getText().toString());
                            advocateDataMap.put("type", "advocate");

                            firebaseFirestore.collection("Advocates").document(uID).set(advocateDataMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    nameEt.setText("");
                                    emailET.setText("");
                                    passwordET.setText("");
                                    cnicET.setText("");
                                    cityET.setText("");
                                    experienceEt.setText("");
                                    educationET.setText("");
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Settings.this, "Advocate Registered Successfully", Toast.LENGTH_SHORT).show();
                                    Intent toMain = new Intent(Settings.this , Home.class);
                                    startActivity(toMain);
                                    finish();
                                }
                            });
                        }
                    }
                });
            }
        });


    }
}