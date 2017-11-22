package not_an_example.com.freelancerworld;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder>  {
    private List<String> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTextView;
//        private Context context;
        ViewHolder(TextView v) {
            super(v);
//            this.context = context;
            mTextView = v;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                String user = mDataset.get(position);
                // We can access the data within the views
//                Toast.makeText(context, user, Toast.LENGTH_SHORT).show();
                Log.v("========Clicker",user);
            }
        }
    }

    public JobListAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public JobListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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