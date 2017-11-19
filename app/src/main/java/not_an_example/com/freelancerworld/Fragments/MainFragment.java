package not_an_example.com.freelancerworld.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import not_an_example.com.freelancerworld.JobListAdapter;

import java.util.ArrayList;

import not_an_example.com.freelancerworld.JobListAdapter;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.DividerItemDecoration;

public class MainFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private View mLayout;
    private RecyclerView mUpperRecycler, mLowerRecycler;
    private JobListAdapter mUpperAdapter, mLowerAdapter;

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
            ArrayList<String> upperJobs = new ArrayList<String>() {{ add("Steve Jobs"); add("No Jobs"); add("Blow Jobs");}};
            mUpperAdapter = new JobListAdapter(upperJobs);
        }

        if ( mLowerAdapter == null) {
            ArrayList<String> lowerJobs = new ArrayList<String>() {{ add("Job well done"); add("Job not paid"); add("JIP a.k.a. job in progress"); add("Job awaiting... for executioner"); }};
            mLowerAdapter = new JobListAdapter(lowerJobs);
        }

        DividerItemDecoration recyclerDecoration = new DividerItemDecoration(mUpperRecycler.getContext(),R.drawable.list_decorator);
        mUpperRecycler.addItemDecoration(recyclerDecoration);
        recyclerDecoration = new DividerItemDecoration(mLowerRecycler.getContext(),R.drawable.list_decorator);
        mLowerRecycler.addItemDecoration(recyclerDecoration);

        mUpperRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mLowerRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        mUpperRecycler.setAdapter(mUpperAdapter);
        mLowerRecycler.setAdapter(mLowerAdapter);
    }
}
