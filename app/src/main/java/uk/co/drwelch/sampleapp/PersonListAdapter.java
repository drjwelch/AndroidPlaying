package uk.co.drwelch.sampleapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PersonListAdapter extends RecyclerView.Adapter<PersonListAdapter.MyViewHolder> {

    // Interface for methods required to be implemented by the object listening to interactions with widgets in the viewholder
    public interface OnItemClickListener {
        void onItemClick(String item);
    }

    private final String[] mDataset; // TODO change to Person[]
    private final OnItemClickListener listener;

    // Provide a suitable constructor (depends on the kind of dataset)
    public PersonListAdapter(String[] myDataset, OnItemClickListener mylistener) {
        this.mDataset = myDataset;
        this.listener = mylistener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view ('row' of the recycler which contains the person_row.xml widgets)
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.person_row, parent, false);
        return new MyViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // put data into the widget and add interaction
        holder.bind(mDataset[position], listener);
    }

    // Return the size of dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    // NOW THE INTERNAL CLASS FOR VIEWHOLDER

    // Viewholder contains the widgets required for each "row" or item in the recyclerview
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        // each data item is just a string in this case, shown in a TextView
        public TextView mTextView;

        public MyViewHolder(View v) {
            super(v);
            mTextView = v.findViewById(R.id.personTextView);
        }

        // requested by the layout manager via the Adapter to instantiate rows in the recyclerview
        public void bind(final String item, final OnItemClickListener listener) {
            mTextView.setText(item);
            mTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }

    } // end inner class (viewholder)

} // end outer class (adapter)
