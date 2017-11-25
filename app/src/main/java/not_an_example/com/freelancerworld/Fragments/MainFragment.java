package not_an_example.com.freelancerworld.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import not_an_example.com.freelancerworld.JobListAdapter;

import java.util.ArrayList;
import java.util.List;

import not_an_example.com.freelancerworld.JobListAdapter;
import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.DividerItemDecoration;
import not_an_example.com.freelancerworld.Utils.Filters;

public class MainFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private View mLayout;
    private RecyclerView mUpperRecycler, mLowerRecycler;
    private JobListAdapter mUpperAdapter, mLowerAdapter;
    List<String> upperJobs;

    List<RequestModel> requestModelList = new ArrayList<RequestModel>();
    List<RequestModel> filteredModelList;

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
            upperJobs = new ArrayList<>(); //upperJobs.add("Kierowca PKS");upperJobs.add("Android Developer");upperJobs.add("Potrzebny mechanik");
            mUpperAdapter = new JobListAdapter(upperJobs);
        }

        if ( mLowerAdapter == null) {
            List<String> lowerJobs = new ArrayList<>(); lowerJobs.add("Job well done");lowerJobs.add("Job not paid");
            lowerJobs.add("JIP a.k.a. job in progress");lowerJobs.add("Job awaiting... for executioner");
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
        mUpperRecycler.setAdapter(mUpperAdapter);
        mLowerRecycler.setAdapter(mLowerAdapter);
    }

    private void applyFilters() {
        filteredModelList = new ArrayList<>();
        for (RequestModel requestModel : requestModelList) {
            if ( (requestModel.minPayment >= Filters.getMinPayment()) &&
                    (requestModel.minPayment <= Filters.getMaxPayment()) &&
                    (requestModel.maxPayment <= Filters.getMaxPayment()) &&
                    (requestModel.maxPayment >= Filters.getMinPayment()) ) {
                filteredModelList.add(requestModel);
            }
        }
    }

    public class GetAllRequestsTask extends AsyncTask<Void, Void, Boolean> {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        @Override
        protected Boolean doInBackground(Void... voids) {
            UserModel userModel = gson.fromJson(getActivity().getIntent().getStringExtra("user_profile"),UserModel.class);
            String response = new Communication().Receive("/user/findrequests/" + userModel.id,"", "GET");
            Log.v("======GSON", response);
            requestModelList = gson.fromJson( response, new TypeToken<ArrayList<RequestModel>>(){}.getType());
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
//            for (RequestModel requestModel : requestModelList) {
//                upperJobs.add(requestModel.title);
//            }
//            mUpperAdapter = new JobListAdapter(requestNameList);
//            mUpperRecycler.setAdapter(mUpperAdapter);
            applyFilters();
            mUpperAdapter.notifyDataSetChanged();
            mUpperAdapter.setData(requestModelList);
            mUpperAdapter.setUser(getActivity().getIntent().getStringExtra("user_profile"));
        }
    }
}