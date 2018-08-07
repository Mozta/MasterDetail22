package com.example.mozta.masterdetail2;

import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a single Patient detail screen.
 * This fragment is either contained in a {@link PatientListActivity}
 * in two-pane mode (on tablets)
 */
public class PatientDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public String ARG_ITEM_ID = "item_id";
    private String mNacimiento;

    private View rootView;

    private FloatingActionButton fabPatient;

    private DatabaseReference mDatabase, mRefNombre;

    private TextView emptyText;
    private RecyclerView recyclerView;
    private List<HistoryModel> result;
    private HistoryAdapter adapter;

    private String nombre;

    /**
     * The dummy content this fragment is presenting.
     */
    private PatientsModel mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PatientDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            ARG_ITEM_ID = bundle.getString("KEY");
        }

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            //mItem = DummyContent.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Toast.makeText(getContext(), ARG_ITEM_ID, Toast.LENGTH_SHORT).show();

        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Paciente_release").child(ARG_ITEM_ID).child("Expedientes");

        mRefNombre = FirebaseDatabase.getInstance().getReference().child("Paciente_release");

        mRefNombre.child(ARG_ITEM_ID).child("DatosPaciente").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    PatientModel patientModel = dataSnapshot.getValue(PatientModel.class);

                    CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
                    if (appBarLayout != null) {
                        appBarLayout.setTitle(patientModel.Nombre);
                    }
                    nombre = patientModel.Nombre;

                    //mNacimiento = dataSnapshot.getChildren("Nacimiento").getValue().toString();
                    Log.d("QUERY",patientModel.Nacimiento);
                    ((TextView) rootView.findViewById(R.id.txt_nacimiento)).setText(patientModel.Nacimiento);
                    ((TextView) rootView.findViewById(R.id.txt_residencia)).setText(patientModel.Residencia);
                    ((TextView) rootView.findViewById(R.id.txt_ocupacion)).setText(patientModel.Ocupacion);
                    ((TextView) rootView.findViewById(R.id.txt_rfc)).setText(patientModel.RFC);
                    ((TextView) rootView.findViewById(R.id.txt_curp)).setText(patientModel.CURP);
                    ((TextView) rootView.findViewById(R.id.txt_sangre)).setText(patientModel.Sangre);
                    fabPatient = ((FloatingActionButton) rootView.findViewById(R.id.fab_patient));

                    Glide.with(rootView)
                            .load(patientModel.patient_image)
                            .into(fabPatient);

                    FloatingActionButton fab = ((FloatingActionButton) rootView.findViewById(R.id.fab));
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Snackbar.make(view, "Anadir nuevo registro al paciente: "+nombre, Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                        }
                    });

                    result = new ArrayList<>();

                    recyclerView = ((RecyclerView) rootView.findViewById(R.id.patient_list_history));
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(rootView.getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

                    recyclerView.setLayoutManager(linearLayoutManager);

                    adapter = new HistoryAdapter(result);
                    recyclerView.setAdapter(adapter);

                    updateList();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //emptyText = rootView.findViewById(R.id.text_no_data);
    }

    private void updateList(){
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                final HistoryModel model = dataSnapshot.getValue(HistoryModel.class);
                model.setKey(dataSnapshot.getKey());

                //model.setNombre(dataSnapshot.getValue().toString());
                Log.e("MUESTRA", dataSnapshot.getValue().toString());


                result.add(model);
                adapter.notifyDataSetChanged();

                checkIfEmpty();

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                HistoryModel model = dataSnapshot.getValue(HistoryModel.class);
                model.setKey(dataSnapshot.getKey());

                int index = getItemIndex(model);
                result.set(index, model);
                adapter.notifyItemChanged(index);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                HistoryModel model = dataSnapshot.getValue(HistoryModel.class);
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

    private int getItemIndex(HistoryModel historyModel){
        int index = -1;

        for (int i=0; i < result.size(); i++){
            if (result.get(i).key.equals(historyModel.key)) {
                index = i;
                break;
            }
        }
        return index;

    }

    private void removePatient(int position){
        mDatabase.child(result.get(position).key).removeValue();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.patient_detail, container, false);

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null) {
            appBarLayout.setTitle("");
        }

        // Show the dummy content as text in a TextView.
        ((TextView) rootView.findViewById(R.id.txt_nacimiento)).setText("");

        return rootView;
    }
}
