package apps.advocatecasediary.advocate.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import apps.advocatecasediary.advocate.Models.ScheduleModel;
import apps.webscare.advocatecasediaryadvocate.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class DownloadSchedule extends AppCompatActivity {
    LinearLayout scheduleLayout;
    TextView scheduleNameTextView;
    DatabaseReference mDatabaseReference;
    ScheduleModel scheduleModel;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    Button downloadScheduleBtn;
    String donwloadUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_schedule);
        scheduleLayout = findViewById(R.id.documentlayoutId);
        downloadScheduleBtn = findViewById(R.id.downloadScheduleBtnId);
        scheduleNameTextView = findViewById(R.id.scheleFileNameTextViewId);

        storage = FirebaseStorage.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        scheduleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent toDownloadFile = new Intent(Intent.ACTION_VIEW);
                toDownloadFile.setData(Uri.parse(donwloadUrl));
                startActivity(toDownloadFile);
            }
        });
        downloadScheduleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirestore.collection("Schedules").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                HashMap<String , Object> schduleHasMap = (HashMap<String, Object>) documentChange.getDocument().getData();
                                scheduleModel = documentChange.getDocument().toObject(ScheduleModel.class);
                                if (scheduleModel != null) {
                                    scheduleNameTextView.setText(scheduleModel.getName() + ".pdf");
                                    scheduleLayout.setVisibility(View.VISIBLE);
                                    donwloadUrl = schduleHasMap.get("image_url").toString();
//                                    finish();
                                }
                            }
                        }
                    }
                });
            }
        });
    }
}