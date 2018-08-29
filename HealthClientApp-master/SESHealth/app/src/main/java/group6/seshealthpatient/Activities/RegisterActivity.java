package group6.seshealthpatient.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import group6.seshealthpatient.R;

/**
 * Created by Nick on 27/08/2018.
 */

public class RegisterActivity extends AppCompatActivity {

    /**
     * Use the @BindView annotation so Butter Knife can search for that view, and cast it for you
     * (in this case it will get casted to Edit Text)
     */
    @BindView(R.id.reg_full_nameET)
    EditText usernameEditText;

    /**
     * If you want to know more about Butter Knife, please, see the link I left at the build.gradle
     * file.
     */
    @BindView(R.id.reg_maleRB)
    RadioButton maleRB;
    @BindView(R.id.reg_femaleRB)
    RadioButton femaleRB;
    @BindView(R.id.reg_dobET)
    EditText dobET;
    @BindView(R.id.reg_heightET)
    EditText heightET;
    @BindView(R.id.reg_weightET)
    EditText weightET;
    @BindView(R.id.reg_medicalInfoET)
    EditText medicalInfoET;
    @BindView(R.id.registerBtn)
    Button registerBtn;

    /**
     * It is helpful to create a tag for every activity/fragment. It will be easier to understand
     * log messages by having different tags on different places.
     */
    private static String TAG = "RegisterActivity";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // You need this line on your activity so Butter Knife knows what Activity-View we are referencing
        ButterKnife.bind(this);

        // A reference to the toolbar, that way we can modify it as we please
        Toolbar toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);

        // Please try to use more String resources (values -> strings.xml) vs hardcoded Strings.
        setTitle(R.string.register_title);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActivity.this, "Registered successfully! Waiting for database activation.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
