package com.example.mouse.notetaker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mouse.notetaker.db.MyDb;
import com.example.mouse.notetaker.pojo.Note;

import net.danlew.android.joda.JodaTimeAndroid;

import java.util.List;


public class NoteListActivity extends AppCompatActivity {

    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        JodaTimeAndroid.init(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.note_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
        Log.i("Test", "onCreate: NoteListActivity");
        if(getIntent().hasExtra("notifyItemRemoved")){
            Log.i("Test", "onCreate: notifyItemRemoved");
            SimpleItemRecyclerViewAdapter s = (SimpleItemRecyclerViewAdapter)((RecyclerView) recyclerView).getAdapter();
            s.delete(getIntent().getStringExtra("notifyItemRemoved"));
        }
        if(getIntent().hasExtra("NewNote")){
            Log.i("Test", "onCreate: NewNote exists");
            Note newNote = getIntent().getExtras().getParcelable("NewNote");
            SimpleItemRecyclerViewAdapter s = (SimpleItemRecyclerViewAdapter)((RecyclerView) recyclerView).getAdapter();
            newNote.id = Integer.toString(s.getItemCount());
            s.insert(s.getItemCount(),newNote);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add a Note: Goto empty detail fragment", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if (mTwoPane) {
                    NoteDetailFragment fragment = new NoteDetailFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.note_detail_container, fragment)
                            .commit();
                } else {
                    Intent intent = new Intent(getApplicationContext(), NoteDetailActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    getApplicationContext().startActivity(intent);
//                    getApplicationContext().startActivity(intent);
                }
            }
        });

        if (findViewById(R.id.note_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        MyDb myDb = new MyDb(this);
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(myDb.getAll()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private List<Note> mValues;

        public SimpleItemRecyclerViewAdapter(List<Note> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.note_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mTitleView.setText(mValues.get(position).title);
//            holder.mContentView.setText(mValues.get(position).content);
            String s = mValues.get(position).content;
            String contentToShow = s.substring(0, Math.min(s.length(), 30));
            holder.mContentView.setText(contentToShow+"...");
            // Format date to a local format for the user - regardless of where they live
            Long date = mValues.get(position).date;
            NoteDateLocaliser note = new NoteDateLocaliser();
            String localizedCalendarDate = note.getLocalDate(date);
            holder.mModifiedDateView.setText(localizedCalendarDate);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("Test", "onClick: "+ "on click");
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
//                        arguments.putString(NoteDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        arguments.putParcelable("ExistingNote", holder.mItem);
                        NoteDetailFragment fragment = new NoteDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.note_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, NoteDetailActivity.class);
                        intent.putExtra("ExistingNote", holder.mItem);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
        private int find(String stringToFind){
            int ret = -1;
            for(int i = 0; i<mValues.size(); ++i){
                if(mValues.get(i).title.contentEquals(stringToFind)){
                    ret = i;
                }
            }
            return ret;
        }
        public void delete(String title){
//            int position = find(title);
//            mValues.remove(position);
//            notifyItemRemoved(position);
        }

        public void insert(int position, Note note){
            mValues.add(position, note);
            notifyItemInserted(position);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mTitleView;
            public final TextView mContentView;
            public final TextView mModifiedDateView;
            public Note mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTitleView = (TextView) view.findViewById(R.id.title);
                mContentView = (TextView) view.findViewById(R.id.content);
                mModifiedDateView = (TextView) view.findViewById(R.id.modified);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
