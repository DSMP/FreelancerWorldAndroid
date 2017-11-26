package not_an_example.com.freelancerworld.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import not_an_example.com.freelancerworld.Adapter.LegacyAdapter;
import not_an_example.com.freelancerworld.Models.ProfessionModel;
import not_an_example.com.freelancerworld.Models.SmallModels.Professions;
import not_an_example.com.freelancerworld.Models.SmallModels.User;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.Communication;

public class UserProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView mUpperRecycler, mLowerRecycler;
    private LegacyAdapter mUpperAdapter, mLowerAdapter;
    Spinner mSpinner;
    Button mSpeccAdd;
    Button mSpeccRem;
    ArrayList<String> mAllSpec;
    ArrayList<String> mUserSpec;
    ArrayAdapter<String> spinnerAdapter;
    EditText mDescribeEditText;

    UserModel mUserModel;


    public UserProfileFragment() {
        // Required empty public constructor
    }

    public static UserProfileFragment newInstance(String param1, String param2) {
        UserProfileFragment fragment = new UserProfileFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Gson gson = new Gson();
        mUserModel = gson.fromJson(getActivity().getIntent().getStringExtra("user_profile"), UserModel.class);
        mAllSpec = new ArrayList<>();
        mUserSpec = new ArrayList<>();
        mUpperRecycler = (RecyclerView) view.findViewById(R.id.upper_job_recycler);
        mLowerRecycler = (RecyclerView) view.findViewById(R.id.lower_job_recycler);
        createAdapters();
        mSpinner = (Spinner) view.findViewById(R.id.SelectSpec);
        mDescribeEditText = (EditText) view.findViewById(R.id.describeEditText);
        mDescribeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    new AsyncEditDescribe().execute();
            }
        });
        spinnerAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, mAllSpec);
        mSpinner.setAdapter(spinnerAdapter);
        mSpeccAdd = (Button) view.findViewById(R.id.specAddButton);
        mSpeccRem = (Button) view.findViewById(R.id.specRemButton);
        new AsyncGetAllProfs().execute();
        mSpeccAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserSpec.add((String) mSpinner.getSelectedItem());
                mUpperAdapter.notifyItemInserted(mUserSpec.size()-1);
                mUpperAdapter.notifyDataSetChanged();
                new AsyncPutProfession().execute();
            }
        });
        mSpeccRem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserSpec.remove((String) mSpinner.getSelectedItem());
                mUpperAdapter.notifyDataSetChanged();
                new AsyncPutProfession().execute();
            }
        });
        for (Professions p: mUserModel.professions) {
            mUserSpec.add(p.name);
        }
    }

    public void OnDestroyView()
    {
        new AsyncEditDescribe().execute();
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

    private void createAdapters() {
        if ( mUpperAdapter == null) {
            mUpperAdapter = new LegacyAdapter(mUserSpec);
        }

        if ( mLowerAdapter == null) {
            List<String> lowerJobs = new ArrayList<>(); for (int i=1; i< 5 ; i++){lowerJobs.add("zlecenie " + (i*3-2));}
            mLowerAdapter = new LegacyAdapter(lowerJobs);
        }

        mUpperRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mLowerRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mUpperRecycler.setAdapter(mUpperAdapter);
        mLowerRecycler.setAdapter(mLowerAdapter);

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

    private class AsyncPutProfession extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params) {
            ProfessionModel professionModel = new ProfessionModel();
            User userID = new User(); userID.id = mUserModel.id;
            Professions[] professionsTable = new Professions[mUserSpec.size()]; int i = 0; for (int j = 0; j < professionsTable.length ; j++) professionsTable[j] = new Professions();
            for (String s: mUserSpec) {
                professionsTable[i].name = mUserSpec.get(i);
                i++;
            }
            professionModel.user = userID;
            professionModel.professions = professionsTable;
            Gson gson = new Gson();
            Communication communication = new Communication();
            return communication.Receive("/user/professionadd",
                    gson.toJson(professionModel), "PUT");
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            Gson gson = new Gson();
            mUserModel = gson.fromJson(result, UserModel.class);
        }
    }
    private class AsyncEditDescribe extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params) {
            return new Communication().Receive();
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
        }
    }
}
