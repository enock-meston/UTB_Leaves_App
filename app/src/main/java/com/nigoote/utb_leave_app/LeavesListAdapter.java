package com.nigoote.utb_leave_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LeavesListAdapter extends RecyclerView.Adapter<LeavesListAdapter.viewHolder> {
    List<DataLeave> leavesTitle;
    Context ctx;
    LayoutInflater inflater;
    private SelectLeaves selectLeaves;
    public LeavesListAdapter(Context ctx,List<DataLeave> leavesTitle,SelectLeaves selectLeaves) {
        this.leavesTitle = leavesTitle;
        this.ctx = ctx;
        this.selectLeaves = selectLeaves;
        inflater =LayoutInflater.from(ctx);
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.customer_leaves_list,parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        DataLeave dataLeave =leavesTitle.get(position);
        holder.title.setText(dataLeave.getTitle());
    }


    @Override
    public int getItemCount() {
        return leavesTitle.size();
    }

    public interface SelectLeaves{
        void selectLeaves(DataLeave dataLeave);
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView title,type;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.custom_leav_Title);
            type = itemView.findViewById(R.id.custom_leav_Type);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectLeaves.selectLeaves(leavesTitle.get(getAdapterPosition()));
                    Toast.makeText(ctx, "id"+getAdapterPosition(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
