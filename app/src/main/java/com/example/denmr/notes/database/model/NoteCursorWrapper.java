package com.example.denmr.notes.database.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.denmr.notes.model.Note;

import java.util.Date;
import java.util.UUID;

import static com.example.denmr.notes.database.model.NoteDbSchema.*;

/**
 * Created by Denys_Makarov on 7/21/2017.
 */

public class NoteCursorWrapper extends CursorWrapper {
    public NoteCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Note getNote() {
        String uuidString = getString(getColumnIndex(NoteTable.Cols.UUID));
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        long date = getLong(getColumnIndex(NoteTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(NoteTable.Cols.SOLVED));
        String contact = getString(getColumnIndex(NoteTable.Cols.CONTACT));

        Note note = new Note(UUID.fromString(uuidString));
        note.setTitle(title);
        note.setDate(new Date(date));
        note.setSolved(isSolved != 0);
        note.setContact(contact);

        return note;
    }
}