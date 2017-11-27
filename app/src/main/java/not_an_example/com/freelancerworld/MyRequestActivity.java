package not_an_example.com.freelancerworld;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import not_an_example.com.freelancerworld.Adapter.ContractorListAdapter;
import not_an_example.com.freelancerworld.Adapter.JobListAdapter;
import not_an_example.com.freelancerworld.Adapter.LegacyAdapter;
import not_an_example.com.freelancerworld.Models.AddressModel;
import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.Utils;

public class MyRequestActivity extends AppCompatActivity {

    public static final String REQUEST = "request";

    private TextView mTitleText;
    private TextView mMinPaymentText;
    private TextView mMaxPaymentText;
    private TextView mDescriptionText;
    private TextView mCreationDateText;
    private TextView mProfessionText;
    private TextView mAdresstText;
    private TextView mUserText;

    private RecyclerView interestsContractorsRecycler;
    private ContractorListAdapter interestsContractorsAdapter;


    private RequestModel requestModel;
    private List<UserModel> mContractors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_request);
        mTitleText = (TextView) findViewById(R.id.TitleText);
        mMinPaymentText = (TextView) findViewById(R.id.MinPaymentText);
        mMaxPaymentText = (TextView) findViewById(R.id.MaxPaymentText);
        mDescriptionText = (TextView) findViewById(R.id.DescriptionText);
        mCreationDateText = (TextView) findViewById(R.id.CreationDateText);
        mProfessionText = (TextView) findViewById(R.id.ProfessionText);
        mAdresstText = (TextView) findViewById(R.id.AdresstText);
        mUserText = (TextView) findViewById(R.id.UserText);

        requestModel = Utils.getGsonInstance().fromJson(getIntent().getStringExtra(REQUEST), RequestModel.class);

        mTitleText.setText("Title: " + requestModel.title);
        mMinPaymentText.setText("Min payment: " + String.valueOf(requestModel.minPayment));
        mMaxPaymentText.setText("Max payment: " + String.valueOf(requestModel.maxPayment));
        mDescriptionText.setText("Description: " + requestModel.description);
        mCreationDateText.setText("Date created: " + requestModel.creationDate.toString());
        mProfessionText.setText("Lookingo for: " + requestModel.profession.name);
        AddressModel adress = requestModel.address; mAdresstText.setText(new StringBuilder("Adres: " + adress.buildingNumber + " " +
                adress.city + " " + adress.city + " " + adress.street + " " + adress.houseNumber + " " + adress.postalCode));
        mUserText.setText("Signature: " + requestModel.user.name + " " + requestModel.user.lastName);
        interestsContractorsRecycler = (RecyclerView) findViewById(R.id.ContractorsRecycler);

        createAdapters();

        //TODO: narazie bo nie ma jeszcze
        interestsContractorsAdapter.setActivityForListener(ContractorActivity.class);
        interestsContractorsAdapter.setDataset(mContractors);
        interestsContractorsAdapter.setRequest(Utils.getGsonInstance().toJson(requestModel));
        new AsyncShowContractors().execute();
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
}
