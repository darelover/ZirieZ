package com.example.atulsachdeva.ziriez.Fragments;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.atulsachdeva.ziriez.Adapters.ShowFirebaseRecyclerAdapter;
import com.example.atulsachdeva.ziriez.Models.Show;
import com.example.atulsachdeva.ziriez.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class MainFragment extends Fragment {

    ShowFirebaseRecyclerAdapter adapter;
    String tabHeader;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        tabHeader = getArguments().getString("TabHeader");

        RecyclerView rvList = rootView.findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(getContext()));


//        FirebaseDatabase.getInstance().setPersistenceEnabled(true);   // what changes to be implemented in project when using this ?
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Watching").child(tabHeader);

//        for pulling all the data and limit it to 20 key- value pairs, following line is replaced by line following it
//        Query showListQuery = reference.limitToFirst(20);
        Query showListQuery = reference;

        FirebaseRecyclerOptions<Show> options =
                new FirebaseRecyclerOptions.Builder<Show>()
                        .setQuery(showListQuery, Show.class)
                        .build();

        adapter = new ShowFirebaseRecyclerAdapter(options, getContext());
        rvList.setAdapter(adapter);

        // for implementing swipe events
        ItemTouchHelper.SimpleCallback helper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            public float ALPHA_FULL = (float) 1.0;

            public int convertDpToPx(int dp) {
                return Math.round(dp * (getResources().getDisplayMetrics().xdpi / DisplayMetrics.DENSITY_DEFAULT));
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getAdapterPosition();

                // delete
                if (direction == ItemTouchHelper.LEFT) {
                    adapter.delete(position, tabHeader);
                }
                // edit
                else if (direction == ItemTouchHelper.RIGHT) {
                    adapter.edit(position, tabHeader);
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

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter != null)
            adapter.startListening();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (adapter != null)
            adapter.stopListening();
    }
}
