package group6.seshealthpatient.DoctorFragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.ButterKnife;
import group6.seshealthpatient.R;

public class ViewPatientsFragment extends Fragment {

    //Firebase contents
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference ref;
    private String userID;

    //Global contents
    private Context context;
    private static final String TAG = "DoctorInfoFragment";

    EditText search_edit_text;

    RecyclerView recyclerView;

    DatabaseReference databaseReference;

    FirebaseUser firebaseUser;

    ArrayList<String> nameList;

    ArrayList<String> addressList;

    SearchAdapter searchAdapter;

    //XML contents

    public ViewPatientsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.doctor_search, container, false);

        ButterKnife.bind(this, v);



        context = v.getContext();

        search_edit_text = (EditText) v.findViewById (R.id.searchText  );

        recyclerView = (RecyclerView) v.findViewById ( R.id.result_list );



        databaseReference = FirebaseDatabase.getInstance().getReference();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();



        recyclerView.setHasFixedSize( true );

        recyclerView.setLayoutManager( new LinearLayoutManager(context) );

        recyclerView.addItemDecoration( new DividerItemDecoration( context, LinearLayoutManager.VERTICAL ) );




        nameList = new ArrayList<>();

        addressList = new ArrayList<>();



        search_edit_text.addTextChangedListener( new TextWatcher() {

            @Override

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }



            @Override

            public void onTextChanged(CharSequence s, int start, int before, int count) {



            }



            @Override

            public void afterTextChanged(Editable s) {

                if (!s.toString().isEmpty()) {

                    setAdapter( s.toString() );



                } else {



                    nameList.clear();

                    addressList.clear();


                    recyclerView.removeAllViews();

                }



            }

        } );

        return v;

    }







    private void setAdapter(final String searchedString){



        databaseReference.child("Patient").addListenerForSingleValueEvent( new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                nameList.clear();

                addressList.clear();

                recyclerView.removeAllViews();

                int counter = 0;



                for (DataSnapshot snapshot: dataSnapshot.getChildren()){

                    String uid = snapshot.getKey();

                    String name = snapshot.child("name").getValue(String.class);

                    String address = snapshot.child("email").getValue(String.class);



                    if(name.toLowerCase().contains( searchedString.toLowerCase() )){

                        nameList.add(name);

                        addressList.add(address);


                        counter++;

                    } else if (address.toLowerCase().contains( searchedString.toLowerCase() )){

                        nameList.add(name);

                        addressList.add(address);


                        counter++;

                    }



                    if(counter ==15)

                        break;



                }



                searchAdapter = new SearchAdapter( context, nameList, addressList);

                recyclerView.setAdapter( searchAdapter) ;

            }



            @Override

            public void onCancelled(DatabaseError databaseError) {



            }

        } );



    }

}
