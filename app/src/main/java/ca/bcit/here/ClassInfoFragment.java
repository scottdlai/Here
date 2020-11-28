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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * ClassInfo Fragment Class
 * to display class info page
 * contains an adapter
 */
public class ClassInfoFragment extends Fragment implements View.OnClickListener {
    /** FIREBASE DATA */
    FirebaseFirestore db;

    /** Documents and Collections References*/
    DocumentReference classRef;
    DocumentReference studentListRef;
    CollectionReference sessionListRef;

    /** FIREBASE AUTHENTICATION */
    FirebaseAuth mAuth;

    /** User's primary key */
    String userId;

    /** Class's primary key */
    private String classId;

    /** frame layouts */
    View view;
    TextView courseName_text;
    TextView courseTime_text;
    Button btnSession;
    Button btnStudent;
    Button btnNewSession;


    private int totalNum;

    /**
     * to Create a new instance of ClassInfo Fragment with corresponding data from a user
     * @param id, class's primary key, id
     * @return a new fragment with a corresponding fragment with a class's id
     */
    public static ClassInfoFragment newInstance(String id) {
        // instantiating a new bundle to store / pass down some data
        Bundle bundle = new Bundle();
        bundle.putString("id",id);

        ClassInfoFragment fragment = new ClassInfoFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    /**
     * to read a bundle information and sets classId attribute with passed down data
     * @param bundle
     */
    private void readBundle (Bundle bundle) {
        if (bundle != null) {
            classId = bundle.getString("id");
        }
    }

    /**
     * to Create this Fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * to Create this Fragment's View / Frame Layouts
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return this view with newly set texts
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_class_info, container, false);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();

        // setting the texts of frame layouts
        courseName_text = view.findViewById(R.id.courseName);
        courseTime_text = view.findViewById(R.id.courseTime);

        btnSession = view.findViewById(R.id.btnSeeSession);
        btnStudent = view.findViewById(R.id.btnSeeStudent);
        btnNewSession = view.findViewById(R.id.newSessionBtn);

        // to set buttons on click listeners
        btnSession.setOnClickListener(this);
        btnStudent.setOnClickListener(this);
        btnNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSession();
            }
        });

        readBundle(getArguments());

        // to test out to print out a classId that is passed down
        Log.e(TAG, classId);


        // to find some data that is corresponding to the classId
        db = FirebaseFirestore.getInstance();
        classRef = db.collection("Courses")
                .document(classId);

        // FIREBASE OPERATIONS
        classRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                String name;
                String time;

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        name = (String) document.getString("Name");

                        // to set date format
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

        // Gets a size of Student Map and sets it to 'totalNum' attribute
        setTotalStudentNumber();

        return view;
    }

    /**
     * Called by the start Session button and adds a new session to the database
     * and sends the user to the code generator fragment to get a code for the session.
     */
    public void startSession(){

        //Check if the username of the user is the same as the teacherName
        db.collection("users").document(userId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                           @Override
                                           public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                               if (task.isSuccessful()) {
                                                   final DocumentSnapshot document = task.getResult();

                                                   final String username = document.getString("username");


                                                   db.collection("Courses").document(classId)
                                                           .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                       @Override
                                                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                           DocumentSnapshot classDocument = task.getResult();
                                                           Log.d("TAG", "Class ID: " + classId);
                                                           Log.d(TAG, "Class Document: " + document.getData());
                                                           String teacherUsername = classDocument.getString("Teacher");

                                                           Map<String, String> studentsMap = (HashMap<String, String>) classDocument.get("Students");

                                                           List<String> students = new ArrayList<>(studentsMap.values());
                                                           //Is the teacher and user the same?.
                                                            if(teacherUsername != null && teacherUsername.equals(username)){

                                                                //Put a new session at this time into the database.
                                                                Map<String,Object> data = new HashMap<>();
                                                                data.put("Date",new Timestamp(Calendar.getInstance().getTime()));
                                                                data.put("Late",new LinkedList<String>());
                                                                data.put("OnTime",new LinkedList<String>());
                                                                data.put("Absent", students);
                                                                //Send to database.
                                                                CollectionReference cr = db.collection("Courses").document(classId).collection("Session");
                                                                cr.add(data);
                                                                Toast.makeText(getActivity(), "Session Created",
                                                                        Toast.LENGTH_SHORT).show();
                                                                //Send to next fragment.
                                                                final FragmentTransaction transaction = getFragmentManager().beginTransaction();
                                                                transaction.replace(R.id.frameLayout, CodeGenerator.newInstance(classId) )
                                                                        .addToBackStack(null)
                                                                        .commit();
                                                            }
                                                            else{
                                                                //Toast that you dont have permission.
                                                                Toast.makeText(getActivity(), "You don't have permission to start a session in this class.",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }

                                                       }
                                                   });
                                               }
                                           }
                                       });
    }

    /**
     * to Set buttons on click listener
     * @param v, this view, fragment
     */
    @Override
    public void onClick(View v) {
        final FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

        // database
        db = FirebaseFirestore.getInstance();

        // v.getId(), gets the id of frame layout that is clicked
        switch (v.getId()) {

            /* if the session button is clicked
            * retrieve some data corresponding to classId and store them into arrays
            * then pass them down to SessionFragment to instantiate and display a page corresponding
            * to the class and show session information */
            case R.id.btnSeeSession:
                sessionListRef = db.collection("Courses").document(classId).collection("Session");

                sessionListRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        ArrayList<String> sessionDate = new ArrayList<>();
                        ArrayList<String> sessionTimeStart = new ArrayList<>();
                        ArrayList<String> sessionTimeEnd = new ArrayList<>();
                        ArrayList<String> sessionRatio = new ArrayList<>();
                        List<String> sessionAbsentees = new ArrayList<>();
                        List<String> sessionLates = new ArrayList<>();
                        List<String> sessionOnTime = new ArrayList<>();

                        if (task.isSuccessful()) {
                            for(QueryDocumentSnapshot document : task.getResult()){

                                //
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                sessionDate.add(sdf.format(document.getTimestamp("Date").toDate()));

                                sdf = new SimpleDateFormat("HH:mm");
                                sessionTimeStart.add(sdf.format(document.getTimestamp("Date").toDate()));

                                sessionTimeEnd.add((String) document.getData().get("TimeEnd"));

                                long attendedNumber =  (long) document.getData().get("Attended");

                                sessionAbsentees = (List<String>) document.getData().get("absentees");

                                // Null Checking because some document doesn't have this field yet
                                if (sessionAbsentees == null) {
                                    sessionAbsentees = new ArrayList<>();
                                }

                                sessionLates = (List<String>) document.getData().get("late");

                                // Null checking same reason above
                                if (sessionLates == null) {
                                    sessionLates = new ArrayList<>();
                                }

                                sessionOnTime = (List<String>) document.getData().get("onTime");

                                if (sessionOnTime == null) {
                                    sessionOnTime = new ArrayList<>();
                                }

                                Log.e(TAG, attendedNumber + "attendedNumber");
                                Log.e(TAG, totalNum + "totalNum");
                                String ratio = attendedNumber + " / " + totalNum;
                                sessionRatio.add(ratio);

                            }

                            // to test out if data has retrieved correctly
                            Log.e(TAG, sessionDate.toString() + "Session Date to String");
                            Log.e(TAG, sessionTimeStart.toString() + "Session Time Start to String");
                            Log.e(TAG, sessionTimeEnd.toString() + "Session Time End to String");
                            Log.e(TAG, sessionRatio.toString() + "Session Ratio to String");
                        } else {
                            Log.d(TAG, "Cached get failed: ", task.getException());
                        }

                        // By using Fragment Manager to transact or change the Fragment with a new
                        // Fragment
                        transaction.replace(R.id.frameLayout,
                                SessionFragment.newInstance(
                                        sessionDate.toArray(new String[sessionDate.size()]),
                                        sessionTimeStart.toArray(new String[sessionTimeStart.size()]),
                                        sessionTimeEnd.toArray(new String[sessionTimeEnd.size()]),
                                        sessionRatio.toArray(new String[sessionRatio.size()]),
                                        sessionAbsentees.toArray(new String[sessionAbsentees.size()]),
                                        sessionLates.toArray(new String[sessionLates.size()]),
                                        sessionOnTime.toArray(new String[sessionOnTime.size()])))
                                .addToBackStack(null)
                                .commit();


                    }
                });
                break;

            // please refer to above case's comment
            // works similar
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
                                Log.e(TAG, students.size() + "Size of Student Map");

                            } else {
                                Log.d("LOGGER", "No such document");
                            }
                        } else {
                            Log.d("LOGGER", "get failed with ", task.getException());
                        }

                        transaction.replace(R.id.frameLayout,
                                StudentListFragment.newInstance(names.toArray(new String[names.size()])))
                                .addToBackStack(null)
                                .commit();
                    }
                });
                break;
        }
    }

    private void setTotalStudentNumber() {
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
                        totalNum = students.size();

                    } else {
                        Log.d("LOGGER", "No such document");
                    }
                } else {
                    Log.d("LOGGER", "get failed with ", task.getException());
                }

            }
        });
    }

}