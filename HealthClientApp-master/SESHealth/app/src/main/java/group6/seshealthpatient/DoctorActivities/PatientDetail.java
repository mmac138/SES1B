package group6.seshealthpatient.DoctorActivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
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
import group6.seshealthpatient.PatientActivities.EditProfileActivity;
import group6.seshealthpatient.PatientActivities.Patient;
import group6.seshealthpatient.R;

public class PatientDetail extends AppCompatActivity {

    //Firebase contents
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private String userID;
    private Context mContext;
    //Global contents
    private Context context;
    private static final String TAG = "PatientInfo";

    private String test;

    private String name, address, med,dob,height,weight,gender;

    private long phone;

    private int rating;

    private TextView mMedicalInfo, mName, mAddress,mDob,mHeight,mWeight,mGender;


    private Button mAddComment, mViewComment;

    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    private FirebaseDatabase mFirebaseDatabase;

    private DatabaseReference myRef;

    DatabaseReference databaseReference;




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_profile);
        mContext = PatientDetail.this;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Log.d(TAG, "onCreate: start");


        setupFirebaseAuth();

        myRef = FirebaseDatabase.getInstance().getReference().child("Patient");
        myRef.keepSynced(true);


        initWidgets();

        getIncomingIntent();


//        addComment.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//
//            public void onClick(View v) {
//
//                if (userID.equals("")) {
//
//                    Intent intent = new Intent(mContext, LoginActivity.class);
//
//                    startActivity(intent);
//
//                } else {
//
//                    Intent intent = new Intent(mContext, AddUserCommentActivity.class);
//
//                    intent.putExtra("res_id", res_id);
//
//                    startActivity(intent);
//
//                    finish();
//
//
//                }
//
//            }
//
//        });


//        Button viewComment = (Button) findViewById(R.id.btnViewComments);
//
//        viewComment.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//
//            public void onClick(View v) {
//
//                Intent intent = new Intent(mContext, ViewUserCommentActivity.class);
//
//                intent.putExtra("res_id", res_id);
//
//                startActivity(intent);
//
//            }
//
//        });
//
    }

    private void getIncomingIntent() {

        Log.d(TAG, "getIncomingIntent: check for incoming intent");


        if (getIntent().hasExtra("name") && getIntent().hasExtra("address") ) {

            Log.d(TAG, "getIncomingIntent: found intent extras.");


            String rName = getIntent().getStringExtra("name");

            String rAddress = getIntent().getStringExtra("address");
//
//            String rDob = getIntent().getStringExtra("dob");
//
//            String rHeight = getIntent().getStringExtra("height");
//
//            String rWeight = getIntent().getStringExtra("weight");
//            String rGender = getIntent().getStringExtra("gender");
//            String rMedicalInfo = getIntent().getStringExtra("medicalInfo");
//            && getIntent().hasExtra("medicalInfo") && getIntent().hasExtra("dob") && getIntent().hasExtra("height")&& getIntent().hasExtra("weight")&& getIntent().hasExtra("gender")

            showData(rName, rAddress);

        }

    }


    private void showData(final String rName, final String rAddress) {


        databaseReference.child("Patient").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot ds : dataSnapshot.getChildren()) {

                    String name = ds.child("name").getValue(String.class);

                    Log.d(TAG, "onDataChange: test " + name + " " + rName);


                    String address = ds.child("email").getValue(String.class);

                    Log.d(TAG, "onDataChange: test " + address + " " + rAddress);

                    if (areSame(rName, name) && areSame(rAddress, address)) {

                        Log.d(TAG, "setRestaurant: setting profile photo name and address");


                        TextView dName = (TextView) findViewById(R.id.info_fullName);

                        dName.setText(ds.child("name").getValue(String.class));


                        TextView dDob = (TextView) findViewById(R.id.info_dob);

                        dDob.setText("D.O.B: " + ds.child("dob").getValue(String.class));


                        TextView dAddress = (TextView) findViewById(R.id.info_email);

                        dAddress.setText("Email: " + ds.child("email").getValue(String.class));


                        TextView dGender = (TextView) findViewById(R.id.info_gender);

                        dGender.setText("Gender: " + ds.child("gender").getValue(String.class));


                        TextView dHeight = (TextView) findViewById(R.id.info_height);

                        dHeight.setText("Height: " + ds.child("height").getValue(String.class) + "cm");


                        TextView dWeight = (TextView) findViewById(R.id.info_weight);

                        dWeight.setText("Weight: " + ds.child("weight").getValue(String.class)+ " kg");


                        TextView dMedicalInfo = (TextView) findViewById(R.id.info_med);

                        dMedicalInfo.setText("Medical Info: " + ds.child("medicalInfo").getValue(String.class));

                    } else {

                        Log.d(TAG, "onDataChange: no restaurant found ");

                    }

                }

            }


            @Override

            public void onCancelled(DatabaseError databaseError) {


            }

        });

    }


    private void initWidgets() {

        Log.d(TAG, "intWidgets: Initializing Widgets.");

        mName = (TextView) findViewById(R.id.info_fullName);

        mDob = (TextView) findViewById(R.id.info_dob);

        mGender = (TextView) findViewById(R.id.info_gender);

        mHeight = (TextView) findViewById(R.id.info_height);

        mWeight = (TextView) findViewById(R.id.info_weight);

        mMedicalInfo= (TextView) findViewById(R.id.info_med);

        mAddress = (TextView) findViewById(R.id.info_email);

//
//        mAddComment = (Button) findViewById(R.id.btnAddComments);
//
//        mViewComment = (Button) findViewById(R.id.btnViewComments);

        mContext = PatientDetail.this;


        name = mName.getText().toString();

        dob = mDob.getText().toString();

       gender = mGender.getText().toString();

        height= mHeight.getText().toString();

        weight=mWeight.getText().toString();

        med=mMedicalInfo.getText().toString();

        address=mAddress.getText().toString();


        Log.d(TAG, "initWidgets: ++++++");


    }


    private boolean areSame(String string1, String string2) {

        return string1.equals(string2);

    }


    private void setupFirebaseAuth() {

        Log.d(TAG, "setupFirebaseAuth: setting up firebase auth");


        mAuth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override

            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();


                if (user != null) {

                    // user is signed in

                    Log.d(TAG, "onAuthStateChanged: signed_in: " + user.getUid());

                    userID = user.getUid();

                } else {

                    // user is signed out

                    Log.d(TAG, "onAuthStateChanged: signed_out");

                    userID = "";

                }

            }

        };

    }


    @Override

    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

        mAuth.addAuthStateListener(mAuthListener);

    }


    @Override

    public void onStop() {

        super.onStop();

        if (mAuthListener != null) {

            mAuth.removeAuthStateListener(mAuthListener);

        }

    }

}
