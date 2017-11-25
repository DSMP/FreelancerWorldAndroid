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

import not_an_example.com.freelancerworld.Models.AddressModel;
import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.DividerItemDecoration;

public class MyRequestActivity extends AppCompatActivity {

    TextView mTitleText;
    TextView mMinPaymentText;
    TextView mMaxPaymentText;
    TextView mDescriptionText;
    TextView mCreationDateText;
    TextView mProfessionText;
    TextView mAdresstText;
    TextView mUserText;

    private RecyclerView interestsContractorsRecycler;
    private JobListAdapter interestsContractorsAdapter;
    private List<String> interestsContractorsList;

    RequestModel requestModel;
    UserModel mUserModel;
    List<UserModel> mContractors;

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

        Gson gson = new Gson();
        requestModel = gson.fromJson(getIntent().getStringExtra("REQUEST"), RequestModel.class);
        mUserModel = gson.fromJson(getIntent().getStringExtra("user_profile"), UserModel.class);;

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
        interestsContractorsAdapter.setClass(null);
        new AsyncShowContractors().execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void createAdapters() {
        if ( interestsContractorsAdapter == null) {
            interestsContractorsList = new ArrayList<>(); //upperJobs.add("Kierowca PKS");upperJobs.add("Android Developer");upperJobs.add("Potrzebny mechanik");
            interestsContractorsAdapter = new JobListAdapter(interestsContractorsList);
        }


//        DividerItemDecoration recyclerDecoration = new DividerItemDecoration(interestsContractorsRecycler.getContext(),R.drawable.list_decorator);
//        interestsContractorsRecycler.addItemDecoration(recyclerDecoration);

        interestsContractorsRecycler.setLayoutManager(new LinearLayoutManager(this));

        interestsContractorsAdapter.setContext(this);
        interestsContractorsRecycler.setAdapter(interestsContractorsAdapter);
    }

    private class AsyncShowContractors extends AsyncTask<String,Integer,String>
    {
        Gson gson =  new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

        @Override
        protected String doInBackground(String... strings) {

            return new Communication().Receive("/request/showcontractors/"+String.valueOf(requestModel.id), "","POST");
        }
        @Override
        protected void onPostExecute(String result)
        {
            mContractors = gson.fromJson( result, new TypeToken<ArrayList<UserModel>>(){}.getType());
            for (UserModel u: mContractors) {
                interestsContractorsList.add(u.name + " " + u.lastName);
            }
            interestsContractorsAdapter.setUsers(mContractors);
            interestsContractorsAdapter.notifyDataSetChanged();
        }
    }
}
