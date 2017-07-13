package com.example.denmr.notes.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.denmr.notes.NotePagerActivity;
import com.example.denmr.notes.R;
import com.example.denmr.notes.model.Note;
import com.example.denmr.notes.model.NoteStore;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by denmr on 25.06.2017.
 */

public class NoteListFragment extends Fragment {
    private RecyclerView mNoteRecyclerView;
    private NoteAdapter mAdapter;
    private int position;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        mNoteRecyclerView = (RecyclerView) view
                .findViewById(R.id.crime_recycler_view);
        mNoteRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    /**
     * Override onResume to update note fragment list after any changes in notes
     */
    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI() {
        NoteStore noteStore = NoteStore.get(getActivity());
        List<Note> notes = new ArrayList<>(noteStore.getNotes().values());

        if (mAdapter == null) {
            mAdapter = new NoteAdapter(notes);
            mNoteRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyItemChanged(position);
        }
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mImageView;
        private Note mNote;

        public NoteHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.note_list_item, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.note_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.note_date);
            mImageView = (ImageView) itemView.findViewById(R.id.note_important_image);
        }

        public TextView getmTitleTextView() {
            return mTitleTextView;
        }

        public void setmTitleTextView(TextView mTitleTextView) {
            this.mTitleTextView = mTitleTextView;
        }

        public TextView getmDateTextView() {
            return mDateTextView;
        }

        public void setmDateTextView(TextView mDateTextView) {
            this.mDateTextView = mDateTextView;
        }

        public Note getmNote() {
            return mNote;
        }

        public void setmNote(Note mNote) {
            this.mNote = mNote;
        }

        public ImageView getmImageView() {
            return mImageView;
        }

        public void setmImageView(ImageView mImageView) {
            this.mImageView = mImageView;
        }

        @Override
        public void onClick(View v) {
            Intent intent = NotePagerActivity.newIntent(getActivity(), mNote.getId());

            position = getLayoutPosition();
            startActivityForResult(intent, position);
        }
    }

    private class NoteImportantHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView mTitleTextView;
        private TextView mImportantTextView;
        private ImageView mImageView;
        private Note mNote;

        public NoteImportantHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.note_list_item, parent, false));
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView) itemView.findViewById(R.id.note_title);
            mImageView = (ImageView) itemView.findViewById(R.id.note_important_image);
        }

        public TextView getmTitleTextView() {
            return mTitleTextView;
        }

        public void setmTitleTextView(TextView mTitleTextView) {
            this.mTitleTextView = mTitleTextView;
        }

        public TextView getmImportantTextView() {
            return mImportantTextView;
        }

        public void setmImportantTextView(TextView mImportantTextView) {
            this.mImportantTextView = mImportantTextView;
        }

        public Note getmNote() {
            return mNote;
        }

        public void setmNote(Note mNote) {
            this.mNote = mNote;
        }

        public ImageView getmImageView() {
            return mImageView;
        }

        public void setmImageView(ImageView mImageView) {
            this.mImageView = mImageView;
        }

        @Override
        public void onClick(View v) {
            Intent intent = NotePagerActivity.newIntent(getActivity(), mNote.getId());

            position = getLayoutPosition();
            startActivityForResult(intent, position);
        }
    }

    private class NoteAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Note> mNotes;

        public NoteAdapter(List<Note> notes) {
            mNotes = notes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());

            RecyclerView.ViewHolder viewHolder;
            switch (viewType) {
                case 1:
                    viewHolder = new NoteImportantHolder(layoutInflater, parent);
                    break;
                default:
                    viewHolder = new NoteHolder(layoutInflater, parent);
            }

            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            Note note = mNotes.get(position);
            switch (holder.getItemViewType()){
                case 0:
                    NoteHolder vh1 = (NoteHolder) holder;
                    vh1.setmNote(note);
                    vh1.getmTitleTextView().setText(note.getTitle());
                    java.text.DateFormat df = DateFormat.getDateFormat(getContext());
                    vh1.getmDateTextView().setText(DateFormat.format("E yyyy-MM-dd,  HH:mm", note.getDate()));
                    vh1.getmImageView().setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    NoteImportantHolder vh2 = (NoteImportantHolder) holder;
                    vh2.setmNote(note);
                    vh2.getmTitleTextView().setText(note.getTitle());
                    vh2.getmImageView().setVisibility(View.VISIBLE);
                    break;
            }
        }


        @Override
        public int getItemViewType(int position) {
            // determine note type and return 1 for important and 0 for other notes
            Note note = mNotes.get(position);
            boolean noteType = note.isImportant();
            return noteType ? 1 : 0;
        }

        @Override
        public int getItemCount() {
            return mNotes.size();
        }
    }
}
