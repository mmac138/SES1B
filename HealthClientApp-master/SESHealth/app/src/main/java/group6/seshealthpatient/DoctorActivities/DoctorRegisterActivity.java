package group6.seshealthpatient.DoctorActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import group6.seshealthpatient.MainActivities.LoginActivity;
import group6.seshealthpatient.MainActivities.PreRegisterActivity;
import group6.seshealthpatient.R;

public class DoctorRegisterActivity extends AppCompatActivity {

    //XML Contents
    @BindView(R.id.reg_full_nameET)
    EditText usernameEditText;
    @BindView(R.id.reg_maleRB)
    RadioButton maleRB;
    RadioButton radioButton;
    @BindView(R.id.reg_genderTV)
    TextView genderTv;
    @BindView(R.id.reg_radioGroup)
    RadioGroup radioGroup;
    @BindView(R.id.reg_femaleRB)
    RadioButton femaleRB;
    @BindView(R.id.reg_dobET)
    EditText dobET;
    @BindView(R.id.registerBtn)
    Button registerBtn;
    @BindView(R.id.reg_emailAddressET)
    EditText emailET;
    @BindView(R.id.reg_passwordET)
    EditText passwordET;
    @BindView(R.id.reg_streetET)
    EditText streetET;
    @BindView(R.id.reg_cityET)
    EditText cityET;
    @BindView(R.id.reg_postcodeET)
    EditText postcodeET;


    //Firebase Contents
    FirebaseDatabase fireBaseDatabase;
    DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    //Global Contents
    private static String TAG = "DoctorRegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.doctor_activity_register);
        ButterKnife.bind(this);

        //Access Doctor child
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("Doctor");

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailET.getText().toString().trim();
                String password = passwordET.getText().toString().trim();
                String name = usernameEditText.getText().toString();
                String dob = dobET.getText().toString();
                String address = streetET.getText() + " " + cityET.getText() + " " + postcodeET.getText();
                int selectedId = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton) findViewById(selectedId);
                String gender = radioButton.getText().toString();

                //Create new Doctor Profile
                final Doctor doctor = new Doctor(name, email, dob, gender, address);
                //Check if all fields are filled
                if (name.isEmpty() || dob.isEmpty() || email.isEmpty()) {
                    Toast.makeText(getBaseContext(), "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                //Add Doctor to Firebase Database
                                databaseReference.child(user.getUid()).setValue(doctor);
                                Toast.makeText(group6.seshealthpatient.DoctorActivities.DoctorRegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
                                login();
                            } else {
                                Toast.makeText(group6.seshealthpatient.DoctorActivities.DoctorRegisterActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }

        });

    }

    @OnClick(R.id.loginTV)
    public void login() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, PreRegisterActivity.class);
        startActivity(intent);
        finish();
    }

}