package group6.seshealthpatient.PatientFragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import group6.seshealthpatient.ChatActivity.ChatDetails;
import group6.seshealthpatient.DoctorActivities.Doctor;
import group6.seshealthpatient.R;

public class PatientChatSearchFragment extends Fragment{

    // XML contents
    private EditText searchText;
    private Button searchButton;
    private TextView resultTextView;

    //Global variables
    private String inputText;
    private Boolean userExist = false;
    private FragmentManager fragmentManager;
    private static final String TAG = "PatientChatSearch";
    private InputMethodManager inputMethodManager;
    private Context fragmentContext;
    private View fragmentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.chat_search_user, container, false);

        // Initialise fragment and XML contents
        fragmentView = v;
        searchText = v.findViewById(R.id.searchUserEditText);
        searchButton = v.findViewById(R.id.searchUserButton);
        resultTextView = v.findViewById(R.id.searchUserTextView);
        fragmentManager = getFragmentManager();
        fragmentContext = v.getContext();

        searchButton.setText("Search doctor");

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputText = searchText.getText().toString();
                Query query = FirebaseDatabase.getInstance().getReference().child("Doctor").orderByChild("email").equalTo(inputText);

                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Log.d(TAG, "onDatachange");
                        if(dataSnapshot.exists()){
                            userExist = true;
                            Log.d(TAG, userExist.toString());
                            Log.d(TAG, dataSnapshot.toString());

                            for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                Log.d(TAG, snapshot.toString());
                                Log.d(TAG, "DATA SNAPSHOT KEY: " + snapshot.getKey());
                                ChatDetails.chatWithID = snapshot.getKey();
                                Doctor doctor = snapshot.getValue(Doctor.class);
                                ChatDetails.chatWith = doctor.getName();
                                Log.d(TAG, "ChatWith: " + ChatDetails.chatWith);
                                break;
                            }
                            ChangeFragment(new PatientChatFragment());

                        }
                        else{
                            resultTextView.setText("Cannot find this doctor!!!");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                hideKeyBoard();
            }
        });

        return v;
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

    // This function hides the keyboard
    private void hideKeyBoard(){
        inputMethodManager = (InputMethodManager)
                fragmentContext.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(fragmentView.getWindowToken(), 0);
    }

}

