package not_an_example.com.freelancerworld;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.List;

import not_an_example.com.freelancerworld.Models.RequestModel;
import not_an_example.com.freelancerworld.Models.UserModel;

public class LegacyAdapter extends RecyclerView.Adapter<LegacyAdapter.ViewHolder>  {
    private List<String> mDataset;
    private List<RequestModel> mRequests;
    private RequestModel mRequest;

    public void setUsers(List<UserModel> mUsers) {
        this.mUsers = mUsers;
    }

    private List<UserModel> mUsers;
    private Context mContexta;
    private String mUserModel;

    public void setClass(Class mClass) {
        this.mClass = mClass;
    }

    private Class mClass;

    public void setContext(Context context)
    {
        this.mContexta = context;
        String s = mContexta.getPackageName();
    }
    public void setRequests(List<RequestModel> data)
    {
        mRequests = data;
    }

    public void setUser(String user) {
        mUserModel = user;
    }

    public void setRequest(RequestModel request) {
        this.mRequest = request;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;
        ViewHolder(TextView v) {
            super(v);
            mTextView = v;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                String user = mDataset.get(position);
                Gson gson = new Gson();
                Log.v("========Clicker",user);
                Intent intent = new Intent(mContexta, mClass);
                if (mUserModel != null)
                    intent.putExtra("user_profile", mUserModel);
                else
                    intent.putExtra("contractor_profile", gson.toJson(mUsers.get(position)));
                if (mRequests != null)
                    intent.putExtra("REQUEST",  gson.toJson(mRequests.get(position)));
                else
                    intent.putExtra("request",  gson.toJson(mRequest));
                mContexta.startActivity(intent);
            }
        }
    }

    public LegacyAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public LegacyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.job_item_recycler, parent, false);
        TextView textView = (TextView) v.findViewById(R.id.job_name);
        ViewHolder vh = new ViewHolder(textView);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTextView.setText(mDataset.get(position));

    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}