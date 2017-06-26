package kz.kbtu.notes;

/**
 * Created by abakh on 22-Jun-17.
 */

public class Note {

    private String id;
    private String text;
    private String author;
    private String author_name;
    private String status_id;
    private String status_name;
    private String date;


    public Note(String id, String text, String author, String author_name, String status_id, String status_name, String date) {
        this.id = id;
        this.text = text;
        this.author = author;
        this.author_name = author_name;
        this.status_id = status_id;
        this.status_name = status_name;
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

    public String getAuthor_name() {
        return author_name;
    }

    public String getStatus_id() {
        return status_id;
    }

    public String getStatus_name() {
        return status_name;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Note: " + text + "\n" +
                "Author: " + author_name + "\n" +
                "Status: " + status_name + "\n" +
                "Last edited: " + date;
    }
}
