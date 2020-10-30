package ca.bcit.here;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.content.ContentValues.TAG;

public class FragmentEnterCode extends Fragment {
    FirebaseFirestore db;
    Button attendBtn;
    EditText codeText;


    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_enter_code, container, false);
        attendBtn = view.findViewById(R.id.submitCodeBtn);
        codeText = view.findViewById(R.id.editTextCode);

        db = FirebaseFirestore.getInstance();
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