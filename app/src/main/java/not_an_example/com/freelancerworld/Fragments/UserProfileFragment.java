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
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.ArrayList;

import not_an_example.com.freelancerworld.JobListAdapter;
import not_an_example.com.freelancerworld.Models.ProfessionModel;
import not_an_example.com.freelancerworld.Models.SmallModels.Professions;
import not_an_example.com.freelancerworld.Models.SmallModels.User;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.Communication;

public class UserProfileFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private RecyclerView mUpperRecycler, mLowerRecycler;
    private JobListAdapter mUpperAdapter, mLowerAdapter;
    Spinner mSpinner;
    Button mSpeccAdd;
    ArrayList<String> mAllSpec;
    ArrayList<String> mUserSpec;

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
        mAllSpec.add("Stolarz"); mAllSpec.add( "Hydraulik"); mAllSpec.add("Programista");
        mUpperRecycler = (RecyclerView) view.findViewById(R.id.upper_job_recycler);
        mLowerRecycler = (RecyclerView) view.findViewById(R.id.lower_job_recycler);
        createAdapters();
        mSpinner = (Spinner) view.findViewById(R.id.SelectSpec);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, (String[]) mAllSpec.toArray(new String[mAllSpec.size()]));
        mSpinner.setAdapter(adapter);
        mSpeccAdd = (Button) view.findViewById(R.id.specAddButton);
        mSpeccAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserSpec.add((String) mSpinner.getSelectedItem());
                mUpperAdapter.notifyItemInserted(mUserSpec.size()-1);
                mUpperAdapter.notifyDataSetChanged();
                ProfessionModel professionModel = new ProfessionModel();
                User userID = new User(); userID.id = mUserModel.id;
                Professions[] professionsTable = new Professions[mUserSpec.size()+1]; int i = 0; for (int j = 0; j < professionsTable.length ; j++) professionsTable[j] = new Professions();
                for (String s: mUserSpec) {
                    professionsTable[i].name = mUserSpec.get(i);
                    i++;
                }
                professionsTable[i].name = (String) mSpinner.getSelectedItem();
                professionModel.user = userID;
                professionModel.professions = professionsTable;
                Gson gson = new Gson();
                new AsyncSendData().execute(gson.toJson(professionModel));
            }
        });
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
            mUpperAdapter = new JobListAdapter((String[]) mUserSpec.toArray(new String[mUserSpec.size()]));
        }

        if ( mLowerAdapter == null) {
            String[] lowerJobs = { "Zlecenie 11", "Zlecenie 13", "Zlecenie 21", "Zlecenie 24" };
            mLowerAdapter = new JobListAdapter(lowerJobs);
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

    private class AsyncSendData extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params) {

            Communication communication = new Communication();
            return communication.Receive("/user/professionadd", params[0]);
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            Gson gson = new Gson();
            mUserModel = gson.fromJson(result, UserModel.class);
        }
    }
}
