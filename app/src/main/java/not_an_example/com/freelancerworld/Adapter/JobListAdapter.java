package not_an_example.com.freelancerworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import not_an_example.com.freelancerworld.Contants.FilterConstants;
import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.Utils;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder>  {
    private List<RequestModel> mDataset;
    private String mUserModelSerialized;
    private Context mContext;
    private Class mActivity;

    public JobListAdapter(List<RequestModel> myDataset) {
        this(myDataset, null, null, null);
    }

    public JobListAdapter(List<RequestModel> myDataset, String userModelSerialized, Context currentActivityContext, Class activityClass) {
        mDataset = myDataset;
        mUserModelSerialized = userModelSerialized;
        mContext = currentActivityContext;
        mActivity = activityClass;
    }

    public void setDataset( List<RequestModel> myDataset) {
        mDataset = myDataset;
    }

    public void setActivityForListener(Class activityClass) {
        mActivity = activityClass;
    }

    public void setUser(String userSerialized) {
        mUserModelSerialized = userSerialized;
    }

    public void setContext(Context context)
    {
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mJobOfferName, mJobOfferMinPayment, mJobOfferMaxPayment;

        ViewHolder(View view) {
            super(view);
            mJobOfferName = (TextView) view.findViewById(R.id.job_offer_item_name);
            mJobOfferMinPayment = (TextView) view.findViewById(R.id.job_offer_item_min_payment_text);
            mJobOfferMaxPayment = (TextView) view.findViewById(R.id.job_offer_item_max_payment_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position == RecyclerView.NO_POSITION) { return; }

            if (mActivity != null && mContext != null) {
                Intent intent = new Intent(mContext, mActivity);
                intent.putExtra("user_profile", mUserModelSerialized);
                intent.putExtra("REQUEST", Utils.getGsonInstance().toJson(mDataset.get(position)));
                mContext.startActivity(intent);
            }
        }
    }

    @Override
    public JobListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_offer_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mJobOfferName.setText(mDataset.get(position).title);
        holder.mJobOfferMinPayment.setText("Min:" + mDataset.get(position).minPayment + FilterConstants.PAYMENT_UNIT);
        holder.mJobOfferMaxPayment.setText("Max:" + mDataset.get(position).maxPayment + FilterConstants.PAYMENT_UNIT);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}