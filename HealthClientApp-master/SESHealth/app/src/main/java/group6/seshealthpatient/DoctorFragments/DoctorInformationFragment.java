package group6.seshealthpatient.DoctorFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import group6.seshealthpatient.DoctorActivities.Doctor;
import group6.seshealthpatient.R;

public class DoctorInformationFragment extends Fragment {

    //Firebase contents
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private String userID;

    //Global contents
    private Context context;
    private static final String TAG = "DoctorInfoFragment";

    //XML contents
    @BindView(R.id.info_fullNameTV)
    TextView info_fullNameTV;
    @BindView(R.id.info_dobTV)
    TextView info_dobTV;
    @BindView(R.id.info_genderTV)
    TextView info_genderTV;
    @BindView(R.id.info_addressTV)
    TextView info_addressTV;


    public DoctorInformationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.doctor_fragment_doctor_information, container, false);
        ButterKnife.bind(this, v);

        //Access Doctor child
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        ref = firebaseDatabase.getReference("Doctor");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                //Gather users Doctor class
                Doctor doctor = dataSnapshot.child(user.getUid()).getValue(Doctor.class);
                //Update TextView with Doctor class
                info_fullNameTV.setText(doctor.getName());
                info_dobTV.setText("D.O.B: " + doctor.getDob());
                info_genderTV.setText("Gender: " + doctor.getGender());
                info_addressTV.setText("Address: " + doctor.getAddress());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return v;
    }

}