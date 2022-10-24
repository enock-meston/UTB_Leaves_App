package com.nigoote.utb_leave_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nigoote.utb_leave_app.fragments.MyLeavesFragment;

import java.util.List;

public class ActionListAdapter extends RecyclerView.Adapter<ActionListAdapter.viewHolder>{
    Context ctx;
    LayoutInflater inflater;
    List<DataActionStatus> actionList;

    public ActionListAdapter(Context ctx, List<DataActionStatus> actionList) {
        this.ctx = ctx;
        this.actionList = actionList;

        inflater =LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customer_actions_status,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DataActionStatus actionStatus = actionList.get(position);
        holder.title.setText(actionStatus.getTitle());
        holder.supervisor.setText(actionStatus.getSp());
        holder.hr.setText(actionStatus.getHr());
        holder.dvcpaf.setText(actionStatus.getDvcf());
        holder.dvca.setText(actionStatus.getDvca());
        holder.vc.setText(actionStatus.getVc());
    }


    @Override
    public int getItemCount() {
        return actionList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title,supervisor,hr,dvcpaf,dvca,vc;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.cus_status_Title);
            supervisor = itemView.findViewById(R.id.cus_status_StatusSupervisor);
            hr = itemView.findViewById(R.id.cus_status_HR);
            dvcpaf = itemView.findViewById(R.id.cus_status_DVCPAF);
            dvca = itemView.findViewById(R.id.cus_status_DVCA);
            vc = itemView.findViewById(R.id.cus_VC);
        }
    }
}
