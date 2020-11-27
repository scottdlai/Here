package ca.bcit.here;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * An adapter that is for setting up the recycler view for the searching for class fragment
 */
public class addClassAdapter extends RecyclerView.Adapter<addClassAdapter.ViewHolder>

{
    FirebaseFirestore db;
    FirebaseUser user;
    FirebaseAuth mAuth;
    String username;
    String userId;
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
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

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
    /**
     * Sets the values of the cards in the view and adds on click features to them that do the logic for adding someone to a class.
     */
    public void onBindViewHolder(final addClassAdapter.ViewHolder holder, final int position) {
        //Sets the data.
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
                    //Sets the values for the course in the users file.
                 userId = user.getUid();

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


                    //Gets the user document and finds their name too use in adding them to the class list.
                    db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    //Adds the data to a document model.
                                    username = document.getString("username");
                                    Map<String,Object> data = new HashMap<>();
                                    Map<String,String> entry =new HashMap<>();
                                    entry.put(userId,username);
                                    data.put("Students",entry);
                                    Log.e(TAG, data.toString());
                                    //Sets the data inside the database to contain the students name and Uid.
                                    db.collection("Courses").document(ids[position]).set(data, SetOptions.merge())
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(holder.itemView.getContext(), "Class joined",
                                                            Toast.LENGTH_SHORT).show();
                                                    Log.d(TAG, "DocumentSnapshot successfully written!");
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });

                                } else {
                                    Log.d(TAG, "No such document");
                                }
                            } else {
                                Log.d(TAG, "get failed with ", task.getException());
                            }
                        }
                    });



                cardView.setVisibility(View.GONE);
            }



        });

    }

}

