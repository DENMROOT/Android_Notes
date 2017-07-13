package com.example.denmr.notes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.denmr.notes.fragments.NoteFragment;
import com.example.denmr.notes.model.Note;
import com.example.denmr.notes.model.NoteStore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Denys_Makarov on 7/13/2017.
 */

public class NotePagerActivity extends AppCompatActivity {
    private static final String EXTRA_NOTE_ID =
            "com.example.denmr.notes.note_id";

    private ViewPager viewPager;
    private Button firstButton;
    private Button lastButton;
    private List<Note> notes;

    public static Intent newIntent(Context packageContext, UUID noteId) {
        Intent intent = new Intent(packageContext, NotePagerActivity.class);
        intent.putExtra(EXTRA_NOTE_ID, noteId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_pager);

        final UUID noteId = (UUID) getIntent()
                .getSerializableExtra(EXTRA_NOTE_ID);

        viewPager = (ViewPager) findViewById(R.id.crime_view_pager);

        this.notes = new ArrayList<>(NoteStore.get(this).getNotes().values());

        FragmentManager fragmentManager = getSupportFragmentManager();
        viewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            @Override
            public Fragment getItem(int position) {
                Note note = notes.get(position);
                return NoteFragment.newInstance(note.getId());
            }

            @Override
            public int getCount() {
                return notes.size();
            }
        });

        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getId().equals(noteId)) {
                viewPager.setCurrentItem(i);
                break;
            }
        }

        firstButton = (Button) findViewById(R.id.first_button);

        if (noteId == notes.get(0).getId()) {
            firstButton.setEnabled(false);
        } else if (noteId == notes.get(notes.size()).getId()) {
            lastButton.setEnabled(false);
        }

        firstButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID firstNoteId = notes.get(0).getId();

                for (int i = 0; i < notes.size(); i++) {
                    if (notes.get(i).getId().equals(firstNoteId)) {
                        viewPager.setCurrentItem(i);
                        break;
                    }
                }
            }
        });

        lastButton = (Button) findViewById(R.id.last_button);

        lastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UUID firstNoteId = notes.get(notes.size()).getId();

                for (int i = 0; i < notes.size(); i++) {
                    if (notes.get(i).getId().equals(firstNoteId)) {
                        viewPager.setCurrentItem(i);
                        break;
                    }
                }
            }
        });
    }
}
