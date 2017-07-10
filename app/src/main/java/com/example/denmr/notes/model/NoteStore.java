package com.example.denmr.notes.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by denmr on 25.06.2017.
 */

public class NoteStore {
    private static NoteStore instance;
    private Map<UUID, Note> notes;

    private NoteStore(Context context) {
        notes = new LinkedHashMap<>();
        for (int i = 0; i < 100; i++) {
            Note newNote = new Note();
            newNote.setTitle("Note #" + i);
            newNote.setSolved(i % 2 == 0); // Every other one
            newNote.setImportant(i % 2 == 0); // Every other one
            notes.put(newNote.getId(), newNote);
        }

    }

    public static NoteStore get(Context context) {
        if (instance == null) {
            instance = new NoteStore(context);
        }
        return instance;
    }

    public Map<UUID, Note> getNotes(){
        return notes;
    }

    public Note getNote(UUID id){
        return  notes.get(id);
    }
}
