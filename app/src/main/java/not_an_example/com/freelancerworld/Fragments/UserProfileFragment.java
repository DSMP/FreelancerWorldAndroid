package not_an_example.com.freelancerworld.Fragments;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import not_an_example.com.freelancerworld.Adapter.LegacyAdapter;
import not_an_example.com.freelancerworld.MainActivity;
import not_an_example.com.freelancerworld.Models.Message;
import not_an_example.com.freelancerworld.Models.ProfessionModel;
import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.SmallModels.EditDescription;
import not_an_example.com.freelancerworld.Models.SmallModels.Professions;
import not_an_example.com.freelancerworld.Models.SmallModels.User;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.Utils;

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
    private List<RequestModel> mPortfolioList;
    private List<String> mRequestsTitles;


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
        mUserModel = Utils.getGsonInstance().fromJson(getActivity().getIntent().getStringExtra("user_profile"), UserModel.class);
        mAllSpec = new ArrayList<>();
        mUserSpec = new ArrayList<>();
        mUpperRecycler = (RecyclerView) view.findViewById(R.id.upper_job_recycler);
        mLowerRecycler = (RecyclerView) view.findViewById(R.id.lower_job_recycler);
        createAdapters();
        mSpinner = (Spinner) view.findViewById(R.id.SelectSpec);
        mDescribeEditText = (EditText) view.findViewById(R.id.describeEditText);
        mDescribeEditText.setText(mUserModel.description);
//        mDescribeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus)
//                    new AsyncEditDescribe().execute();
//            }
//        });
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
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        new AsyncEditDescribe().execute();
//        getActivity().getIntent().putExtra("user_profile", new Gson().toJson(mUserModel));
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
            mPortfolioList = new ArrayList<>();
            mRequestsTitles = new ArrayList<>();
            mLowerAdapter = new LegacyAdapter(mRequestsTitles);
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

    private void launchPortfolio()
    {
        new AsyncShowPortfolio().execute(String.valueOf(mUserModel.id));
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
            Professions[] professionses = new Professions[10]; for (int j = 0; j < professionses.length ; j++) professionses[j] = new Professions();
            professionses = Utils.getGsonInstance().fromJson(result, Professions[].class);
            for (Professions s:professionses) {
                mAllSpec.add(s.name);
            }
            spinnerAdapter.notifyDataSetChanged();
            launchPortfolio();
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
            Communication communication = new Communication();
            return communication.Receive("/user/professionadd",
                    Utils.getGsonInstance().toJson(professionModel), "PUT");
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            mUserModel = Utils.getGsonInstance().fromJson(result, UserModel.class);
        }
    }
    private class AsyncEditDescribe extends AsyncTask<String,Integer,String>
    {
        EditDescription ed = new EditDescription();
        MainActivity activity;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            activity = (MainActivity) getActivity();
        }

        @Override
        protected String doInBackground(String... params) {
            ed.id = mUserModel.id;
            ed.description = mDescribeEditText.getText().toString();
            return new Communication().Receive("/user/editdescription",Utils.getGsonInstance().toJson(ed),"PATCH");
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            Message msg = Utils.getGsonInstance().fromJson(result, Message.class);
            if (msg.status != 201)
                Toast.makeText(getContext(), msg.message, Toast.LENGTH_LONG).show();
            else
                mUserModel.description = mDescribeEditText.getText().toString();
            activity.getIntent().putExtra("user_profile", Utils.getGsonInstance().toJson(mUserModel));
            activity.refreshMenu();
        }
    }
    private class AsyncShowPortfolio extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... params) {
            return new Communication().Receive("/user/getportfolio/"+params[0],"","GET");
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            mPortfolioList = Utils.getGsonInstance().fromJson(result,  new TypeToken<ArrayList<RequestModel>>(){}.getType());
//            if (mPortfolioList!=null)
                for (RequestModel r: mPortfolioList) {
                    mRequestsTitles.add(r.title);
                }
            mLowerAdapter.notifyDataSetChanged();

        }
    }
}
