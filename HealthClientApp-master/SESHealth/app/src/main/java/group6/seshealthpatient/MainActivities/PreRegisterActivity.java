package group6.seshealthpatient.MainActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import group6.seshealthpatient.DoctorActivities.DoctorRegisterActivity;
import group6.seshealthpatient.PatientActivities.PatientRegisterActivity;
import group6.seshealthpatient.R;

public class PreRegisterActivity extends AppCompatActivity{

    @BindView(R.id.doctorIV)
    ImageView doctorIV;
    @BindView(R.id.patientIV)
    ImageView patientIV;

    private static String TAG = "PreRegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_register);
        // You need this line on your activity so Butter Knife knows what Activity-View we are referencing
        ButterKnife.bind(this);

    }

    @OnClick(R.id.patientIV)
    public void patientRegister() {
        Intent intent = new Intent(this, PatientRegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.doctorIV)
    public void doctorRegister() {
        Intent intent = new Intent(this, DoctorRegisterActivity.class);
        startActivity(intent);
        finish();
    }

}
