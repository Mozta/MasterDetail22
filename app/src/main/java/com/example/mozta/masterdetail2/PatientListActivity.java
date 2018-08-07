package com.example.mozta.masterdetail2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mozta.masterdetail2.dummy.DummyContent;

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

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //emptyText = findViewById(R.id.text_no_data);

        result = new ArrayList<>();

        recyclerView = findViewById(R.id.patient_list);
        assert recyclerView != null;

        adapter = new PatientsAdapter(this, result);

        setupRecyclerView((RecyclerView) recyclerView);

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
            recyclerView.setVisibility(View.INVISIBLE);
            //emptyText.setVisibility(View.VISIBLE);
        } else{
            recyclerView.setVisibility(View.VISIBLE);
            //emptyText.setVisibility(View.INVISIBLE);
        }
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(adapter);
    }



    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final PatientListActivity mParentActivity;
        private final List<PatientsModel> mValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DummyContent.DummyItem item = (DummyContent.DummyItem) view.getTag();

                //Pone los datos en el fragmento derecho
                /*Bundle arguments = new Bundle();
                arguments.putString(PatientDetailFragment.ARG_ITEM_ID, item.id);
                Log.d("MIO", item.id);

                PatientDetailFragment fragment = new PatientDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.patient_detail_container, fragment)
                        .commit();*/
            }
        };

        SimpleItemRecyclerViewAdapter(PatientListActivity parent,
                                      List<PatientsModel> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.patient_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            //holder.mIdView.setText(mValues.get(position).key);
            holder.mContentView.setText(mValues.get(position).nombre);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            //final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                //mIdView = (TextView) view.findViewById(R.id.text_uuidPatient);
                mContentView = (TextView) view.findViewById(R.id.text_nombrePatient);
            }
        }
    }
}
