package group6.seshealthpatient.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import group6.seshealthpatient.R;

public class EditProfileActivity extends AppCompatActivity {

    //Firebase contents
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String userID;

    //Global contents
    private Context context;
    private static final String TAG = "EditProfileActivity";

    //XML contents
    @BindView(R.id.edit_full_nameET)
    EditText edit_full_nameET;
    @BindView(R.id.edit_dobET)
    EditText edit_dob_ET;
    @BindView(R.id.edit_heightET)
    EditText edit_heightET;
    @BindView(R.id.edit_weightET)
    EditText edit_weightET;
    @BindView(R.id.edit_medicalInfoET)
    EditText edit_medicalInfoET;
    @BindView(R.id.edit_makeChangesBtn)
    Button edit_makeChangesBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        // You need this line on your activity so Butter Knife knows what Activity-View we are referencing
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Patient");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Patient patient = dataSnapshot.child(user.getUid()).getValue(Patient.class);
                edit_full_nameET.setText(patient.getName());
                edit_dob_ET.setText(patient.getDob());
                edit_heightET.setText(patient.getHeight());
                edit_weightET.setText(patient.getWeight());
                edit_medicalInfoET.setText(patient.getMedicalInfo());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG, "Firebase Error");
            }
        });

        edit_makeChangesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String name = edit_full_nameET.getText().toString();
                final String dob = edit_dob_ET.getText().toString();
                final String height = edit_heightET.getText().toString();
                final String weight = edit_weightET.getText().toString();
                final String medicalInfo = edit_medicalInfoET.getText().toString();

                if (name.isEmpty() || dob.isEmpty() || height.isEmpty() || weight.isEmpty() || medicalInfo.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Patient patient = dataSnapshot.child(user.getUid()).getValue(Patient.class);
                            String email = patient.getEmail();
                            String gender = patient.getGender();
                            Patient newPatient = new Patient(name, email, medicalInfo, dob, height, weight, gender);
                            databaseReference.child(user.getUid()).setValue(newPatient);
                            updateProfile();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, "Firebase Error");
                        }
                    });

                }
            }

        });

    }

    public void updateProfile() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        Toast.makeText(getBaseContext(), "Profile Updated", Toast.LENGTH_SHORT).show();
        finish();
    }

}