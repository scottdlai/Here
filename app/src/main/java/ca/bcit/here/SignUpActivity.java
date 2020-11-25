package ca.bcit.here;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private static final String USERS_COLLECTION = "users";

    private FirebaseFirestore db;

    private FirebaseAuth mAuth;

    private EditText nameField;

    private EditText emailField;

    private EditText passwordField;

    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();

        nameField = findViewById(R.id.nameField);

        emailField = findViewById(R.id.emailField);

        passwordField = findViewById(R.id.passwordField);

        signUpBtn = findViewById(R.id.signUpBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        final String name = nameField.getText().toString().trim();

        String email = emailField.getText().toString().trim();

        String password = passwordField.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));

                    // Write to `users` collection
                    // Get signed up userid
                    Map<String, String> data = new HashMap<>();
                    String userID = task.getResult().getUser().getUid();
                    data.put("username", name);
                    db.collection(USERS_COLLECTION)
                        .document(userID).set(data);

                } else {
                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}