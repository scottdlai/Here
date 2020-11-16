package ca.bcit.here;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

class CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder>

{
    private String[] names;
    private String[] times;
    private String[] ids;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;


        public ViewHolder(CardView v) {

            super(v);

            cardView = v;

        }

    }


    public CaptionedImagesAdapter(String[] names, String[] times, String[] ids) {

        this.names = names;

        this.times = times;
        this.ids = ids;


    }


    @Override

    public int getItemCount() {

        return names.length;

    }


    @Override

    public CaptionedImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_class, parent, false);

        return new ViewHolder(cv);

    }
    private Listener listener;

    interface Listener {

        void onClick(String foodName);

    }


    public void setListener(Listener listener) {

        this.listener = listener;

    }

    @Override

    public void onBindViewHolder(ViewHolder holder, final int position) {

        final CardView cardView = holder.cardView;



        TextView textView = cardView.findViewById(R.id.className);

        textView.setText(names[position]);

        TextView timeTextView = cardView.findViewById(R.id.classTime);

        timeTextView.setText(times[position]);
        Log.e(TAG, ids[position]);

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
