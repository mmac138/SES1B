package group6.seshealthpatient.PatientActivities;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import group6.seshealthpatient.MainActivities.LoginActivity;
import group6.seshealthpatient.PatientFragments.DataPacketFragment;
import group6.seshealthpatient.PatientFragments.HeartRateFragment;
import group6.seshealthpatient.PatientFragments.MapFragment;
import group6.seshealthpatient.PatientFragments.PatientInformationFragment;
import group6.seshealthpatient.PatientFragments.RecordVideoFragment;
import group6.seshealthpatient.PatientFragments.SendFileFragment;
import group6.seshealthpatient.R;

public class PatientMainActivity extends AppCompatActivity {

    //XML Contents
    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;
    private FragmentManager fragmentManager;
    private enum MenuStates {
        PATIENT_INFO, DATA_PACKET, HEARTRATE, RECORD_VIDEO, SEND_FILE, NAVIGATION_MAP
    }
    private MenuStates currentState;

    //Global Contents
    private static String TAG = "PatientMainActivity";
    private boolean doubleBackToExitPressedOnce = false;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.patient_activity_main);

        // the default fragment on display is the patient information
        currentState = MenuStates.PATIENT_INFO;

        // go look for the main drawer layout
        mDrawerLayout = findViewById(R.id.main_drawer_layout);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Patient Information");

        // Set up the menu button
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);

        // Setup the navigation drawer, most of this code was taken from:
        // https://developer.android.com/training/implementing-navigation/nav-drawer
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Using a switch to see which item on the menu was clicked
                        switch (menuItem.getItemId()) {
                            // You can find these id's at: res -> menu -> patient_drawer_viewwer_view.xml
                            case R.id.nav_patient_info:
                                // If the user clicked on a different item than the current item
                                if (currentState != MenuStates.PATIENT_INFO) {
                                    // change the fragment to the new fragment
                                    ChangeFragment(new PatientInformationFragment());
                                    currentState = MenuStates.PATIENT_INFO;
                                    getSupportActionBar().setTitle("Patient Information");
                                }
                                break;
                            case R.id.nav_data_packet:
                                if (currentState != MenuStates.DATA_PACKET) {
                                    ChangeFragment(new DataPacketFragment());
                                    currentState = MenuStates.DATA_PACKET;
                                    getSupportActionBar().setTitle("Create Data Packet");
                                }
                                break;
                            case R.id.nav_heartrate:
                                if (currentState != MenuStates.HEARTRATE) {
                                    ChangeFragment(new HeartRateFragment());
                                    currentState = MenuStates.HEARTRATE;
                                    getSupportActionBar().setTitle("Heart Rate");
                                }
                                break;
                            case R.id.nav_recordvideo:
                                if (currentState != MenuStates.RECORD_VIDEO) {
                                    ChangeFragment(new RecordVideoFragment());
                                    currentState = MenuStates.RECORD_VIDEO;
                                    getSupportActionBar().setTitle("Record Video");
                                }
                                break;
                            case R.id.nav_sendfile:
                                if (currentState != MenuStates.SEND_FILE) {
                                    ChangeFragment(new SendFileFragment());
                                    currentState = MenuStates.SEND_FILE;
                                    getSupportActionBar().setTitle("Send Local File");
                                }
                                break;
                            case R.id.nav_map:
                                if (currentState != MenuStates.NAVIGATION_MAP) {
                                    ChangeFragment(new MapFragment());
                                    currentState = MenuStates.NAVIGATION_MAP;
                                    getSupportActionBar().setTitle("Facilities Map");
                                }
                                break;
                            case R.id.sign_out:
                                signOut();
                                break;
                        }

                        return true;
                    }
                });

        // If you need to listen to specific events from the drawer layout.
        mDrawerLayout.addDrawerListener(
                new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                        // Respond when the drawer's position changes
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        // Respond when the drawer is opened
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        // Respond when the drawer is closed
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                        // Respond when the drawer motion state changes
                    }
                }
        );


        // More on this code, check the tutorial at http://www.vogella.com/tutorials/AndroidFragments/article.html
        fragmentManager = getFragmentManager();

        // Add the default Fragment once the user logged in
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(R.id.fragment_container, new PatientInformationFragment());
        ft.commit();
    }

    private void signOut() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        //finish() is used to prevent the user from entering this page when pressing back from the login menu
        //finish() removes the activity from the stack.
        //This activity will be reopened when opening the app or by signing out
        finish();
    }

    /**
     * Called when one of the items in the toolbar was clicked, in this case, the menu button.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
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
     * This function allows to change the content of the Fragment holder
     * @param fragment The fragment to be displayed
     */
    private void ChangeFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
