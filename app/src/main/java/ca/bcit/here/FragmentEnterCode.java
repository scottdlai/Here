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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static android.content.ContentValues.TAG;

public class FragmentEnterCode extends Fragment {
    FirebaseFirestore db;
    Button attendBtn;
    EditText codeText;
    Spinner classSpinner;
    List<String> spinnerArrayNames = new ArrayList<>();
    List<String> spinnerArrayIds = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.fragment_enter_code, container, false);
        attendBtn = view.findViewById(R.id.submitCodeBtn);
        codeText = view.findViewById(R.id.editTextCode);
        classSpinner = view.findViewById(R.id.classChoiceSpinner);

        db = FirebaseFirestore.getInstance();
        String userId = "Rj822fFLTjyyOYT4dij0";
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

    private void attend(){
        final String userCode = codeText.getText().toString();
        //Get this key from the Course reference in the student that was selected
        String courseKey = "2SyBpNctfI2X6GZeAIYC";
        //Find the section key from a live session
        String sessionKey = "Qi2hLg3ISvoF9ePgtBsL";

        DocumentReference sessionRef = db.collection("Courses")
                                            .document(courseKey)
                                            .collection("Session")
                                            .document(sessionKey);

        sessionRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    // Document found in the offline cache
                    DocumentSnapshot document = task.getResult();
                    if (userCode.equals(document.getData().get("Code"))){
                        Log.d(TAG,"Matches");
                        //Popup a success toast or something.
                        //Update the session data adding the student.
                    }
                    else{
                        Log.d(TAG,"Doesn't");
                    }

                } else {
                    Log.d(TAG, "Cached get failed: ", task.getException());
                }
            }
        });






    }







}