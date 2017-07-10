package com.example.denmr.notes;

import android.support.v4.app.Fragment;

import com.example.denmr.notes.fragments.NoteListFragment;

public class NoteListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new NoteListFragment();
    }
}
