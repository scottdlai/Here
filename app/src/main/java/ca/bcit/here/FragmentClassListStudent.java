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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class FragmentClassListStudent extends Fragment {

    FirebaseFirestore db;
    private FragmentManager fragmentManager;
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
        Button addBtn = view.findViewById(R.id.addBtn);
        Button joinBtn = view.findViewById(R.id.joinBtn);
        fragmentManager = getFragmentManager();

        readBundle(getArguments());
        ClassInfoAdapter adapter = new ClassInfoAdapter(classNames, classTimes, classIds);
        classListRecycler.setAdapter(adapter);

        GridLayoutManager lm = new GridLayoutManager(view.getContext(), 1);
        classListRecycler.setLayoutManager(lm);

        adapter.setListener(new ClassInfoAdapter.Listener() {

            public void onClick(String classId) {

                Bundle bundle = new Bundle();

                Log.e(TAG, classId + "THIS IS CLASS ID");

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

    private void showJoinDialog( View view) {
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
    private void searchClass(String name){
        Log.e(TAG, "Does it get here");
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout,searchClassFragment.newInstance(name)).commitAllowingStateLoss();;
    }

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
    private void updateReading(String id, Date date, String className) {
        db = FirebaseFirestore.getInstance();

        Map<String,Object> course = new HashMap<>();
        course.put("Name",className);
        course.put("StartDate",new Timestamp(date));
        course.put("Teacher","The boss");
        db.collection("Courses").document().set(course)
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
    }

}