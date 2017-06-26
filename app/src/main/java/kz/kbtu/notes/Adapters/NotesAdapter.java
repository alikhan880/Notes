package kz.kbtu.notes.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import kz.kbtu.notes.Interfaces.RecyclerItemClickListener;
import kz.kbtu.notes.Note;
import kz.kbtu.notes.R;


/**
 * Created by abakh on 23-Jun-17.
 */

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {
    private ArrayList<Note> notes;
    private RecyclerItemClickListener listener;

    public NotesAdapter(ArrayList<Note> notes, RecyclerItemClickListener listener) {
        this.notes = notes;
        this.listener = listener;
    }

    public Note getItem(int position){
        return notes.get(position);
    }


    @Override
    public NotesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_main, parent, false);
        return new NotesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(NotesViewHolder holder, int position) {
        Note note = notes.get(position);
        holder.tvText.setText(note.getText());
        holder.tvDate.setText(note.getDate());
        holder.tvStatus.setText(note.getStatus_name());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView tvText;
        TextView tvDate;
        TextView tvStatus;

        public NotesViewHolder(View itemView) {
            super(itemView);
            tvText = (TextView)itemView.findViewById(R.id.item_text);
            tvDate = (TextView)itemView.findViewById(R.id.item_date);
            tvStatus = (TextView)itemView.findViewById(R.id.item_status);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.itemClicked(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
            listener.itemLongClicked(getAdapterPosition());
            return true;
        }
    }
}
