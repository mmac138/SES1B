package group6.seshealthpatient.DoctorFragments;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import group6.seshealthpatient.DoctorActivities.PatientDetail;
import group6.seshealthpatient.PatientActivities.Patient;
import group6.seshealthpatient.R;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {
    Context context;



    private Context mContext;

    private LayoutInflater bsman = null;



    ArrayList<String> nameList;

    ArrayList<String> addressList;

    DatabaseReference databaseReference;





    class SearchViewHolder extends RecyclerView.ViewHolder{


        TextView name, address,tRating;


        public SearchViewHolder(View itemView){

            super(itemView);

            name = (TextView) itemView.findViewById( R.id.restaurantName );

            address = (TextView) itemView.findViewById( R.id.restaurantAddress );

        }

    }



    public SearchAdapter(Context context, ArrayList<String> nameList,ArrayList<String> addressList){

        this.context = context;

        this.nameList = nameList;

        this.addressList = addressList;




        this.mContext = context;

        bsman = LayoutInflater.from(context);



    }



    @Override

    public SearchAdapter.SearchViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        databaseReference = FirebaseDatabase.getInstance().getReference();



        View view = LayoutInflater.from( context ).inflate( R.layout.patient_intro ,parent,false);

        return new SearchAdapter.SearchViewHolder( view );

    }



    @Override

    public void onBindViewHolder(final SearchViewHolder holder, final int position) {

        holder.name.setText( nameList.get( position ) );

        holder.address.setText( addressList.get( position ) );




        databaseReference.child("Patient").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override

            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot ds:dataSnapshot.getChildren()){

                    String rName = ds.child("name").getValue(String.class);

                    String rAddress = ds.child("email").getValue(String.class);

                    if(areSame(rName,nameList.get(position))&&areSame(rAddress,addressList.get(position))){

                        holder.itemView.setOnClickListener(new View.OnClickListener() {

                            @Override

                            public void onClick(View v) {

                                Toast.makeText(mContext, ds.child("name").getValue(String.class), Toast.LENGTH_SHORT).show();



                                Intent intent = new Intent(mContext,PatientDetail.class);

                                intent.putExtra("name",ds.child("name").getValue(String.class));

                                intent.putExtra("address",ds.child("email").getValue(String.class));



                                mContext.startActivity(intent);

                            }

                        });

                    }

                }

            }



            @Override

            public void onCancelled(DatabaseError databaseError) {



            }

        });

    }







    @Override

    public int getItemCount() {

        return nameList.size();

    }



    private boolean areSame(String string1, String string2){

        return string1.equals(string2);

    }





}

