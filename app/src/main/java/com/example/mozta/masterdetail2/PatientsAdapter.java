package com.example.mozta.masterdetail2;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by mozta on 23/05/18.
 */

public class PatientsAdapter extends RecyclerView.Adapter<PatientsAdapter.PatientsViewHolder>{
    public View view;
    private PatientListActivity mParentActivity;
    private RecyclerViewOnItemClickListener recyclerViewOnItemClickListener;

    private List<PatientsModel> list;

    public PatientsAdapter(List<PatientsModel> list) {
        this.list = list;
    }

    public interface  RecyclerViewOnItemClickListener {

        void onClick(View v, int position);
    }

    @Override
    public PatientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_item, parent, false);

        return new PatientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PatientsViewHolder holder, final int position) {

        PatientsModel patient = list.get(position);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("MIO","vamooooooooooooooooooooos");
                Log.d("MIO",list.get(position).Folio);

                //PatientListActivity.intenta(list.get(position).Folio);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("TOCO","me tocaron: "+list.get(position).Folio);

                //Pone los datos en el fragmento derecho
                Bundle arguments = new Bundle();
                arguments.putString(PatientDetailFragment.ARG_ITEM_ID, list.get(position).key);
                Log.d("MIO", list.get(position).key);
                PatientDetailFragment fragment = new PatientDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.patient_detail_container, fragment)
                        .commit();
            }
        });

        holder.textDate.setText(patient.FechaRegistro);
        holder.textFolio.setText(patient.Folio);
        holder.textPatient.setText(patient.Paciente);
        holder.text_nombrePatient.setText(patient.nombre);

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(holder.getAdapterPosition(), 0, 0, "Visualizar");
                contextMenu.add(holder.getAdapterPosition(), 0, 0, "Eliminar");
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class PatientsViewHolder extends RecyclerView.ViewHolder{

        TextView textPatient, textDate, textFolio, text_nombrePatient;

        public PatientsViewHolder(View itemView) {
            super(itemView);

            textDate = itemView.findViewById(R.id.text_date);
            textFolio = itemView.findViewById(R.id.text_folio);
            textPatient = itemView.findViewById(R.id.text_uuidPatient);
            text_nombrePatient = itemView.findViewById(R.id.text_nombrePatient);
        }
    }
}