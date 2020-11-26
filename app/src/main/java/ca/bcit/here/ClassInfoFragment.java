package ca.bcit.here;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class ClassInfoFragment extends Fragment implements View.OnClickListener {
    FirebaseFirestore db;
    DocumentReference classRef;
    DocumentReference studentListRef;
    CollectionReference sessionListRef;

    private String classId;

    View view;

    TextView courseName_text;
    TextView courseTime_text;
    Button btnSession;
    Button btnStudent;
    Button btnNewSession;


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

        btnSession = view.findViewById(R.id.btnSeeSession);
        btnStudent = view.findViewById(R.id.btnSeeStudent);
        btnNewSession = view.findViewById(R.id.newSessionBtn);

        btnSession.setOnClickListener(this);
        btnStudent.setOnClickListener(this);
        btnNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSession();
            }
        });

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
    public void startSession(){

//        Put a new session at this time into the database.
        Map<String,Object> data = new HashMap<>();
        data.put("Date",new Timestamp(Calendar.getInstance().getTime()));
        data.put("Late",new LinkedList<String>());
        data.put("OnTime",new LinkedList<String>());
        CollectionReference cr = db.collection("Courses").document(classId).collection("Session");
        cr.add(data);
        Toast.makeText(getActivity(), "Session Created",
                Toast.LENGTH_SHORT).show();
        final FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, CodeGenerator.newInstance(classId) ).commitNowAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        // database
        db = FirebaseFirestore.getInstance();

        switch (v.getId()) {

            case R.id.btnSeeSession:
                sessionListRef = db.collection("Courses").document(classId).collection("Session");

                sessionListRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<String> sessionDate = new ArrayList<>();
                        ArrayList<String> sessionTimeStart = new ArrayList<>();
                        ArrayList<String> sessionTimeEnd = new ArrayList<>();
                        ArrayList<String> sessionRatio = new ArrayList<>();

                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()){

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                sessionDate.add(sdf.format(document.getTimestamp("Date").toDate()));

                                sdf = new SimpleDateFormat("HH:mm");
                                sessionTimeStart.add(sdf.format(document.getTimestamp("Date").toDate()));

                                sessionTimeEnd.add((String) document.getData().get("TimeEnd"));
                                sessionRatio.add(String.valueOf(document.getData().get("ratioPresent")));

                            }
                            Log.e(TAG, sessionDate.toString());
                            Log.e(TAG, sessionTimeStart.toString());
                            Log.e(TAG, sessionTimeEnd.toString());
                            Log.e(TAG, sessionRatio.toString());
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }

                        transaction.replace(R.id.frameLayout, SessionFragment.newInstance(sessionDate.toArray(new String[sessionDate.size()]), sessionTimeStart.toArray(new String[sessionTimeStart.size()]), sessionTimeEnd.toArray(new String[sessionTimeEnd.size()]), sessionRatio.toArray(new String[sessionRatio.size()]))).commitNowAllowingStateLoss();

                    }
                });
                break;

            case R.id.btnSeeStudent:
                studentListRef = db.collection("Courses").document(classId);
                studentListRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<String> names = new ArrayList<>();
                        Map<String, String> students;

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document != null && document.exists()) {

                                students = (Map) document.getData().get("Students");

                                for (String name : students.values()) {
                                    names.add(name);
                                }

                                Log.e(TAG, students.toString() + "Map Students");
                                Log.e(TAG, names.toString() + "ArrayList Students");

                            } else {
                                Log.d("LOGGER", "No such document");
                            }
                        } else {
                            Log.d("LOGGER", "get failed with ", task.getException());
                        }

                        transaction.replace(R.id.frameLayout, StudentListFragment.newInstance(names.toArray(new String[names.size()]))).commitNowAllowingStateLoss();

                    }
                });

                break;

        }




    }
}