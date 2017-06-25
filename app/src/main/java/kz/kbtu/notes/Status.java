package kz.kbtu.notes;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by abakh on 22-Jun-17.
 */

public class Status implements Parcelable {
    private String id;
    private String name;
    private String author;

    public Status(String id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    protected Status(Parcel in) {
        id = in.readString();
        name = in.readString();
        author = in.readString();
    }

    public static final Creator<Status> CREATOR = new Creator<Status>() {
        @Override
        public Status createFromParcel(Parcel in) {
            return new Status(in);
        }

        @Override
        public Status[] newArray(int size) {
            return new Status[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(author);
    }
}
