package ca.bcit.here;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class EnterCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);


    }

    public void onClickListTeacher(View view) {
        Intent i = new Intent(this, ClassListTeacher.class);
        startActivity(i);
    }
}