 package apps.webscare.advocatecasediaryadvocate.Activities;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import apps.webscare.advocatecasediaryadvocate.R;
import io.grpc.Context;

public class SignUp extends AppCompatActivity {

    ImageView profilePictreImageView;
    int PICK_IMAGE = 1;
    EditText nameEt, emailET, phoneET, cityET, experienceEt, cnicET, educationET, passwordET;
    Button submitBtn;
    TextView selectProfileImageText;
    ProgressBar progressBar;

    FirebaseAuth mAuth;
    StorageReference mStorage;
    FirebaseFirestore firebaseFirestore;

    String uID;
    Uri imageUri;

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
        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        imageUri = null;

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
        selectProfileImageText = findViewById(R.id.selectImageTextViewID);
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
                        || cnicET.getText().toString().isEmpty()) {
                    nameEt.setError("This Field is Required");
                    emailET.setError("This Field is Required");
                    cityET.setError("This Field is Required");
                    cnicET.setError("This Field is Required");
                    experienceEt.setError("This Field is Required");
                    phoneET.setError("This Field is Required");
                    progressBar.setVisibility(View.GONE);
                } else {

                    mAuth.createUserWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                uID = mAuth.getCurrentUser().getUid();
                                StorageReference user_profile_image = mStorage.child(uID + ".jpg");
                                if (imageUri != null) {
                                    user_profile_image.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            if (taskSnapshot.getMetadata() != null) {
                                                if (taskSnapshot.getMetadata().getReference() != null) {
                                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            String downlodUrl = uri.toString();
                                                            Toast.makeText(SignUp.this, "URL : " + downlodUrl, Toast.LENGTH_SHORT).show();
                                                            final Map<String, Object> advocateDataMap = new HashMap<>();
                                                            if (downlodUrl != null)
                                                                advocateDataMap.put("image_url" , downlodUrl);
                                                            else
                                                                Toast.makeText(SignUp.this, "Download Url is null", Toast.LENGTH_SHORT).show();
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
                                                                    Toast.makeText(SignUp.this, "Advocate Registered Successfully", Toast.LENGTH_SHORT).show();
                                                                    Intent toMain = new Intent(SignUp.this, MainActivity.class);
                                                                    startActivity(toMain);
                                                                    finish();
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(SignUp.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                                                    progressBar.setVisibility(View.GONE);
                                                                }
                                                            });
                                                        }
                                                    });
                                                }
                                            }
                                        }});

                                    /*user_profile_image.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            String downlodUrl = task.getResult().getStorage().getDownloadUrl().toString();
                                            final Map<String, Object> advocateDataMap = new HashMap<>();
                                            if (downlodUrl != null)
                                                advocateDataMap.put("image_url" , downlodUrl);
                                            else
                                                Toast.makeText(SignUp.this, "Download Url is null", Toast.LENGTH_SHORT).show();
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
                                                    Toast.makeText(SignUp.this, "Advocate Registered Successfully", Toast.LENGTH_SHORT).show();
                                                    Intent toMain = new Intent(SignUp.this, MainActivity.class);
                                                    startActivity(toMain);
                                                    finish();
                                                }
                                            });
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUp.this, "Failed to upload image", Toast.LENGTH_SHORT).show();     
                                        }
                                    });*/
                                }else {
                                    Toast.makeText(SignUp.this, "Image Uri is Null", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                               /* StorageReference user_profile = mStorage.child(uID +".jpg");
                                user_profile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {
                                        if (uploadTask.isSuccessful()){

//                                            String downloadUrl = uploadTask.getResult();

                                        }else{
                                            Toast.makeText(SignUp.this, "Error : " + uploadTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });*/
                               
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
        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            if (imageUri != null)
                profilePictreImageView.setImageURI(imageUri);
            else
                Toast.makeText(this, "No Image Selected", Toast.LENGTH_SHORT).show();
        }
    }
}