package ca.bcit.here;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private FragmentClassListStudent fragmentClassListStudent = new FragmentClassListStudent();
    private FragmentEnterCode fragmentEntercode = new FragmentEnterCode();
    private FragmentNotification fragmentNotification = new FragmentNotification();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragmentClassListStudent).commitAllowingStateLoss();

        BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());
    }

    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            switch(menuItem.getItemId())
            {
                case R.id.menu_bottom_1:
                    transaction.replace(R.id.frameLayout, fragmentClassListStudent).commitAllowingStateLoss();
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