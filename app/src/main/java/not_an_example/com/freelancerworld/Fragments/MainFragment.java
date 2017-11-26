package not_an_example.com.freelancerworld.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import not_an_example.com.freelancerworld.Adapter.JobListAdapter;

import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.RequestActivity;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.DividerItemDecoration;
import not_an_example.com.freelancerworld.Utils.Filters;
import not_an_example.com.freelancerworld.Utils.Utils;

public class MainFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private UserModel mUserModel;

    private RecyclerView mUpperRecycler, mLowerRecycler;
    private JobListAdapter mUpperAdapter, mLowerAdapter;

    private List<RequestModel> myRequestModelList = new ArrayList<RequestModel>();
    private List<RequestModel> filteredModelList;
    private List<RequestModel> takenRequestModelList;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserModel = Utils.getGsonInstance().fromJson(getActivity().getIntent().getStringExtra("user_profile"), UserModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUpperRecycler = (RecyclerView) view.findViewById(R.id.upper_job_recycler);
        mLowerRecycler = (RecyclerView) view.findViewById(R.id.lower_job_recycler);
        createAdapters();
        new GetAllRequestsForUserTask().execute((Void)null);
        new GetAllRequestsTask().execute((Void)null);
    }

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
        void onFragmentInteraction(Uri uri);
    }

    private void createAdapters() {
        if ( mUpperAdapter == null) {
            List<RequestModel> upperJobs = new ArrayList<>();
            upperJobs.add(new RequestModel("Kierowca PKS"));upperJobs.add(new RequestModel("Android Developer"));upperJobs.add(new RequestModel("Potrzebny mechanik"));
            mUpperAdapter = new JobListAdapter(upperJobs);
            Map<String,Boolean> flags = new HashMap<>();
            flags.put(RequestActivity.IS_REQUEST_BTN_VISIBLE, true);
            mUpperAdapter.setActivityFlags(flags);
        }

        if ( mLowerAdapter == null) {
            List<RequestModel> lowerJobs = new ArrayList<>();
            lowerJobs.add(new RequestModel("Job well done"));lowerJobs.add(new RequestModel("Job not paid"));
            lowerJobs.add(new RequestModel("JIP a.k.a. job in progress"));lowerJobs.add(new RequestModel("Job awaiting... for executioner"));
            mLowerAdapter = new JobListAdapter(lowerJobs);
        }

        DividerItemDecoration recyclerDecoration = new DividerItemDecoration(mUpperRecycler.getContext(),R.drawable.list_decorator);
        mUpperRecycler.addItemDecoration(recyclerDecoration);
        recyclerDecoration = new DividerItemDecoration(mLowerRecycler.getContext(),R.drawable.list_decorator);
        mLowerRecycler.addItemDecoration(recyclerDecoration);

        mUpperRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mLowerRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mUpperAdapter.setContext(this.getContext());
        mLowerAdapter.setContext(this.getContext());
        mUpperAdapter.setActivityForListener(RequestActivity.class);
        mLowerAdapter.setActivityForListener(RequestActivity.class);
        mUpperRecycler.setAdapter(mUpperAdapter);
        mLowerRecycler.setAdapter(mLowerAdapter);
    }

    private void applyFilters() {
        filteredModelList = new ArrayList<>();
        for (RequestModel requestModel : myRequestModelList) {
            if ( (requestModel.minPayment >= Filters.getMinPayment()) &&
                    (requestModel.minPayment <= Filters.getMaxPayment()) &&
                    (requestModel.maxPayment <= Filters.getMaxPayment()) &&
                    (requestModel.maxPayment >= Filters.getMinPayment()) ) {
                filteredModelList.add(requestModel);
            }
        }
    }

    public class GetAllRequestsForUserTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            UserModel userModel = Utils.getGsonInstance().fromJson(getActivity().getIntent().getStringExtra("user_profile"),UserModel.class);
            String response = new Communication().Receive("/user/findrequests/" + userModel.id,"", "GET");
            Log.v("======GSON", response);
            myRequestModelList = Utils.getGsonInstance().fromJson( response, new TypeToken<ArrayList<RequestModel>>(){}.getType());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            applyFilters();
//            for (RequestModel requestModel : filteredModelList) {
//                upperJobs.add(requestModel.title);
//            }
            
            mUpperAdapter.setDataset(filteredModelList);
            mUpperAdapter.notifyDataSetChanged();
            mUpperAdapter.setUser(getActivity().getIntent().getStringExtra("user_profile"));
        }
    }

    public class GetAllRequestsTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            UserModel userModel = Utils.getGsonInstance().fromJson(getActivity().getIntent().getStringExtra("user_profile"),UserModel.class);
            String response = new Communication().Receive("/request/getall/","", "GET");
            Log.v("======GSON", response);
            takenRequestModelList = Utils.getGsonInstance().fromJson( response, new TypeToken<ArrayList<RequestModel>>(){}.getType());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            List<RequestModel> allRequestModelList = new ArrayList<RequestModel>(takenRequestModelList);

            takenRequestModelList.clear();
            for (RequestModel requestModel : allRequestModelList) {
                if (requestModel.requestTakerId == mUserModel.id) {
                    takenRequestModelList.add(requestModel);
                }
            }

            mLowerAdapter.setDataset(takenRequestModelList);
            mLowerAdapter.notifyDataSetChanged();
            mLowerAdapter.setUser(getActivity().getIntent().getStringExtra("user_profile"));
        }
    }
}