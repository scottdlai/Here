package ca.bcit.here;

import android.app.AlertDialog;
import android.app.Dialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class SessionInfoAdapter extends RecyclerView.Adapter<SessionInfoAdapter.ViewHolder> {
    private String[] sessionDate;
    private String[] sessionTimeStart;
    private String[] sessionTimeEnd;
    private String[] sessionRatio;
    private String[] absentees;
    private String[] lateComers;
    private String[] onTime;

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Pops up
                    showDialog();
                }
            });
        }
        
        private void showDialog() {
            final Dialog dialog = new Dialog(cardView.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.absent_late_list);

            Spinner lateSpinner = dialog.findViewById(R.id.late_spinner);

            Spinner absenteeSpinner = dialog.findViewById(R.id.absentee_spinner);

            ArrayAdapter<String> lateAdapter =
                    new ArrayAdapter<>(cardView.getContext(),
                            R.layout.absent_late_list,
                            lateComers);

            lateSpinner.setAdapter(lateAdapter);

            dialog.show();
        }
    }

    @Override
    public int getItemCount() {
        return sessionDate.length;
    }

    public SessionInfoAdapter(
            String[] sessionDate,
            String[] sessionTimeStart,
            String[] sessionTimeEnd,
            String[] sessionRatio,
            String[] absentees,
            String[] lateComers,
            String[] onTime) {
        this.sessionDate = sessionDate;
        this.sessionTimeStart = sessionTimeStart;
        this.sessionTimeEnd = sessionTimeEnd;
        this.sessionRatio = sessionRatio;
        this.absentees = absentees;
        this.lateComers = lateComers;
        this.onTime = onTime;
    }

    @Override
    public SessionInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_session, parent, false);

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;

        // to set TextView's text with data array
        TextView textView_date = cardView.findViewById(R.id.sessionDate);
        textView_date.setText(sessionDate[position]);

        TextView TextView_timeStart = cardView.findViewById(R.id.sessionTimeStart);
        TextView_timeStart.setText(sessionTimeStart[position]);

        TextView textView_timeEnd = cardView.findViewById(R.id.sessionTimeEnd);
        textView_timeEnd.setText(sessionTimeEnd[position]);

        TextView TextView_ratio = cardView.findViewById(R.id.sessionRatio);
        TextView_ratio.setText(sessionRatio[position]);
    }
}
