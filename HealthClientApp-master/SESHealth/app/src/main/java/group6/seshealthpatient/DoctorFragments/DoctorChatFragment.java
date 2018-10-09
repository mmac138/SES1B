package group6.seshealthpatient.DoctorFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import group6.seshealthpatient.ChatActivity.ChatDetails;
import group6.seshealthpatient.R;


public class DoctorChatFragment extends Fragment {

    // Global variables
    private static final String TAG = "Doctor Chat Fragment";

    // XML contents
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;

    //Firebase Contents
    private DatabaseReference reference1, reference2;

    public DoctorChatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(TAG, "onCreateView");
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.chat_message_fragment, container, false);

        linearLayout = v.findViewById(R.id.linearLayout);
        relativeLayout = v.findViewById(R.id.relativeLayout);
        sendButton = v.findViewById(R.id.sendButton);
        messageArea = v.findViewById(R.id.messageArea);
        scrollView = v.findViewById(R.id.scrollView);

        String doctorUrl = "https://ses1b-af78f.firebaseio.com/Messages/" + ChatDetails.userID + "_" + ChatDetails.chatWithID;
        String patientUrl = "https://ses1b-af78f.firebaseio.com/Messages/" + ChatDetails.chatWithID + "_" + ChatDetails.userID;
        /*Log.d(TAG, "doctor: " + doctorUrl);
        Log.d(TAG, "patient: " + patientUrl);*/

        reference1  = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(doctorUrl);
        reference2  = FirebaseDatabase.getInstance()
                .getReferenceFromUrl(patientUrl);


        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                String userName = map.get("user").toString();
                String message = map.get("message").toString();

                if(userName.equals(ChatDetails.username)){
                    addMessageBox("You:\n" + message, 1);
                }
                else{
                    addMessageBox(ChatDetails.chatWith + ":\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<>();
                    map.put("user", ChatDetails.username);
                    map.put("message", messageText);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        return v;
    }

    @Override
    public void onStop() {
        reference1 = null;
        reference2 = null;
        super.onStop();
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(this.getContext());
        textView.setText(message);

        LinearLayout.LayoutParams lp2 =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 2.0f;

        // out message
        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundColor(getResources().getColor(R.color.lightBlueColor));
        }
        // in message
        else{
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundColor(getResources().getColor(R.color.lightGreyColor));
        }

        textView.setLayoutParams(lp2);
        linearLayout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }
}


