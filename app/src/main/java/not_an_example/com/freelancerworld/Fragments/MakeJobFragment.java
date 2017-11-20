package not_an_example.com.freelancerworld.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import not_an_example.com.freelancerworld.Models.SmallModels.Professions;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.Communication;

public class MakeJobFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    EditText mBuildingNumber;
    EditText mCity;
    EditText mHouseNumber;
    EditText mPostalCode;
    EditText mStreet;
    Spinner mSpec;
    EditText mDescription;
    TextView mMaxPaymentT;
    SeekBar mMaxPayment;
    TextView mMinPaymentT;
    SeekBar mMinPayment;
    EditText mTitle;

    UserModel mUserModel;
    ArrayList<String> mAllSpec;
    ArrayAdapter<String> spinnerAdapter;


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
        mAllSpec = new ArrayList<>();
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
        mMaxPaymentT = (TextView) view.findViewById(R.id.MaxPaymentTextView);
        mMaxPayment = (SeekBar) view.findViewById(R.id.MaxPaymentSeekBar);
        mMinPaymentT = (TextView) view.findViewById(R.id.MinPaymentTextView);
        mMinPayment = (SeekBar) view.findViewById(R.id.MinPaymentSeekBar);
        mTitle = (EditText) view.findViewById(R.id.TitleEditText);
        mMaxPaymentT.setText("200"); mMinPaymentT.setText("0");
        mMaxPayment.setMax(200); mMinPayment.setMax(100); mMaxPayment.setProgress(200);
        mMaxPayment.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMaxPaymentT.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        mMinPayment.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mMinPaymentT.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, mAllSpec);
        mSpec.setAdapter(spinnerAdapter);
        new AsyncGetAllProfs().execute();

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
    private class AsyncGetAllProfs extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... params) {
            Communication communication = new Communication();
            String JSON = communication.Receive("/profession/getall", "", "GET");
            return JSON;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            Gson gson = new Gson();
            Professions[] professionses = new Professions[10]; for (int j = 0; j < professionses.length ; j++) professionses[j] = new Professions();
            professionses = gson.fromJson(result, Professions[].class);
            for (Professions s:professionses) {
                mAllSpec.add(s.name);
            }
            spinnerAdapter.notifyDataSetChanged();
        }
    }
    private class AsyncSendNewRequest extends AsyncTask<String,Integer,String>
    {

        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
        @Override
        protected void onPostExecute(String result)
        {}
    }
}
