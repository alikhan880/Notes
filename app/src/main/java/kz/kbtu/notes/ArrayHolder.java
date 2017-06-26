package kz.kbtu.notes;

import java.util.ArrayList;

/**
 * Created by abakh on 26-Jun-17.
 */

public class ArrayHolder {
    private static ArrayHolder arrayHolder;
    public ArrayList<Note> notes;
    public ArrayList<Status> statuses;
    public ArrayList<String> labels;

    private ArrayHolder() {
        this.notes = new ArrayList<>();
        this.statuses = new ArrayList<>();
        this.labels = new ArrayList<>();
    }

    public static ArrayHolder getInstance(){
        if(arrayHolder == null){
            arrayHolder = new ArrayHolder();
        }
        return arrayHolder;
    }



}
