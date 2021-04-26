package apps.webscare.advocatecasediaryadvocate.Adapters;

import android.appwidget.AppWidgetHost;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import apps.webscare.advocatecasediaryadvocate.Models.CaseModel;
import apps.webscare.advocatecasediaryadvocate.R;

public class CasesRecyclerViewAdapter extends RecyclerView.Adapter<CasesRecyclerViewAdapter.CaseViewHolder> {

    ArrayList<CaseModel> caseModels;
    Context mContext;

    public CasesRecyclerViewAdapter(ArrayList<CaseModel> caseModels, Context mContext) {
        this.caseModels = caseModels;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public CaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.case_item_layout , parent , false);
        CaseViewHolder caseViewHolder = new CaseViewHolder(v);
        return caseViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CaseViewHolder holder, int position) {
        holder.filerName.setText(caseModels.get(position).getCaseFilerName());
        holder.filerPhone.setText(caseModels.get(position).getCaseFilerPhone());
        holder.filerAct.setText(caseModels.get(position).getCaseActAppllied());
    }

    @Override
    public int getItemCount() {
        return caseModels.size();
    }

    public  class CaseViewHolder extends RecyclerView.ViewHolder{

        TextView filerName , filerPhone , filerAct;
        public CaseViewHolder(@NonNull View itemView) {
            super(itemView);
            filerAct = itemView.findViewById(R.id.filerActID);
            filerName = itemView.findViewById(R.id.filerNameID);
            filerPhone = itemView.findViewById(R.id.filerPhoneID);
        }
    }
}
