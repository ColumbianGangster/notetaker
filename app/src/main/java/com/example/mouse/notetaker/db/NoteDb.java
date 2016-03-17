package com.example.mouse.notetaker.db;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by TAE_user2 on 16/03/2016.
 */
public class NoteDb extends RealmObject {
    @Required
    private String id;
    @Required
    private String title;
    @Required
    private String content;
    @Required
    private Long date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }
}
