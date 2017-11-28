package not_an_example.com.freelancerworld;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import not_an_example.com.freelancerworld.Adapter.ContractorListAdapter;
import not_an_example.com.freelancerworld.Contants.FilterConstants;
import not_an_example.com.freelancerworld.Models.AddressModel;
import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.Utils;

public class MyRequestActivity extends AppCompatActivity {

    public static final String REQUEST = "request";

    private TextView mTitleText;
    private TextView mPaymentText;
    private TextView mDescriptionText;
    private TextView mCreationDateText;
    private TextView mProfessionText;
    private TextView mAdresstText;

    private RecyclerView interestsContractorsRecycler;
    private ContractorListAdapter interestsContractorsAdapter;

    private RatingBar mRequestRating;
    private Button  mFinishButton;

    private RequestModel requestModel;
    private List<UserModel> mContractors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request);
        mTitleText = (TextView) findViewById(R.id.my_request_title_text);
        mPaymentText = (TextView) findViewById(R.id.my_request_payment);
        mDescriptionText = (TextView) findViewById(R.id.my_request_description_text);
        mCreationDateText = (TextView) findViewById(R.id.my_request_created_date_text);
        mProfessionText = (TextView) findViewById(R.id.my_request_profession_text);
        mAdresstText = (TextView) findViewById(R.id.my_request_address_text);
        mRequestRating = (RatingBar) findViewById(R.id.my_request_rating_bar);
        mFinishButton = (Button) findViewById(R.id.my_request_button_rate);
        mFinishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRequestRating.setIsIndicator(true);
                requestModel.mark = (int) mRequestRating.getRating();
                requestModel.active = 0;

            }
        });

        requestModel = Utils.getGsonInstance().fromJson(getIntent().getStringExtra(REQUEST), RequestModel.class);

        mTitleText.setText(requestModel.title);
        mPaymentText.setText(requestModel.getPaymentThreshold("-", FilterConstants.PAYMENT_UNIT));
        mDescriptionText.setText(requestModel.description);
        mCreationDateText.setText(requestModel.getFormattedDate());
        mProfessionText.setText(requestModel.profession.name);
        AddressModel adress = requestModel.address;
        mAdresstText.setText(new StringBuilder(adress.buildingNumber + " " +
                adress.city + " " + adress.city + " " + adress.street + " " + adress.houseNumber + " " + adress.postalCode));
        interestsContractorsRecycler = (RecyclerView) findViewById(R.id.my_request_contractor_recycler);

        createAdapters();
        Boolean isRecyclerShown = setVisibility();
        if (isRecyclerShown) {
            interestsContractorsAdapter.setActivityForListener(ContractorActivity.class);
            interestsContractorsAdapter.setDataset(mContractors);
            interestsContractorsAdapter.setRequest(Utils.getGsonInstance().toJson(requestModel));
            new AsyncShowContractors().execute();
        }
    }

    private boolean setVisibility() {
        if (requestModel.requestTakerId != 0) {
            interestsContractorsRecycler.setVisibility(View.INVISIBLE);
            mRequestRating.setVisibility(View.VISIBLE);
            mFinishButton.setVisibility(View.VISIBLE);
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void createAdapters() {
        if ( mContractors == null) {

            mContractors = new ArrayList<>(); //upperJobs.add("Kierowca PKS");upperJobs.add("Android Developer");upperJobs.add("Potrzebny mechanik");
            interestsContractorsAdapter = new ContractorListAdapter(mContractors);
        }

//        DividerItemDecoration recyclerDecoration = new DividerItemDecoration(interestsContractorsRecycler.getContext(),R.drawable.list_decorator);
//        interestsContractorsRecycler.addItemDecoration(recyclerDecoration);

        interestsContractorsRecycler.setLayoutManager(new LinearLayoutManager(this));

        interestsContractorsAdapter.setContext(this);
        interestsContractorsRecycler.setAdapter(interestsContractorsAdapter);
    }

    private class AsyncShowContractors extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... strings) {

            return new Communication().Receive("/request/showcontractors/"+String.valueOf(requestModel.id), "","POST");
        }
        @Override
        protected void onPostExecute(String result)
        {
            mContractors = Utils.getGsonInstance().fromJson( result, new TypeToken<ArrayList<UserModel>>(){}.getType());
            interestsContractorsAdapter.setDataset(mContractors);
            interestsContractorsAdapter.notifyDataSetChanged();
        }
    }

    private class AsyncRateAndClose extends AsyncTask<String,Integer,String>
    {
        @Override
        protected String doInBackground(String... strings) {

            return new Communication().Receive("/request/showcontractors/"+String.valueOf(requestModel.id), "","POST");
        }
        @Override
        protected void onPostExecute(String result)
        {
            mContractors = Utils.getGsonInstance().fromJson( result, new TypeToken<ArrayList<UserModel>>(){}.getType());
            interestsContractorsAdapter.setDataset(mContractors);
            interestsContractorsAdapter.notifyDataSetChanged();
        }
    }
}
