package com.example.atulsachdeva.ziriez.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.atulsachdeva.ziriez.Models.Show;
import com.example.atulsachdeva.ziriez.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

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

        holder.tvShowNname.setText(show.getName());
        holder.tvShowEpisode.setText("Episode " + show.getEpisode());

//        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final Dialog dialog = new Dialog(context);
//                dialog.setContentView(R.layout.dialog_edit);
//
//                ((EditText) dialog.findViewById(R.id.etShowName))
//                        .setText(show.getName());
//                ((EditText) dialog.findViewById(R.id.etShowEpisode))
//                        .setText(show.getEpisode());
//
//                ((Button) dialog.findViewById(R.id.btnSave))
//                        .setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                shows.set(
//                                        position,
//                                        new Show(
//                                                ((EditText) dialog.findViewById(R.id.etShowName))
//                                                        .getText().toString(),
//                                                ((EditText) dialog.findViewById(R.id.etShowEpisode))
//                                                        .getText().toString()
//                                        ));
//                                notifyDataSetChanged();
//                                dialog.dismiss();
//                            }
//                        });
//                dialog.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                dialog.show();
//
//            }
//        });

//        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // perform Deletion
//                Log.d("DEL", "onClick: ");
//
//                FirebaseDatabase.getInstance().getReference("ShowList").child(show.getName()).removeValue();
//            }
//        });
    }

    @Override
    public ShowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return new ShowViewHolder(li.inflate(R.layout.list_item, parent, false));
    }

    public class ShowViewHolder extends RecyclerView.ViewHolder {

        TextView tvShowNname, tvShowEpisode;
        Button btnDelete, btnEdit;

        public ShowViewHolder(View itemView) {
            super(itemView);
            tvShowNname = itemView.findViewById(R.id.tvShowName);
            tvShowEpisode = itemView.findViewById(R.id.tvShowEpisode);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);

        }
    }
}
