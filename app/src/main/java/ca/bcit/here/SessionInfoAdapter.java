package ca.bcit.here;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class SessionInfoAdapter extends RecyclerView.Adapter<SessionInfoAdapter.ViewHolder> {
    private String[] sessionDate;
    private String[] sessionTimeStart;
    private String[] sessionTimeEnd;
    private String[] sessionRatio;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {

            super(v);

            cardView = v;

        }

    }

    @Override

    public int getItemCount() {

        return sessionDate.length;

    }

    public SessionInfoAdapter(String[] sessionDate, String[] sessionTimeStart, String[] sessionTimeEnd, String[] sessionRatio) {
        this.sessionDate = sessionDate;
        this.sessionTimeStart = sessionTimeStart;
        this.sessionTimeEnd = sessionTimeEnd;
        this.sessionRatio = sessionRatio;

    }

    @Override

    public SessionInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_session, parent, false);

        return new ViewHolder(cv);

    }

//    private Listener listener;
//
//    interface Listener {
//
//        void onClick(String foodName);
//
//    }
//
//
//    public void setListener(Listener listener) {
//
//        this.listener = listener;
//
//    }

    @Override

    public void onBindViewHolder(ViewHolder holder, final int position) {

        final CardView cardView = holder.cardView;



        TextView textView_date = cardView.findViewById(R.id.sessionDate);

        textView_date.setText(sessionDate[position]);

        TextView TextView_timeStart = cardView.findViewById(R.id.sessionTimeStart);

        TextView_timeStart.setText(sessionTimeStart[position]);

        TextView textView_timeEnd = cardView.findViewById(R.id.sessionTimeEnd);

        textView_timeEnd.setText(sessionTimeEnd[position]);

        TextView TextView_ratio = cardView.findViewById(R.id.sessionRatio);

        TextView_ratio.setText(sessionRatio[position]);


//        cardView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//
//            public void onClick(View view) {
//
//                if (listener != null) {
//
//                    listener.onClick(ids[position]);
//
//
//                }
//
//
//            }
//
//        });


    }
}
