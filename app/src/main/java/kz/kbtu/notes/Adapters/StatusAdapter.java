package kz.kbtu.notes.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kz.kbtu.notes.Status;
import kz.kbtu.notes.Interfaces.RecyclerItemClickListener;
import kz.kbtu.notes.R;

/**
 * Created by abakh on 23-Jun-17.
 */

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    private ArrayList<Status> statuses;
    private RecyclerItemClickListener listener;

    public StatusAdapter(ArrayList<Status> statuses, RecyclerItemClickListener listener) {
        this.statuses = statuses;
        this.listener = listener;
    }

    public Status getItem(int position){
        return statuses.get(position);
    }

    @Override
    public StatusViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_status, parent, false);
        return new StatusViewHolder(v);
    }

    @Override
    public void onBindViewHolder(StatusViewHolder holder, int position) {
        Status status = statuses.get(position);
        holder.tvStatus.setText(status.getName());
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    class StatusViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvStatus;
        Button btnDelete;
        StatusViewHolder(View itemView) {
            super(itemView);
            tvStatus = (TextView)itemView.findViewById(R.id.item_status_text);
            tvStatus.setOnClickListener(this);
            btnDelete = (Button)itemView.findViewById(R.id.item_status_delete_button);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.item_status_text:
                    listener.itemClicked(getAdapterPosition());
                    break;
                case R.id.item_status_delete_button:
                    listener.btnDeleteClicked(getAdapterPosition());
                    break;
            }
        }

    }
}
