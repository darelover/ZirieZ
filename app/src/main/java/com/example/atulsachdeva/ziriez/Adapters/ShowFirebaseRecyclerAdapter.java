package com.example.atulsachdeva.ziriez.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.atulsachdeva.ziriez.Models.Show;
import com.example.atulsachdeva.ziriez.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by AtulSachdeva on 30/11/17.
 */

public class ShowFirebaseRecyclerAdapter extends FirebaseRecyclerAdapter<Show, ShowFirebaseRecyclerAdapter.ShowViewHolder> {

    private Context context;

    public ShowFirebaseRecyclerAdapter(FirebaseRecyclerOptions<Show> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(ShowViewHolder holder, final int position, final Show show) {
        holder.bindView(show);
    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ShowViewHolder(li.inflate(R.layout.list_item, parent, false));
    }

    public void edit(int position, final String tabHeader) {

        final String name = getItem(position).getName();
        String episode = getItem(position).getEpisode();

        final Dialog dialogEdit = new Dialog(context);
        dialogEdit.setContentView(R.layout.dialog_edit);

        ((EditText) dialogEdit.findViewById(R.id.etShowName))
                .setText(name);
        ((EditText) dialogEdit.findViewById(R.id.etShowEpisode))
                .setText(episode);

        ((Button) dialogEdit.findViewById(R.id.btnSave))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Show updatedShow = new Show(
                                ((EditText) dialogEdit.findViewById(R.id.etShowName))
                                        .getText().toString(),
                                ((EditText) dialogEdit.findViewById(R.id.etShowEpisode))
                                        .getText().toString()
                        );

                        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Watching").child(tabHeader);

                        reference
                                .orderByChild("name")
                                .equalTo(name)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        String key = null;
                                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                                            key = childSnapshot.getKey();
                                        }
                                        HashMap<String, Object> map = updatedShow.toMap();

                                        HashMap<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put(key, map);

                                        reference.updateChildren(childUpdates);
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        notifyDataSetChanged();
                        dialogEdit.dismiss();
                    }
                });
        dialogEdit.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogEdit.dismiss();
            }
        });
        notifyDataSetChanged();
        dialogEdit.show();
    }

    public void delete(int position, String tabHeader) {
        String name = getItem(position).getName();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Watching").child(tabHeader);

        reference
                .orderByChild("name")
                .equalTo(name)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String key = null;
                        for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            key = childSnapshot.getKey();
                        }
//                            HashMap<String, Object> map = updatedShow.toMap();

                        // removeValue() -- try to use this
                        HashMap<String, Object> childUpdates = new HashMap<>();
                        childUpdates.put(key, null);

                        reference.updateChildren(childUpdates);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(context, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder {

        TextView tvShowNname, tvShowEpisode;

        public ShowViewHolder(View itemView) {
            super(itemView);

            tvShowNname = itemView.findViewById(R.id.tvShowName);
            tvShowEpisode = itemView.findViewById(R.id.tvShowEpisode);
        }

        void bindView(Show thisShow) {
            tvShowNname.setText(thisShow.getName());
            tvShowEpisode.setText(thisShow.getEpisode());
        }
    }
}
