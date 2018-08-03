package com.example.mozta.masterdetail2;

import android.app.Activity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mozta.masterdetail2.dummy.DummyContent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
