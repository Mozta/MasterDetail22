package com.example.mozta.masterdetail2;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
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
    private Context mContext;
    public String itemSelected;
    private List<PatientsModel> list;


    public PatientsAdapter(Context context, List<PatientsModel> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public PatientsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_content, parent, false);
        return new PatientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PatientsViewHolder holder, final int position) {
        final PatientsModel patient = list.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("TOCO","me tocaron: "+list.get(position).Folio);
                itemSelected = list.get(position).Folio;

                //Pone los datos en el fragmento derecho
                Bundle arguments = new Bundle();
                arguments.putString("KEY", list.get(position).Paciente);
                Log.d("MIO", list.get(position).Paciente);

                PatientDetailFragment fragment = new PatientDetailFragment();
                fragment.setArguments(arguments);
                ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.patient_detail_container, fragment)
                        .commit();
            }
        });

        holder.textDate.setText(patient.FechaRegistro);
        holder.textFolio.setText(patient.Folio);
        //holder.textPatient.setText(patient.Paciente);
        holder.text_nombrePatient.setText(patient.nombre);

        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(holder.getAdapterPosition(), 0, 0, "UUID: " + patient.Paciente);
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
            //textPatient = itemView.findViewById(R.id.text_uuidPatient);
            text_nombrePatient = itemView.findViewById(R.id.text_nombrePatient);
        }
    }
}