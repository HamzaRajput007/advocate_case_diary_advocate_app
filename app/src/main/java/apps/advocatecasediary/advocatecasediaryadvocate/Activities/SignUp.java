package apps.advocatecasediary.advocatecasediaryadvocate.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import apps.advocatecasediary.advocatecasediaryadvocate.R;

public class SignUp extends AppCompatActivity {

    ImageView profilePictreImageView;
    int PICK_IMAGE = 1;
    EditText nameEt , emailET , phoneET , cityET , experienceEt , cnicET , educationET , passwordET;
    Button submitBtn;
    TextView selectProfileImageText;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    String uID;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        nameEt = findViewById(R.id.advocateNameID);
        emailET = findViewById(R.id.EMAILNameID);
        cityET = findViewById(R.id.cityNameID);
        experienceEt = findViewById(R.id.experienceNameID);
        cnicET = findViewById(R.id.cnicNameID);
        educationET = findViewById(R.id.educationNameID);
        passwordET = findViewById(R.id.advocatePasswordId);
        submitBtn = findViewById(R.id.addAdvocateBtnId);
        phoneET = findViewById(R.id.phoneNumberID);
        progressBar = findViewById(R.id.progressBar);
        selectProfileImageText  = findViewById(R.id.selectImageTextViewID);
        profilePictreImageView = findViewById(R.id.profile_image);
        profilePictreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toPickImage = new Intent();
                toPickImage.setType("image/*");
                toPickImage.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(toPickImage, "Select Picture"), PICK_IMAGE);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                if (nameEt.getText().toString().isEmpty()
                        || emailET.getText().toString().isEmpty()
                        || cityET.getText().toString().isEmpty()
                        || experienceEt.getText().toString().isEmpty()
                        || passwordET.getText().toString().isEmpty()
                        || phoneET.getText().toString().isEmpty()
                        || cnicET.getText().toString().isEmpty()){
                    nameEt.setError("This Field is Required");
                    emailET.setError("This Field is Required");
                    cityET.setError("This Field is Required");
                    cnicET.setError("This Field is Required");
                    experienceEt.setError("This Field is Required");
                    phoneET.setError("This Field is Required");
                    progressBar.setVisibility(View.GONE);
                } else  {
                    final Map<String , Object> advocateDataMap = new HashMap<>();
                    advocateDataMap.put("name" , nameEt.getText().toString());
                    advocateDataMap.put("email" , emailET.getText().toString());
                    advocateDataMap.put("city" , cityET.getText().toString());
                    advocateDataMap.put("experience" , experienceEt.getText().toString());
                    advocateDataMap.put("cnic" , cnicET.getText().toString());
                    advocateDataMap.put("phone_number" , phoneET.getText().toString());
                    advocateDataMap.put("type" , "advocate");

                    mAuth.createUserWithEmailAndPassword(emailET.getText().toString() , passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()){
                                uID = mAuth.getCurrentUser().getUid();
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
                                        Toast.makeText(SignUp.this, "Advocate Registered Successfully", Toast.LENGTH_SHORT).show();
                                        Intent toMain = new Intent(SignUp.this , apps.webscare.advocatecasediaryadvocate.Activities.MainActivity.class);
                                        startActivity(toMain);
                                        finish();
                                    }
                                });
                            }
                        }
                    });
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){

        }
    }
}