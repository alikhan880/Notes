package kz.kbtu.notes;

/**
 * Created by abakh on 22-Jun-17.
 */

public class Status {
    private String id;
    private String name;
    private String author;

    public Status(String id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }
}
