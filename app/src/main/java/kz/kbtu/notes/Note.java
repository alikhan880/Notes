package kz.kbtu.notes;

/**
 * Created by abakh on 22-Jun-17.
 */

public class Note {

    private String id;
    private String text;
    private String author;
    private String status;
    private String date;


    public Note(String id, String text, String author, String status, String date) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.status = status;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }
}
