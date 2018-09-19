package group6.seshealthpatient.Fragments;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import group6.seshealthpatient.Activities.EditProfileActivity;
import group6.seshealthpatient.Activities.Patient;
import group6.seshealthpatient.R;

/**
 * Class: PatientInformationFragment
 * Extends: {@link Fragment}
 * Author: Carlos Tirado < Carlos.TiradoCorts@uts.edu.au> and YOU!
 * Description:
 * <p>
 * This fragment's job will be that to display patients information, and be able to edit that
 * information (either edit it in this fragment or a new fragment, up to you!)
 * <p>

 */
public class PatientInformationFragment extends Fragment {

    //Firebase contents
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private String userID;

    //Global contents
    private Context context;
    private static final String TAG = "PatientInfoFragment";

    //XML contents
    @BindView(R.id.info_fullNameTV)
    TextView info_fullNameTV;
    @BindView(R.id.info_dobTV)
    TextView info_dobTV;
    @BindView(R.id.info_genderTV)
    TextView info_genderTV;
    @BindView(R.id.info_editProfileBtn)
    Button info_editProfileBtn;
    @BindView(R.id.info_bodyIV)
    ImageView info_bodyIV;
    @BindView(R.id.info_weightTV)
    TextView info_weightTV;
    @BindView(R.id.info_heightTV)
    TextView info_heightTV;

    public PatientInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO: Instead of hardcoding the title perhaps take the user name from somewhere?
        // Note the use of getActivity() to reference the Activity holding this fragment
        getActivity().setTitle("Patient Information");
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_patient_information, container, false);

        ButterKnife.bind(this, v);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Patient");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                Patient patient = dataSnapshot.child(user.getUid()).getValue(Patient.class);

                info_fullNameTV.setText(patient.getName());
                info_dobTV.setText("D.O.B: " + patient.getDob());
                info_genderTV.setText("Gender: " + patient.getGender());
                info_weightTV.setText("Weight: " + patient.getWeight() + "kg");
                info_heightTV.setText("Height: " + patient.getHeight() + "cm");
                if(patient.getGender().equals("Male")) {
                    info_bodyIV.setImageResource(R.drawable.male_silhouette);
                } else {
                    info_bodyIV.setImageResource(R.drawable.female_silhouette);
                }


                Log.d(TAG, "datasnapshot: " + dataSnapshot);
                Log.d(TAG, "User id: " + user.getUid());
                Log.d(TAG, "Patient: " + patient);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.print("Error");
            }
        });

        return v;
    }

    @OnClick(R.id.info_editProfileBtn)
    public void editProfile() {
        Intent intent = new Intent(getActivity(), EditProfileActivity.class);
        startActivity(intent);
    }
}
