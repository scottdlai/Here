package ca.bcit.here;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

/**
 * An Adapter for 'ClassInfo Fragment' to display as a Recycler View using Card View
 * */
class ClassInfoAdapter extends RecyclerView.Adapter<ClassInfoAdapter.ViewHolder> {
    /** to store names of classes */
    private String[] names;

    /** to store times/dates of classes */
    private String[] times;

    /** to store ids of classes */
    private String[] ids;

    /** ViewHolder is the Inner class of this Adapter class that extends Recycler View's ViewHolder */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        /** This ViewHolder contains a CardView object */
        private CardView cardView;

        /**
         * ViewHolder Constructor
         * @param v, CardView object
         */
        public ViewHolder(CardView v) {
            /**
             * to construct and initialize Parent objects
             */
            super(v);

            cardView = v;

        }

    }

    /**
     * ClassInfoAdapter constructor
     * @param names, String array
     * @param times, String array
     * @param ids, String array
     */
    public ClassInfoAdapter(String[] names, String[] times, String[] ids) {
        this.names = names;
        this.times = times;
        this.ids = ids;
    }

    /**
     * gets the count for total items
     * this needs to be overridden as extending RecyclerView.Adapter<T>
     * @return the total length of any items
     */
    @Override
    public int getItemCount() {
        return names.length;
    }

    /**
     * onCreateViewHolder to create ViewHolder of ClassInfoAdapter
     * @param parent, ViewGroup
     * @param viewType, int
     * @return new ViewHolder, a new instantiation of ViewHolder object that takes a CardView as a
     * constructor param
     */
    @Override
    public ClassInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_class, parent, false);

        return new ViewHolder(cv);

    }

    /** Listener Object to implement */
    private Listener listener;

    /**
     * implement an interface of Listener
     */
    interface Listener {
        /** this interface has a abstract method */
        void onClick(String className);
    }

    /**
     * Initializes a listener
     * @param listener, Listener
     */
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Called by RecyclerView to display the data at the specified position.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final CardView cardView = holder.cardView;

        // sets TextView's text with class names by position
        TextView textView = cardView.findViewById(R.id.className);
        textView.setText(names[position]);

        // sets TextView's text with class times by position
        TextView timeTextView = cardView.findViewById(R.id.classTime);
        timeTextView.setText(times[position]);

        // to test which id is selected by the position clicked
        // to print out the actual id's of the clicked CardView
        Log.e(TAG, ids[position]);

        // to set CardViews in this ViewHolder on a click listener
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null) {
                    listener.onClick(ids[position]);
                }
            }
        });
    }

}
