package com.example.denmr.notes.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.denmr.notes.database.NoteBaseHelper;
import com.example.denmr.notes.database.model.NoteCursorWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.denmr.notes.database.model.NoteDbSchema.*;

/**
 * Created by denmr on 25.06.2017.
 */

public class NoteStore {
    private static NoteStore instance;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    private NoteStore(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new NoteBaseHelper(mContext)
                .getWritableDatabase();
    }

    public static NoteStore get(Context context) {
        if (instance == null) {
            instance = new NoteStore(context);
        }
        return instance;
    }

    public Map<UUID, Note> getNotes(){
        Map<UUID, Note> notes = new HashMap<>();

        try (NoteCursorWrapper cursor = queryNotes(null, null)) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Note note = cursor.getNote();
                notes.put(note.getId(), note);
                cursor.moveToNext();
            }
        }

        return notes;
    }

    public Note getNote(UUID id){
        try (NoteCursorWrapper cursor = queryNotes(
                NoteTable.Cols.UUID + " = ?",
                new String[]{id.toString()})
        ) {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getNote();
        }
    }

    public void addNote(Note note) {
        ContentValues values = getContentValues(note);

        mDatabase.insert(NoteTable.NAME, null, values);
    }

    public void updateNote(Note note) {
        String uuidString = note.getId().toString();
        ContentValues values = getContentValues(note);

        mDatabase.update(NoteTable.NAME, values,
                NoteTable.Cols.UUID + " = ?",
                new String[] { uuidString });
    }

    //TODO implement delete in controller
    public void deleteNote(Note note){
        String uuidString = note.getId().toString();
        mDatabase.delete(NoteTable.NAME, NoteTable.Cols.UUID + " = ?", new String[] {uuidString});
    }

    private static ContentValues getContentValues(Note note) {
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.UUID, note.getId().toString());
        values.put(NoteTable.Cols.TITLE, note.getTitle());
        values.put(NoteTable.Cols.DATE, note.getDate().getTime());
        values.put(NoteTable.Cols.SOLVED, note.isSolved() ? 1 : 0);
        values.put(NoteTable.Cols.CONTACT, note.getContact());

        return values;
    }

    private NoteCursorWrapper queryNotes(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                NoteTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new NoteCursorWrapper(cursor);
    }
}
