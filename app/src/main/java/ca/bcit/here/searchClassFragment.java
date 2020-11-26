package ca.bcit.here;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * A fragment that will search for classes of a specific name and display them to the user.
 */
public class searchClassFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String className;
    FirebaseFirestore db;
    private RecyclerView classListRecycler;

    public searchClassFragment() {
        // Required empty public constructor
    }

    public static searchClassFragment newInstance(String name) {
        searchClassFragment fragment = new searchClassFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, name);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            className = getArguments().getString(ARG_PARAM1);

        }
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();
        View view = inflater.inflate(R.layout.fragment_search_class, container, false);
        classListRecycler = view.findViewById(R.id.classListRecycler);

        //Get all courses with the input name.
        Query query = db.collection("Courses").whereEqualTo("Name",className);
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        ArrayList<String> classNames = new ArrayList<>();
                        ArrayList<String> classTeacher = new ArrayList<>();
                        ArrayList<String> classTimes = new ArrayList<>();
                        ArrayList<String> classIds = new ArrayList<>();
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                classNames.add((String) document.getData().get("Name"));
                                classTeacher.add((String) document.getData().get("Teacher"));
                                DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                                classTimes.add(df.format(document.getTimestamp("StartDate").toDate()));
                                classIds.add(document.getId());

                            }
                        }
                        else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                        addClassAdapter adapter = new addClassAdapter(classNames.toArray(new String[classNames.size()]),
                                classTeacher.toArray(new String[classTeacher.size()]),
                                classTimes.toArray(new String[classTimes.size()]),
                                classIds.toArray(new String[classIds.size()]));
                        //Set the adapter for the view to contain the data from the database.
                        classListRecycler.setAdapter(adapter);
                    }

                });

        GridLayoutManager lm = new GridLayoutManager(view.getContext(), 1);
        classListRecycler.setLayoutManager(lm);
        return view;
    }

}