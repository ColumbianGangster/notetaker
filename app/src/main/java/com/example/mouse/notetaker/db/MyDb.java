package com.example.mouse.notetaker.db;

import android.content.Context;

import com.example.mouse.notetaker.pojo.Note;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by TAE_user2 on 16/03/2016.
 */
public class MyDb {
    private Realm realm;
    public MyDb(Context context){
        RealmConfiguration realmConfig = new RealmConfiguration.Builder(context).build();
        this.realm = Realm.getInstance(realmConfig);
    }

    public void persist(NoteDb notedb){
        realm.beginTransaction();
        realm.copyToRealm(notedb);
        realm.commitTransaction();
    }

    public Boolean checkIfExists(String title){
        Boolean ret = true;
        RealmResults<NoteDb> notes = realm.where(NoteDb.class).equalTo("title", title).findAll();
        if(notes.size()== 0){
            ret = false;
        }
        return ret;
    }
    public List<Note> getAll(){
        RealmResults<NoteDb> dbNotes = realm.allObjects(NoteDb.class);
        dbNotes.sort("date", Sort.DESCENDING);
        ArrayList<Note> notes = new ArrayList<>();
        for(NoteDb n : dbNotes){
            Note note = new Note(n.getId(),n.getTitle(),n.getContent(),"");
            note.date = n.getDate(); // custom dates must always be explicitly added
            notes.add(note);
        }
        return notes;
    }
    public Boolean hasNotes(){
        return realm.isEmpty();
    }
    public void removeNote(Note note){
        realm.beginTransaction();
        RealmResults<NoteDb> results = realm.where(NoteDb.class)
                .equalTo("title", note.title).findAll();
        results.clear();
        realm.commitTransaction();
    }
    public void addNote(Note note){
        realm.beginTransaction();
        NoteDb noteDb = realm.createObject(NoteDb.class); // Create a new object
        noteDb.setId(note.id);
        noteDb.setDate(note.date);
        noteDb.setTitle(note.title);
        noteDb.setContent(note.content);
        realm.commitTransaction();
    }
}
