package ca.bcit.here;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

/**
 * Activity for signing up the users.
 */
public class SignUpActivity extends AppCompatActivity {

    /** Collection name of the users collection in Firebase. */
    private static final String USERS_COLLECTION = "users";

    /** Reference to Firestore. */
    private FirebaseFirestore db;

    /** Firebase authentication instance. */
    private FirebaseAuth mAuth;

    /** Edit text to enter the name. */
    private EditText nameField;

    /** Edit text to enter the email. */
    private EditText emailField;

    /** Edit text to enter the password. */
    private EditText passwordField;

    /** Sign up button. */
    private Button signUpBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        // Initialize all the field and instances of Firebase.
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

    /**
     * Signs up the email, password and name.
     */
    private void signUp() {
        final String name = nameField.getText().toString().trim();

        if (name.isEmpty()) {
            nameField.setError("Can't have empty name");
            return;
        }

        String email = emailField.getText().toString().trim();

        if (email.isEmpty()) {
            emailField.setError("Can't have empty email");
            return;
        }

        String password = passwordField.getText().toString().trim();

        if (password.isEmpty()) {
            passwordField.setError("Can't have empty password");
            return;
        }

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