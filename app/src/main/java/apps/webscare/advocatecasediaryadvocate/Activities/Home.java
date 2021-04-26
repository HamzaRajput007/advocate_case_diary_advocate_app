package apps.webscare.advocatecasediaryadvocate.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import apps.webscare.advocatecasediaryadvocate.Adapters.CasesRecyclerViewAdapter;
import apps.webscare.advocatecasediaryadvocate.Models.CaseModel;
import apps.webscare.advocatecasediaryadvocate.R;

public class Home extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore firebaseFirestore;

    Button caseFileBtn;
    RecyclerView casesListRecyclerView;
    CasesRecyclerViewAdapter casesRecyclerViewAdapter;

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
}