package com.example.denmr.notes;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.example.denmr.notes.fragments.NoteFragment;

import java.util.UUID;

public class NoteActivity extends SingleFragmentActivity {
    private static final String EXTRA_NOTE_ID =
            "com.example.denmr.notes.note_id";

    @Override
    protected Fragment createFragment() {
        UUID noteId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_NOTE_ID);
        return NoteFragment.newInstance(noteId);
    }

    public static Intent newIntent(Context packageContext, UUID crimeId) {
        Intent intent = new Intent(packageContext, NoteActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, crimeId);
        return intent;
    }
}
