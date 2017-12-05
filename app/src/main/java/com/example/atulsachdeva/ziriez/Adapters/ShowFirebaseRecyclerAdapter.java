package com.example.atulsachdeva.ziriez.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

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
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
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

    public class ShowViewHolder extends RecyclerView.ViewHolder {

        LinearLayout llShowItem;
        TextView tvShowNname, tvShowEpisode;

        Button btnPlus = new Button(context);
        Button btnMinus = new Button(context);
        Button btnEdit = new Button(context);
        Button btnDelete = new Button(context);

        public ShowViewHolder(View itemView) {
            super(itemView);

            llShowItem = itemView.findViewById(R.id.llShowItem);
            tvShowNname = itemView.findViewById(R.id.tvShowName);
            tvShowEpisode = itemView.findViewById(R.id.tvShowEpisode);

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    final Dialog dialogOptionsListItem = new Dialog(context);
                    dialogOptionsListItem.setContentView(R.layout.dialog_options_list_item);

                    dialogOptionsListItem.findViewById(R.id.tvEdit)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogOptionsListItem.dismiss();

                                    final Dialog dialogEdit = new Dialog(context);
                                    dialogEdit.setContentView(R.layout.dialog_edit);

                                    ((EditText) dialogEdit.findViewById(R.id.etShowName))
                                            .setText(tvShowNname.getText());
                                    ((EditText) dialogEdit.findViewById(R.id.etShowEpisode))
                                            .setText(tvShowEpisode.getText());

                                    ((Button) dialogEdit.findViewById(R.id.btnSave))
                                            .setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    edit(tvShowNname.getText().toString(),
                                                            new Show(
                                                            ((EditText) dialogEdit.findViewById(R.id.etShowName))
                                                                    .getText().toString(),
                                                            ((EditText) dialogEdit.findViewById(R.id.etShowEpisode))
                                                                    .getText().toString()
                                                    ));
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
                                    dialogEdit.show();

                                }
                            });

                    dialogOptionsListItem.findViewById(R.id.tvDelete)
                            .setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    dialogOptionsListItem.dismiss();

                                    delete(tvShowNname.getText().toString());
                                }
                            });

                    dialogOptionsListItem.show();


                    /*
                    Log.d("HI", "onLongClick: " + pxToDp(tvShowNname.getWidth()));
                    tvShowNname.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//                    tvShowEpisode.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT));

                    btnPlus.setLayoutParams(new LinearLayout.LayoutParams(dpToPx(50), ViewGroup.LayoutParams.WRAP_CONTENT));
                    btnMinus.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    btnEdit.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    btnDelete.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    btnPlus.setText("+");
                    btnMinus.setText("-");
                    btnEdit.setText("EDIT");
                    btnDelete.setText("DELETE");

                    llShowItem.addView(btnPlus);
                    llShowItem.addView(btnMinus);
                    llShowItem.addView(btnEdit);
                    llShowItem.addView(btnDelete);

                    Log.d("HI", "onLongClick: " + pxToDp(tvShowNname.getWidth()));
//                    Log.d("HI", "onLongClick: ");
                    */

                    return false;
                }
            });
        }

        void bindView(Show thisShow) {
            tvShowNname.setText(thisShow.getName());
            tvShowEpisode.setText(thisShow.getEpisode());
        }

        public int pxToDp(int px) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return dp;
        }

        public int dpToPx(int dp) {
            DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
            int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
            return px;
        }

        void edit(String oldName, final Show updatedShow) {
            FirebaseDatabase
                    .getInstance()
                    .getReference("ShowList")
                    .orderByChild("name")
                    .equalTo(oldName)
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

                            FirebaseDatabase.getInstance().getReference("ShowList").updateChildren(childUpdates);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
        void delete(String name) {
            FirebaseDatabase
                    .getInstance()
                    .getReference("ShowList")
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

                            HashMap<String, Object> childUpdates = new HashMap<>();
                            childUpdates.put(key, null);

                            FirebaseDatabase.getInstance().getReference("ShowList").updateChildren(childUpdates);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }

    }
}
