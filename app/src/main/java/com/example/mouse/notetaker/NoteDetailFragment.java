package com.example.mouse.notetaker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mouse.notetaker.db.MyDb;
import com.example.mouse.notetaker.pojo.Note;

/**
 * A fragment representing a single Note detail screen.
 * This fragment is either contained in a {@link NoteListActivity}
 * in two-pane mode (on tablets) or a {@link NoteDetailActivity}
 * on handsets.
 */
public class NoteDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    private Note mItem;
    private Boolean newNote = false;

    public NoteDetailFragment() {
    }

    // Container Activity must implement this interface
    public interface OnNoteAdded {
        public void addNote(Note note);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null){
            Log.i("Test", "onCreate: Bundle is not null ");
            if (!getArguments().isEmpty()) {
                Log.i("Test", "onCreate: ARG_ITEM_ID exists");
                mItem = getArguments().getParcelable("ExistingNote");
                Activity activity = this.getActivity();
                CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(mItem.title);
                }
            }
        } else {
            Log.i("Test", "onCreate: Bundle is null ");
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("New Note");
                newNote = true;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.note_detail, container, false);

        Log.i("Test", "onCreateView: ");

        TextView title = ((TextView) rootView.findViewById(R.id.note_title));
        TextView content = ((TextView) rootView.findViewById(R.id.note_content));

        // if the Note already exists
        if (mItem != null) {
            title.setText(mItem.title);
            content.setText(mItem.content);
            Long date = mItem.date;
            NoteDateLocaliser note = new NoteDateLocaliser();
            String localizedCalendarDate = note.getLocalDate(date);
            ((TextView) rootView.findViewById(R.id.note_modified)).setText(localizedCalendarDate);
        }
        // TextWatchers are added no matter what
        title.addTextChangedListener(new GenericTextWatcher(title));
        content.addTextChangedListener(new GenericTextWatcher(content));

        if(newNote){
            // Delete button should only appear if there is a note to delete
            rootView.findViewById(R.id.note_button_delete).setVisibility(View.INVISIBLE);
        }
        // Regardless of whether the Note already exists...
        ((Button) rootView.findViewById(R.id.note_button_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(rootView);
            }
        });
        ((Button) rootView.findViewById(R.id.note_button_delete)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });
        // Save button should initially appear invisible regardless of whether this is a new note or not
        rootView.findViewById(R.id.note_button_save).setVisibility(View.INVISIBLE);

        return rootView;
    }

    private class GenericTextWatcher implements TextWatcher {

        private View view;
        private GenericTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        public void afterTextChanged(Editable editable) {
            String text = editable.toString();
            switch(view.getId()){
                case R.id.note_title:
                    getActivity().findViewById(R.id.note_button_save).setVisibility(View.VISIBLE);
                    break;
                case R.id.note_content:
                    getActivity().findViewById(R.id.note_button_save).setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    public void addNote(){
        TextView textViewContent = (TextView) getActivity().findViewById(R.id.note_content);
        mItem.content = textViewContent.getText().toString();
        mItem.date = System.currentTimeMillis();
        TextView textViewModified = (TextView) getActivity().findViewById(R.id.note_modified);
        Long currentTime = System.currentTimeMillis();
        NoteDateLocaliser note = new NoteDateLocaliser();
        String localizedCalendarDate = note.getLocalDate(currentTime);
        textViewModified.setText(localizedCalendarDate);
        // Update the title on the Appbar
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle(mItem.title);
        }

        if (newNote) {
            OnNoteAdded hostActivity = (OnNoteAdded) getActivity();
//            hostActivity.addNote(mItem);
            // Delete button should only appear if there is a note to delete
        } else {
//                    notedb.setId(mItem.id);
        }
        MyDb myDb = new MyDb(getContext());
        myDb.addNote(mItem);

//                notedb.setTitle(textViewTitle.getText().toString());
//                notedb.setContent(textViewContent.getText().toString());
//                notedb.setDate(currentTime);
//                myDb.persist(notedb);
    }
    public void save(View rootView){
        TextView textViewTitle = (TextView) getActivity().findViewById(R.id.note_title);
        TextView textViewContent = (TextView) getActivity().findViewById(R.id.note_content);
        // if either are empty... inform user this cannot be!
        if(!textViewTitle.getText().toString().isEmpty() &&
                !textViewContent.getText().toString().isEmpty()){
            MyDb myDb = new MyDb(getActivity());
            if (newNote) {
                mItem = new Note("", "", "", "");
            }
            mItem.title = textViewTitle.getText().toString();
            if(myDb.checkIfExists(mItem.title) && newNote){
                Snackbar snackbar = Snackbar
                        .make(rootView, R.string.note_new_title_exists, Snackbar.LENGTH_LONG);
                snackbar.show();
            } else {
                addNote();
                if(newNote){
                    rootView.findViewById(R.id.note_button_delete).setVisibility(View.VISIBLE);
                    newNote = false;
                }

            }
        } else { // either field is empty
            Snackbar snackbar = Snackbar
                    .make(rootView, R.string.note_new_field_empty, Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }
    public void delete(){
        new AlertDialog.Builder(getActivity())
                .setTitle(R.string.alert_delete_title)
                .setMessage(R.string.alert_delete_message)
                .setCancelable(true)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        Log.i("Test", "onClick: Chose to delete ");
//                                Notes.ITEMS.remove(mItem.id);
//                                Notes.ITEM_MAP.remove(mItem.id);
                        Log.i("Test", "onClick ItemId: "+mItem.id);
                        MyDb myDb = new MyDb(getContext());
                        myDb.removeNote(mItem);
                        Intent intent = new Intent(getActivity(), NoteListActivity.class);
                        intent.putExtra("notifyItemRemoved", mItem.title);
                        getActivity().navigateUpTo(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Log.i("Test", "onClick: Chose not to delete");
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
