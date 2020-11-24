package ca.bcit.here;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class addClassAdapter extends RecyclerView.Adapter<addClassAdapter.ViewHolder>

{
    FirebaseFirestore db;
    private String[] names;
    private String[] teachers;
    private String[] ids;
    private String[] times;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {

            super(v);

            cardView = v;

        }

    }


    public addClassAdapter(String[] names, String[] teachers, String[] times,String[] ids) {

        this.names = names;
        this.times = times;
        this.teachers = teachers;
        this.ids = ids;


    }


    @Override

    public int getItemCount() {

        return names.length;

    }


    @Override

    public addClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_add_class, parent, false);

        return new addClassAdapter.ViewHolder(cv);

    }
    private ClassInfoAdapter.Listener listener;

    interface Listener {

        void onClick(String foodName);

    }


    public void setListener(ClassInfoAdapter.Listener listener) {

        this.listener = listener;

    }

    @Override

    public void onBindViewHolder(addClassAdapter.ViewHolder holder, final int position) {

        final CardView cardView = holder.cardView;

        TextView textView = cardView.findViewById(R.id.className);

        textView.setText(names[position]);

        TextView teacherTextView = cardView.findViewById(R.id.classTeacher);

        teacherTextView.setText(teachers[position]);

        TextView timeTextView = cardView.findViewById(R.id.classTime);
        timeTextView.setText(times[position]);

        cardView.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                String userId = "Rj822fFLTjyyOYT4dij0";

                    db = FirebaseFirestore.getInstance();

                    Map<String,Object> course = new HashMap<>();
                    course.put("className",names[position]);
                    course.put("classTime",times[position]);
                    course.put("Teacher",teachers[position]);
                    db.collection("users").document(userId).collection("Classes").document(ids[position]).set(course)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                }
                            });

                    Map<String,Object> data = new HashMap<>();
                    Map<String,String> entry =new HashMap<>();


                    String username = "Sean";
                    entry.put(userId,username);
                    data.put("Students",entry);
                Log.e(TAG, data.toString());
                    db.collection("Courses").document(ids[position]).set(data, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w(TAG, "Error writing document", e);
                            }
                        });
                cardView.setVisibility(View.GONE);
            }



        });

    }

}

