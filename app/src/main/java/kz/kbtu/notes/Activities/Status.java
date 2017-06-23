package kz.kbtu.notes.Activities;

/**
 * Created by abakh on 22-Jun-17.
 */

public class Status {
    private String id;
    private String name;

    public Status(String id, String name) {
        this.id = id;
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
