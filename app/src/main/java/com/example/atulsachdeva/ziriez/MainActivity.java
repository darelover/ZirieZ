package com.example.atulsachdeva.ziriez;

import android.app.Dialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.atulsachdeva.ziriez.Adapters.ShowFirebaseRecyclerAdapter;
import com.example.atulsachdeva.ziriez.Models.Show;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity {

    RecyclerView rvList;
    FloatingActionButton fabAdd;

    ShowFirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvList = findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this));

        fabAdd = findViewById(R.id.fabAdd);

//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);   // what changes to be implemented in project when using this ?
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ShowList");

        Query showListQuery = reference.limitToFirst(10);

        FirebaseRecyclerOptions<Show> options =
                new FirebaseRecyclerOptions.Builder<Show>()
                        .setQuery(showListQuery, Show.class)
                        .build();

        adapter = new ShowFirebaseRecyclerAdapter(options, this);
        rvList.setAdapter(adapter);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.dialog_add);

                dialog.findViewById(R.id.btnAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Show newShow = new Show(
                                ((EditText) dialog.findViewById(R.id.etShowName))
                                        .getText().toString(),
                                ((EditText) dialog.findViewById(R.id.etShowEpisode))
                                        .getText().toString()
                        );

                        reference.push().setValue(newShow);

                        dialog.dismiss();
                    }
                });
                dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (adapter != null)
            adapter.stopListening();
    }
}
