package ca.bcit.here;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

//import android.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * The fragment that displays the info for the classes that the user is part of
 *  Can also make new classes and join classes from this fragment.
 */
public class FragmentClassListStudent extends Fragment {

    FirebaseFirestore db;
    private FragmentManager fragmentManager;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String[] classNames;
    private String[] classTimes;
    private String[] classIds;

    public static FragmentClassListStudent newInstance(String[] classes, String[] times, String[] ids) {
        Bundle bundle = new Bundle();
        bundle.putStringArray("classes",classes);
        bundle.putStringArray("times",times);
        bundle.putStringArray("ids",ids);

        FragmentClassListStudent fragment = new FragmentClassListStudent();
        fragment.setArguments(bundle);

        return fragment;
    }

    private void readBundle(Bundle bundle) {
        if (bundle != null) {
            classNames = bundle.getStringArray("classes");
            classTimes = bundle.getStringArray("times");
            classIds = bundle.getStringArray("ids");
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_class_list_student, container, false);
        RecyclerView classListRecycler = view.findViewById(R.id.classListRecycler);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Button addBtn = view.findViewById(R.id.addBtn);
        Button joinBtn = view.findViewById(R.id.joinBtn);
        fragmentManager = getFragmentManager();


        readBundle(getArguments());
        //Set the adapter to display the data in the recycler.
        ClassInfoAdapter adapter = new ClassInfoAdapter(classNames, classTimes, classIds);
        classListRecycler.setAdapter(adapter);

        GridLayoutManager lm = new GridLayoutManager(view.getContext(), 1);
        classListRecycler.setLayoutManager(lm);

        adapter.setListener(new ClassInfoAdapter.Listener() {

            public void onClick(String classId) {

                Bundle bundle = new Bundle();

                Log.e(TAG, classId + " THIS IS CLASS ID, in FragmentClassListStudent OnCreateView");
                //Start up the class view fragment with the data of the clicked class.
                bundle.putString("id", classId);

                ClassInfoFragment classInfoFrag = ClassInfoFragment.newInstance(classId);

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack if needed
                transaction.replace(R.id.frameLayout, classInfoFrag);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }

        });


        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showJoinDialog(v);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog("",v);
            }
        });


        return view;
    }

    /**
     * Shows a dialog box for searching a class to join.
     * @param view as the view
     */
    private void showJoinDialog( View view) {
        //Build the view of the alert box.
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.join_dailog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextClassName = dialogView.findViewById(R.id.classNameEditText);

        final Button btnJoin = dialogView.findViewById(R.id.searchClassBtn);

        dialogBuilder.setTitle("Search for a class name");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets the name of the inputed text and sends the user to another activity
                String name = editTextClassName.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    editTextClassName.setError("A Class name is required");
                    return;
                }
                searchClass(name);
                alertDialog.dismiss();
            }
        });


    }

    /**
     * Starts a fragment to display the classes.
     * @param name of the class
     */
    private void searchClass(String name){
        //Sends the user to the fragment that will display the list of classes that they can choose.
        Log.e(TAG, "Does it get here");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout,searchClassFragment.newInstance(name)).commitAllowingStateLoss();;
    }

    /**
     * Shows a dialog box where you can make a new class
     * @param readingId
     * @param view
     */
    private void showAddDialog(final String readingId, View view) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(view.getContext());

        LayoutInflater inflater = getLayoutInflater();

        final View dialogView = inflater.inflate(R.layout.add_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.addClassName);

        final DatePicker classDate = dialogView.findViewById(R.id.classDatePicker);

        final Button btnAdd = dialogView.findViewById(R.id.btnAddClass);

        dialogBuilder.setTitle("Make a new Class");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                Calendar cal = Calendar.getInstance();
                cal.set(classDate.getYear(),classDate.getMonth(),classDate.getDayOfMonth());


                if (TextUtils.isEmpty(name)) {
                    editTextName.setError("A Class name is required");
                    return;
                }

                updateReading(readingId, cal.getTime(), name);

                alertDialog.dismiss();
            }
        });


    }

    /**
     * Does all the setting of values in the database
     * @param id of the user
     * @param date of the creation
     * @param className of the class.
     */
    private void updateReading(String id, final Date date, final String className) {
        db = FirebaseFirestore.getInstance();
        final String userId = user.getUid();

        //Get the username from the users database file.
        db.collection("users").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String username = document.getString("username");
                        Map<String,Object> course = new HashMap<>();
                        course.put("Name", className);
                        course.put("StartDate", new Timestamp(date));
                        course.put("Teacher", username);

                        Map<String,Object> userCourse = new HashMap<>();
                        userCourse.put("className",className);
                        DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
                        userCourse.put("classTime",df.format(date));
                        userCourse.put("Teacher",username);

                        //Make the class in the database.
                        String pathId = db.collection("Courses").document().getId();
                        db.collection("users").document(userId).collection("Classes").document(pathId).set(userCourse)
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



                        db.collection("Courses").document(pathId).set(course)
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

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });





    }

}