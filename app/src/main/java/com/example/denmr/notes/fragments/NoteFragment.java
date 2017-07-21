package com.example.denmr.notes.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.example.denmr.notes.NotePagerActivity;
import com.example.denmr.notes.R;
import com.example.denmr.notes.model.Note;
import com.example.denmr.notes.model.NoteStore;

import java.util.Date;
import java.util.UUID;

import static android.widget.CompoundButton.*;

/**
 * Created by denmr on 25.06.2017.
 */

public class NoteFragment extends Fragment {
    public static final String NOTE_ID = "note_id";
    private static final String DIALOG_DATE_TAG = "note_date_dialog_tag";

    //Target fragment code
    private static final int REQUEST_DATE_CODE = 0;
    private static final int REQUEST_CONTACT = 1;

    private Note note;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mSolvedCheckBox;
    private CheckBox mImportantCheckBox;
    private Button mContactButton;
    private Button mSendReportButton;

    public static NoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(NOTE_ID, noteId);

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID noteId = (UUID) getArguments().getSerializable(NOTE_ID);
        note = NoteStore.get(getContext()).getNote(noteId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_note, container, false);

        mTitleField = (EditText) v.findViewById(R.id.note_title);
        mTitleField.setText(note.getTitle());

        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                note.setTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mDateButton = (Button) v.findViewById(R.id.note_date);
        updateDate();
        mDateButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(note.getDate());
                dialog.setTargetFragment(NoteFragment.this, REQUEST_DATE_CODE);
                dialog.show(fragmentManager, DIALOG_DATE_TAG);
            }
        });

        mSolvedCheckBox = (CheckBox) v.findViewById(R.id.note_solved);
        mSolvedCheckBox.setChecked(note.isSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                note.setSolved(isChecked);
            }
        });

        mImportantCheckBox = (CheckBox) v.findViewById(R.id.note_important);
        mImportantCheckBox.setChecked(note.isImportant());
        mImportantCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isImportant) {
                note.setImportant(isImportant);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK,
                ContactsContract.Contacts.CONTENT_URI);

        // check if are able to access contact data, if no - disable mContactButton
        PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact,
                PackageManager.MATCH_DEFAULT_ONLY) == null) {
            mContactButton.setEnabled(false);
        }

        mContactButton = (Button) v.findViewById(R.id.add_contact);
        mContactButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUEST_CONTACT);
            }
        });

        mSendReportButton = (Button) v.findViewById(R.id.send_report);
        mSendReportButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = ShareCompat.IntentBuilder
                        .from(getActivity())
                        .setType("text/plain")
                        .getIntent()
                        .putExtra(Intent.EXTRA_TEXT, getCrimeReport())
                        .putExtra(Intent.EXTRA_SUBJECT, getString(R.string.note_report_subject));

                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        if (note.getContact() != null) {
            mContactButton.setText(note.getContact());
        }

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_DATE_CODE) {
            Date date = (Date) data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            note.setDate(date);
            updateDate();
        } else if (requestCode == REQUEST_CONTACT && data != null) {
            Uri contactUri = data.getData();
            // Specify which fields you want your query to return
            // values for
            String[] queryFields = new String[]{
                    ContactsContract.Contacts.DISPLAY_NAME
            };
            // Perform your query - the contactUri is like a "where"
            // clause here

            try (Cursor c = getActivity().getContentResolver()
                    .query(contactUri, queryFields, null, null, null)) {
                // Double-check that you actually got results
                if (c == null || c.getCount() == 0) {
                    return;
                }

                // Pull out the first column of the first row of data -
                // that is your suspect's name
                c.moveToFirst();
                String contact = c.getString(0);
                note.setContact(contact);
                mContactButton.setText(contact);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_note:
                Note note = new Note();
                NoteStore.get(getActivity()).addNote(note);
                Intent intent = NotePagerActivity
                        .newIntent(getActivity(), note.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        NoteStore.get(getActivity())
                .updateNote(note);
    }

    private void updateDate() {
        mDateButton.setText(note.getDate().toString());
    }

    private String getCrimeReport() {
        String solvedString = null;
        if (note.isSolved()) {
            solvedString = getString(R.string.note_report_solved);
        } else {
            solvedString = getString(R.string.note_report_unsolved);
        }

        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat,
                note.getDate()).toString();

        String suspect = note.getContact();
        if (suspect == null) {
            suspect = getString(R.string.note_report_no_contact);
        } else {
            suspect = getString(R.string.note_report_contact, suspect);
        }

        String report = getString(R.string.note_report,
                note.getTitle(), dateString, solvedString, suspect);

        return report;
    }
}
