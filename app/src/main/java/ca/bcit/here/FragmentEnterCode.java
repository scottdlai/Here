package ca.bcit.here;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A fragment to enter a code for a class to be taken as attending.
 */
public class FragmentEnterCode extends Fragment {
    FirebaseFirestore db;
    Button attendBtn;
    EditText codeText;
    Spinner classSpinner;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userId;
    String username;
    List<String> spinnerArrayNames = new ArrayList<>();
    List<String> spinnerArrayIds = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_enter_code, container, false);
        attendBtn = view.findViewById(R.id.submitCodeBtn);
        codeText = view.findViewById(R.id.editTextCode);
        classSpinner = view.findViewById(R.id.classChoiceSpinner);
        spinnerArrayNames.clear();
        spinnerArrayIds.clear();

        //Finds a list of the classes that the user has and adds them to the list of classes in the spinner.
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        userId = user.getUid();
        CollectionReference docRef = db.collection("users").document(userId).collection("Classes");
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                spinnerArrayNames.add(document.getString("className"));
                                spinnerArrayIds.add(document.getId());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_spinner_item, spinnerArrayNames);
                            classSpinner.setAdapter(adapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });



        attendBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                attend();
            }
        });
        return view;
    }

    /**
     * Does all the work for the code logic
     */
    private void attend(){
        final String userCode = codeText.getText().toString();
        //Get this key from the Course reference in the student that was selected
        final String courseKey = spinnerArrayIds.get(classSpinner.getSelectedItemPosition());
        //Find the section key from a live session
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,(-1));
        Timestamp hourBefore = new Timestamp(cal.getTime());
        final Query sessionRef = db.collection("Courses")
                                            .document(courseKey)
                                            .collection("Session").whereGreaterThan("Date",hourBefore);

        //Grabs the document from the database and checks if the code was the.
        DocumentReference docRef  = db.collection("Courses").document(courseKey);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot document = task.getResult();

                if (userCode.equals(document.getData().get("Code"))){

                    //if it was it check for all the sessions that are still available.
                    Log.d(TAG,"Matches");
                    //Popup a success toast or something.
                    //Update the session data adding the student.
                    db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    codeText.setText("");
                                    username = document.getString("username");


                                    sessionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                // Document found in the offline cache
                                                for (QueryDocumentSnapshot document : task.getResult()) {
                                                    Log.d(TAG, document.getId() + " => " + document.getData());


//                                                    Check if the user is already in the list.
                                                    List<String> onTimeList = (List<String>) document.get("OnTime");
                                                    List<String> lateList = (List<String>) document.get("Late");
                                                    //Checks if the user has already join this session before.
                                                    if (!onTimeList.contains(username) && !lateList.contains(username)) {

    //                                                  Check if the time is within the last 10 mins.
                                                        Calendar cal = Calendar.getInstance();
                                                        cal.set(Calendar.MINUTE, (-10));
                                                        Timestamp tenBefore = new Timestamp(cal.getTime());
                                                        DocumentReference sessionDocRef = db.collection("Courses")
                                                                .document(courseKey)
                                                                .collection("Session").document(document.getId());
                                                        Map<String, Object> data = new HashMap<>();
                                                        List<String> name = new LinkedList<>();
                                                        name.add(username);
                                                        //Checks if the sessions is less than ten minutes old and will add the user to on time is it is
                                                        //Else it will be added to the Late list.
                                                        if (document.getTimestamp("Date").compareTo(tenBefore) > 0) {
                                                            data.put("OnTime", name);
                                                            Toast.makeText(getActivity(), "Attendance taken",
                                                                    Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            data.put("Late", name);
                                                            Toast.makeText(getActivity(), "Attendance taken as late",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                        data.put("Attended", onTimeList.size() + lateList.size() +1);

                                                        sessionDocRef.set(data, SetOptions.merge());

                                                    }
                                                    else {
                                                        //Display some messages to the user depending on the status of the session and code.
                                                        Toast.makeText(getActivity(), "You have already attended this session.",
                                                                Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            } else {
                                                Log.d(TAG, "Cached get failed: ", task.getException());
                                                Toast.makeText(getActivity(), "That code is too old",
                                                        Toast.LENGTH_SHORT).show();
                                            }
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


                }
                else{
                    Toast.makeText(getActivity(), "Incorrect Code",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}