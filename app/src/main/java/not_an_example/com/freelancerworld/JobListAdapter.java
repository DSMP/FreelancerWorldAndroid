package not_an_example.com.freelancerworld;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.ViewHolder> {
    private List<String> mDataset;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        ViewHolder(TextView v) {
            super(v);
            mTextView = v;
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