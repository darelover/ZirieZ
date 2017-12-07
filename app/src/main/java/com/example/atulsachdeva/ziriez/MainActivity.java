package com.example.atulsachdeva.ziriez;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

//        for pulling all the data and limit it to 20 key- value pairs, following line is replaced by line following it
//        Query showListQuery = reference.limitToFirst(20);
        Query showListQuery = reference;

        FirebaseRecyclerOptions<Show> options =
                new FirebaseRecyclerOptions.Builder<Show>()
                        .setQuery(showListQuery, Show.class)
                        .build();

        adapter = new ShowFirebaseRecyclerAdapter(options, this);
        rvList.setAdapter(adapter);

        // for implementing swipe events
        ItemTouchHelper.SimpleCallback helper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            public int convertDpToPx(int dp) {
                return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
            }

            public float ALPHA_FULL = (float) 1.0;

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();

                // delete
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.delete(position);
                }
                // edit
                else if (direction == ItemTouchHelper.RIGHT) {
                    adapter.edit(position);
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

                    View itemView = viewHolder.itemView;

                    Paint paint = new Paint();
                    Bitmap icon;

                    // right swipe -- edit
                    if (dX > 0) {
                        paint.setARGB(255, 0, 255, 0);
                        c.drawRect(
                                (float) itemView.getLeft(),
                                (float) itemView.getTop(),
                                dX,
                                (float) itemView.getBottom(),
                                paint
                        );
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit_white_24dp);
                        c.drawBitmap(
                                icon,
                                (float) itemView.getLeft() + convertDpToPx(16),
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                                paint
                        );
                    }
                    // left swipe -- delete
                    else {
                        paint.setARGB(255, 255, 0, 0);
                        c.drawRect(
                                (float) itemView.getRight() + dX,
                                (float) itemView.getTop(),
                                (float) itemView.getRight(),
                                (float) itemView.getBottom(),
                                paint
                        );
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete_white_24dp);
                        c.drawBitmap(
                                icon,
                                (float) itemView.getRight() - convertDpToPx(16) - icon.getWidth(),
                                (float) itemView.getTop() + ((float) itemView.getBottom() - (float) itemView.getTop() - icon.getHeight()) / 2,
                                paint
                        );
                    }

                    // Fade out the view when it is swiped out of the parent
                    final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
                    viewHolder.itemView.setAlpha(alpha);
                    viewHolder.itemView.setTranslationX(dX);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        new ItemTouchHelper(helper).attachToRecyclerView(rvList);

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
