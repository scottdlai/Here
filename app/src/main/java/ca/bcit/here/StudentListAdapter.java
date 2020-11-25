package ca.bcit.here;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {
    private String[] studentList;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {

            super(v);

            cardView = v;

        }

    }

    @Override

    public int getItemCount() {

        return studentList.length;

    }

    public StudentListAdapter(String[] studentList) {
        this.studentList = studentList;
    }

    @Override

    public StudentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_student, parent, false);

        return new ViewHolder(cv);

    }

    @Override

    public void onBindViewHolder(ViewHolder holder, final int position) {

        final CardView cardView = holder.cardView;

        TextView textView_date = cardView.findViewById(R.id.studentName);

        textView_date.setText(studentList[position]);

    }
}
