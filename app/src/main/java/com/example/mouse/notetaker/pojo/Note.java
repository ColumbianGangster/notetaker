package com.example.mouse.notetaker.pojo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by TAE_user2 on 17/03/2016.
 */
public class Note implements Parcelable{
    public String id;
    public String title;
    public String content;
    public String details;
    public Long date;

    public Note(String id, String title, String content, String details) {
        // The constructor does not allow the programmer to give a date - therefore, the modified date is safe from programmer error
        this.id = id;
        this.title = title;
        this.content = content;
        this.details = details;
        // http://stackoverflow.com/questions/2168374/what-is-the-best-practice-for-manipulating-and-storing-dates-in-java
        this.date = System.currentTimeMillis();
    }

    protected Note(Parcel in) {
        id = in.readString();
        title = in.readString();
        content = in.readString();
        details = in.readString();
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    @Override
    public String toString() {
        return content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(content);
        dest.writeString(details);
    }

//    public void add(Note newNote) {
//        Notes.addNote(newNote);
//    }
//
//    public int count() {
//        return Notes.COUNT;
//    }
}
