package com.example.denmr.notes.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by denmr on 25.06.2017.
 */

public class Note {
    private UUID id;
    private String title;
    private Date date;
    private boolean solved;
    private boolean important;
    private String contact;

    public Note() {
        this(UUID.randomUUID());
    }

    public Note(UUID id) {
        this.id = id;
        this.date = new Date();
    }

    public UUID getId() {
        return id;
    }

    public boolean isSolved() {
        return solved;
    }

    public void setSolved(boolean solved) {
        this.solved = solved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isImportant() {
        return important;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
