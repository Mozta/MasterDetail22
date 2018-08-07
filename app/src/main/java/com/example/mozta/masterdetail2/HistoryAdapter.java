package com.example.mozta.masterdetail2;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Created by mozta on 23/05/18.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>{
    private ValueEventListener mDatabase;

    private List<HistoryModel> list;

    public HistoryAdapter(List<HistoryModel> list) {
        this.list = list;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_content_history, parent, false));
    }

    @Override
    public void onBindViewHolder(final HistoryViewHolder holder, int position) {

        HistoryModel historyModel = list.get(position);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child(historyModel.Capturista).child("Nombre").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.textCapturista.setText("Capturado por: " + dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        holder.textDate.setText(historyModel.Timestamp);
        holder.text_uuid.setText(historyModel.key);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imageView.setImageResource(R.drawable.ic_assignment);
            }
        });


        /*holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(holder.getAdapterPosition(), 0, 0, "Visualizar");
                contextMenu.add(holder.getAdapterPosition(), 0, 0, "Eliminar");
            }
        });
        */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder{

        TextView textCapturista, textDate, text_uuid;
        ImageView imageView;
        CardView cardView;

        public HistoryViewHolder(View itemView) {
            super(itemView);

            textDate = itemView.findViewById(R.id.text_date_history);
            textCapturista = itemView.findViewById(R.id.text_capturista);
            text_uuid = itemView.findViewById(R.id.text_uuidHistory);
            imageView = itemView.findViewById(R.id.btn_view);
            cardView = itemView.findViewById(R.id.card_view);
        }
    }
}