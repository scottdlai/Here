package ca.bcit.here;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore db;
    CollectionReference classListRef;
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private FragmentClassListStudent fragmentClassListStudent = new FragmentClassListStudent();
    private FragmentEnterCode fragmentEntercode = new FragmentEnterCode();
    private FragmentNotification fragmentNotification = new FragmentNotification();

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String userKey = "Rj822fFLTjyyOYT4dij0";

        db = FirebaseFirestore.getInstance();
        classListRef = db.collection("users")
                .document(userKey)
                .collection("Classes");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentEntercode).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
        bottomNavigationView.setSelectedItemId(R.id.menu_bottom_2);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            final FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.menu_bottom_1:
                    classListRef.get()
                            .addOnCompleteListener( new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    ArrayList<String> classNames = new ArrayList<>();
                                    ArrayList<String> classTimes = new ArrayList<>();
                                    ArrayList<String> classIds = new ArrayList<>();
                                    if (task.isSuccessful()) {
                                        for(QueryDocumentSnapshot document : task.getResult()){

                                            classNames.add((String) document.getData().get("className"));
                                            classTimes.add((String) document.getData().get("classTime"));
                                            classIds.add(document.getId().trim());
                                        }
                                    } else {
                                        Log.d(TAG, "Cached get failed: ", task.getException());
                                    }

                                    transaction.replace(R.id.frameLayout, FragmentClassListStudent.newInstance(classNames.toArray(new String[classNames.size()]), classTimes.toArray(new String[classNames.size()]), classIds.toArray(new String[classNames.size()]))).commitNowAllowingStateLoss();
                                }
                            });
                    break;
                case R.id.menu_bottom_2:
                    transaction.replace(R.id.frameLayout, fragmentEntercode).commitAllowingStateLoss();
                    break;
                case R.id.menu_bottom_3:
                    transaction.replace(R.id.frameLayout, fragmentNotification).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }
}