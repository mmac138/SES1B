package group6.seshealthpatient.PatientFragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import group6.seshealthpatient.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DataPacketFragment extends Fragment {


    public DataPacketFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.patient_fragment_data_packet, container, false);
    }

}
