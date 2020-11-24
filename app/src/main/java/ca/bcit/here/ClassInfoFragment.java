package ca.bcit.here;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

import static android.content.ContentValues.TAG;

public class ClassInfoFragment extends Fragment {
    FirebaseFirestore db;
    DocumentReference classRef;

    private String classId;

    View view;

    TextView courseName_text;
    TextView courseTime_text;


    public static ClassInfoFragment newInstance(String id) {
        Bundle bundle = new Bundle();

        bundle.putString("id",id);

        ClassInfoFragment fragment = new ClassInfoFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle (Bundle bundle) {
        if (bundle != null) {
            classId = bundle.getString("id");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_class_info, container, false);

        courseName_text = view.findViewById(R.id.courseName);
        courseTime_text = view.findViewById(R.id.courseTime);

        readBundle(getArguments());
        Log.e(TAG, classId);

        db = FirebaseFirestore.getInstance();
        classRef = db.collection("Courses")
                .document(classId);

        classRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String name;
                String time;

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        name = (String) document.getString("Name");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        time = sdf.format(document.getTimestamp("StartDate").toDate());

                        courseName_text.setText(name);
                        courseTime_text.setText(time);
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }

        });



        return view;
    }

}