package kz.kbtu.notes.Interfaces;

/**
 * Created by abakh on 23-Jun-17.
 */

public interface RecyclerItemClickListener {

    void btnDeleteClicked(int adapterPosition);
    void itemClicked(int adapterPosition);
    void itemLongClicked(int adapterPosition);
}
