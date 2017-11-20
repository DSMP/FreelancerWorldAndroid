package not_an_example.com.freelancerworld.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.google.gson.Gson;

import java.util.ArrayList;

import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.R;

public class MakeJobFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    EditText mBuildingNumber;
    EditText mCity;
    EditText mHouseNumber;
    EditText mPostalCode;
    EditText mStreet;
    Spinner mSpec;
    EditText mDescription;
    SeekBar mMaxPayment;
    SeekBar mMinPayment;
    EditText mTitle;

    UserModel mUserModel;
    ArrayList<String> mAllSpec;


    public MakeJobFragment() {
        // Required empty public constructor
    }
    public static MakeJobFragment newInstance(String param1, String param2) {
        MakeJobFragment fragment = new MakeJobFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Gson gson = new Gson();
        mUserModel = gson.fromJson(getActivity().getIntent().getStringExtra("user_profile"), UserModel.class);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBuildingNumber = (EditText) view.findViewById(R.id.BuildingNumberEditText);
        mCity = (EditText) view.findViewById(R.id.CityEditText);
        mHouseNumber = (EditText) view.findViewById(R.id.HouseNumberEditText);
        mPostalCode = (EditText) view.findViewById(R.id.PostalCodeEditText);
        mStreet = (EditText) view.findViewById(R.id.StreetEditText);
        mSpec = (Spinner) view.findViewById(R.id.SpecSpinner);
        mDescription = (EditText) view.findViewById(R.id.DescriptionEditText);
        mMaxPayment = (SeekBar) view.findViewById(R.id.MaxPaymentSeekBar);
        mMinPayment = (SeekBar) view.findViewById(R.id.MinPaymentSeekBar);
        mTitle = (EditText) view.findViewById(R.id.TitleEditText);
        SpinnerAdapter spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, mAllSpec);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_make_job, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
