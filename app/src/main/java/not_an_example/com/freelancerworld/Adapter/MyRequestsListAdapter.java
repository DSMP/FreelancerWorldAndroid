package not_an_example.com.freelancerworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import not_an_example.com.freelancerworld.Contants.FilterConstants;
import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.SmallModels.Request;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.MyRequestActivity;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.Communication;
import not_an_example.com.freelancerworld.Utils.Utils;

/**
 * Created by user on 2017-11-26.
 */

public class MyRequestsListAdapter extends RecyclerView.Adapter<MyRequestsListAdapter.ViewHolder> {
    private List<RequestModel> mDataset;
    private Map<Integer, Integer> mRequestContractorCountMap;
    private Context mContext;
    private Class mActivity;
    private Map<String, Boolean> mActivityFlags;

    public MyRequestsListAdapter(List<RequestModel> myDataset) {
        this(myDataset, null, null);
    }

    public MyRequestsListAdapter(List<RequestModel> myDataset, Context currentActivityContext, Class activityClass) {
        mDataset = myDataset;
        mContext = currentActivityContext;
        mActivity = activityClass;
    }

    private void additionalSetup() {
        mRequestContractorCountMap = new HashMap<>();
        for (RequestModel request : mDataset) {
            if (request.requestTakerId == 0) {
                mRequestContractorCountMap.put(request.id, 0);
            }
        }
        new AsyncCountContractors().execute();
    }

    public void setDataset( List<RequestModel> myDataset) {
        this.mDataset = myDataset;
        additionalSetup();
    }

    public void setRequestContractorCountMap( Map<Integer,Integer> requestContractorCountMap ) {
        mRequestContractorCountMap = requestContractorCountMap;
    }

    public void setActivityForListener(Class activityClass) {
        this.mActivity = activityClass;
    }

    public void setContext(Context context)
    {
        this.mContext = context;
    }

    public void setActivityFlags(Map<String, Boolean> activityFlags) {
        this.mActivityFlags = activityFlags;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mJobOfferName, mJobOfferContractors;
        ImageView mJobOfferAssignedContractor;

        ViewHolder(View view) {
            super(view);
            mJobOfferName = (TextView) view.findViewById(R.id.job_offer_item_name);
            mJobOfferContractors = (TextView) view.findViewById(R.id.job_offer_item_contractors);
            mJobOfferAssignedContractor = (ImageView) view.findViewById(R.id.job_offer_item_image_approved);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position == RecyclerView.NO_POSITION) { return; }

            if (mActivity != null && mContext != null) {
                Intent intent = new Intent(mContext, mActivity);
                intent.putExtra(MyRequestActivity.REQUEST, Utils.getGsonInstance().toJson(mDataset.get(position)));
                if ( mActivityFlags != null && !mActivityFlags.isEmpty()) {
                    for (String key : mActivityFlags.keySet()) {
                        intent.putExtra(key, mActivityFlags.get(key));
                    }
                }
                mContext.startActivity(intent);
            }
        }
    }

    @Override
    public MyRequestsListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_my_request_item, parent, false);
        MyRequestsListAdapter.ViewHolder vh = new MyRequestsListAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        RequestModel requestModel = mDataset.get(position);
        holder.mJobOfferName.setText(requestModel.title);
        if (requestModel.requestTakerId != 0) {
            holder.mJobOfferAssignedContractor.setVisibility(View.VISIBLE);
            holder.mJobOfferContractors.setVisibility(View.INVISIBLE);
        } else {
            holder.mJobOfferAssignedContractor.setVisibility(View.INVISIBLE);
            holder.mJobOfferContractors.setVisibility(View.VISIBLE);
            holder.mJobOfferContractors.setText(mRequestContractorCountMap.get(requestModel.id)+ " interested");
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    private class AsyncCountContractors extends AsyncTask<String,Integer,String>
    {
        protected String doInBackground(String... params) {

            String requestContractorSerialized = Utils.getGsonInstance().toJson(mRequestContractorCountMap.keySet().toArray());

            return new Communication().Receive("/request/countcontractors/",requestContractorSerialized,"POST");
        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            mRequestContractorCountMap = Utils.getGsonInstance().fromJson(result, new TypeToken<HashMap<Integer, Integer>>(){}.getType());
            notifyDataSetChanged();
        }
    }
}
