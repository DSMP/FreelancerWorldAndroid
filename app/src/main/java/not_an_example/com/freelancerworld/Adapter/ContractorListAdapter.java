package not_an_example.com.freelancerworld.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import not_an_example.com.freelancerworld.ContractorActivity;
import not_an_example.com.freelancerworld.Models.UserModel;
import not_an_example.com.freelancerworld.R;
import not_an_example.com.freelancerworld.Utils.Utils;

public class ContractorListAdapter extends RecyclerView.Adapter<ContractorListAdapter.ViewHolder>  {
    private List<UserModel> mDataset;
    private String mRequestModelSerialized;
    private Context mContext;
    private Class mActivity;
    private Map<String, Boolean> mActivityFlags;

    public ContractorListAdapter(List<UserModel> myDataset) {
        this(myDataset, null, null, null);
    }

    public ContractorListAdapter(List<UserModel> myDataset, String requestSerialized, Context currentActivityContext, Class activityClass) {
        mDataset = myDataset;
        mContext = currentActivityContext;
        mActivity = activityClass;
        mRequestModelSerialized = requestSerialized;
    }

    public void setDataset( List<UserModel> myDataset) {
        this.mDataset = myDataset;
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

    public void setRequest(String requestSerialized) {
        mRequestModelSerialized = requestSerialized;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mUserFullName;
        RatingBar mUserRating;

        ViewHolder(View view) {
            super(view);
            mUserFullName = (TextView) view.findViewById(R.id.job_contractor_item_name);
            mUserRating = (RatingBar) view.findViewById(R.id.job_contractor_item_rating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position == RecyclerView.NO_POSITION) { return; }

            if (mActivity != null && mContext != null) {
                Intent intent = new Intent(mContext, mActivity);
                intent.putExtra(ContractorActivity.CONTRACTOR, Utils.getGsonInstance().toJson(mDataset.get(position)));
                intent.putExtra(ContractorActivity.REQUEST, mRequestModelSerialized);
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
    public ContractorListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                               int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_contractor_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UserModel user = mDataset.get(position);
        holder.mUserFullName.setText(user.getFullName());
        holder.mUserRating.setRating((float)user.averageMark);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}