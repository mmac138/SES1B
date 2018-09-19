package group6.seshealthpatient.MainActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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
import group6.seshealthpatient.DoctorActivities.DoctorMainActivity;
import group6.seshealthpatient.PatientActivities.PatientMainActivity;
import group6.seshealthpatient.R;

public class LoginActivity extends AppCompatActivity {

    //Firebase Contents
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private String userID;

    private ProgressDialog progressDialog;

    @BindView(R.id.usernameET)
    EditText usernameEditText;
    @BindView(R.id.passwordET)
    EditText passwordEditText;

    private static String TAG = "LoginActivity";

    /**
     This is used to warn the user to exit the app when the back button is pressed twice
     */
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // You need this line on your activity so Butter Knife knows what Activity-View we are referencing
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /**
     * This method is used to exit the app once the back button is pressed twice, to confirm
     * to the user that they want to exit the application
     */
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press the back button again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }


    /**
     * See how Butter Knife also lets us add an on click event by adding this annotation before the
     * declaration of the function, making our life way easier.
     */
    @OnClick(R.id.login_btn)
    public void LogIn() {
        String username = usernameEditText.getText().toString();
        String password = passwordEditText.getText().toString();

        if(TextUtils.isEmpty(username)){
            Toast.makeText(this, "Please enter your username.", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password.", Toast.LENGTH_LONG).show();
            return;
        }

        //progressDialog.setMessage("Logging you in...");
        //progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
              //  progressDialog.dismiss();
                if(task.isSuccessful()){
                  //  finish();
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            Log.d(TAG, "UserID: " + user.getUid());
                            for (DataSnapshot patients : dataSnapshot.child("Patient").getChildren()) {
                                Log.d(TAG, "Patient ID: " + patients.getKey());
                                if (user.getUid().equals(patients.getKey())) {
                                    Log.d(TAG, "Patient Match");
                                    startActivity(new Intent(getApplicationContext(), PatientMainActivity.class));
                                }
                            }
                            for (DataSnapshot doctors : dataSnapshot.child("Doctor").getChildren()) {
                                Log.d(TAG, "Doctor ID: " + doctors.getKey());
                                if (user.getUid().equals(doctors.getKey())) {
                                    Log.d(TAG, "Doctor Match");
                                    startActivity(new Intent(getApplicationContext(), DoctorMainActivity.class));
                                }
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.d(TAG, "Error");
                        }
                    });
                    Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(getBaseContext(), "Incorrect login credentials, please try again.", Toast.LENGTH_LONG).show();
                }

            }
        });



        // TODO: For now, the login button will simply print on the console the username/password and let you in
        // TODO: It is up to you guys to implement a proper login system

        // This Toast message displays the username and password once you press the login button
       //Toast.makeText(this, username + " " + password,
         //       Toast.LENGTH_LONG).show();

        //We need to check that the email and password exist and are correct
        // Start a new activity

        //finish() is used to prevent the user from entering this page when pressing back from the main menu
        //finish() removes the activity from the stack.
        //This activity will be reopened when opening the app or by signing out

    }

    @OnClick(R.id.registerTV)
    public void register() {
        Intent intent = new Intent(this, PreRegisterActivity.class);
        startActivity(intent);
        finish();
    }
}