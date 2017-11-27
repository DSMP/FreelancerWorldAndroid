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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import not_an_example.com.freelancerworld.Adapter.MyRequestsListAdapter;
import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.MyRequestActivity;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.DividerItemDecoration;
import not_an_example.com.freelancerworld.Utils.Utils;

public class MyRequestsFragment extends Fragment {
    private OnFragmentInteractionListener mListener;
    List<RequestModel> requestModelList = new ArrayList<RequestModel>();
    private RecyclerView myRequestsRecycler;
    private MyRequestsListAdapter myRequestsAdapter;
    private HashMap<Integer,Integer> requestContractorCountMap;

    public MyRequestsFragment() {
        // Required empty public constructor
    }

    public static MyRequestsFragment newInstance(String param1, String param2) {
        MyRequestsFragment fragment = new MyRequestsFragment();
        return fragment;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myRequestsRecycler = (RecyclerView) view.findViewById(R.id.my_job_recycler);
        createAdapters();
        new AsyncGetMyRequests().execute();
    }
    private void createAdapters() {
        if ( myRequestsAdapter == null) {
            List<RequestModel> myRequestsList = new ArrayList<RequestModel>();// myRequestsList.add(new RequestModel("Kierowca PKS"));myRequestsList.add(new RequestModel("Android Developer"));myRequestsList.add(new RequestModel("Potrzebny mechanik"));
            myRequestsAdapter = new MyRequestsListAdapter(myRequestsList);
        }


        DividerItemDecoration recyclerDecoration = new DividerItemDecoration(myRequestsRecycler.getContext(),R.drawable.list_decorator);
        myRequestsRecycler.addItemDecoration(recyclerDecoration);
        myRequestsRecycler.setLayoutManager(new LinearLayoutManager(this.getContext()));

        myRequestsAdapter.setContext(this.getContext());
        myRequestsAdapter.setActivityForListener(MyRequestActivity.class);
        myRequestsRecycler.setAdapter(myRequestsAdapter);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_request, container, false);
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
    private class AsyncGetMyRequests extends AsyncTask<String,Integer,String>
    {
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        @Override
        protected String doInBackground(String... strings) {

            UserModel userModel = gson.fromJson(getActivity().getIntent().getStringExtra("user_profile"),UserModel.class);
            return new Communication().Receive("/request/getrequests/"+userModel.id, "","GET");
        }
        @Override
        protected void onPostExecute(String result)
        {
            requestModelList = gson.fromJson( result, new TypeToken<ArrayList<RequestModel>>(){}.getType());
            myRequestsAdapter.setDataset(requestModelList);
            myRequestsAdapter.notifyDataSetChanged();
        }
    }

}
