package com.example.mozta.masterdetail2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Patients. This activity
 * has different presentations for handset and tablet-size devices.
 * On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PatientListActivity extends AppCompatActivity {
    private List<PatientsModel> result;
    private PatientsAdapter adapter;
    private TextView emptyText;
    View recyclerView;

    private DatabaseReference mDatabase, mRefNombre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mDatabase = FirebaseDatabase.getInstance().getReference().child("User").child("R0WtLh1T9dXu3QrSUyjhYK8Qd3q2").child("Registros");
        mRefNombre = FirebaseDatabase.getInstance().getReference().child("Paciente_release");

        //Se inicializa result que es la lista de modelos de pacientes
        result = new ArrayList<>();

        //Se instancia el contenedor recyclerView
        recyclerView = findViewById(R.id.patient_list);
        assert recyclerView != null;

        emptyText = findViewById(R.id.text_no_data);

        //Se crea una instancia de la la clase adaptador de pacientes
        adapter = new PatientsAdapter(this, result);

        //Pasamos el adaptador al recyclerview
        setupRecyclerView((RecyclerView) recyclerView);

        //Actualizamos la lista con los datos de la BD
        updateList();
    }

    private void updateList() {
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("MUESTRA", dataSnapshot.toString());
                final PatientsModel model = dataSnapshot.getValue(PatientsModel.class);
                model.setKey(dataSnapshot.getKey());
                mRefNombre.child(model.Paciente).child("DatosPaciente/Nombre").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            model.setNombre(dataSnapshot.getValue().toString());
                            Log.e("MUESTRA", dataSnapshot.getValue().toString());

                            Log.e("MUESTRA", model.Folio);
                            result.add(model);
                            adapter.notifyDataSetChanged();

                            checkIfEmpty();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(PatientListActivity.this, "Algún paciente registrado ya no existe, contacta al administrador.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                PatientsModel model = dataSnapshot.getValue(PatientsModel.class);
                model.setKey(dataSnapshot.getKey());

                int index = getItemIndex(model);
                result.set(index, model);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                PatientsModel model = dataSnapshot.getValue(PatientsModel.class);
                model.setKey(dataSnapshot.getKey());

                int index = getItemIndex(model);
                result.remove(index);
                adapter.notifyItemRemoved(index);

                checkIfEmpty();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int getItemIndex(PatientsModel patient){
        int index = -1;

        for (int i=0; i < result.size(); i++){
            if (result.get(i).key.equals(patient.key)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void checkIfEmpty(){
        if (result.size() == 0){
            recyclerView.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        } else{
            recyclerView.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }

}
