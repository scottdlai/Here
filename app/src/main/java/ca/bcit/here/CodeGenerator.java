package ca.bcit.here;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Random;

import static android.content.ContentValues.TAG;

public class CodeGenerator extends Fragment {

    FirebaseFirestore db;
    Button generateBtn;
    TextView codeView;
    TextView description;


    public CodeGenerator() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_code_generator, container, false);
        generateBtn = view.findViewById(R.id.CodeGenBtn);
        codeView = view.findViewById(R.id.codeView);
        description = view.findViewById(R.id.GenDesc);
        db = FirebaseFirestore.getInstance();
        generateBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                generateCodes();
            }
        });
        return view;

    }

    private void generateCodes(){
//        long startTime = System.currentTimeMillis();
//        long elapsedTime = System.currentTimeMillis() - startTime;
//        long elapsedSeconds = elapsedTime / 1000;
//        long secondsDisplay = elapsedSeconds % 60;
//        long elapsedMinutes = elapsedSeconds / 60;



        String courseKey = "2SyBpNctfI2X6GZeAIYC";
        String SessionKey = "Qi2hLg3ISvoF9ePgtBsL";

        DocumentReference SessionRef = db.collection("Courses")
                .document(courseKey)
                .collection("Session")
                .document(SessionKey);



        Random rand = new Random();
        String code ="";
        int codeLength = 6;
        for(int i = 0; i <codeLength; i++){
            code += rand.nextInt(10);
        }

        SessionRef.update("Code",code)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        codeView.setText(code);
        generateBtn.setVisibility(View.GONE);
        description.setVisibility(View.GONE);
    }
}