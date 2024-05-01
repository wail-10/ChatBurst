package ma.enset.chatburst;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity {
    EditText userEmail, userPassword;
    TextView signinBtn, signupBtn;
    String email, password;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        userEmail = findViewById(R.id.username);
        userPassword = findViewById(R.id.password);
        signinBtn = findViewById(R.id.signin);
        signupBtn = findViewById(R.id.signup);

        signinBtn.setOnClickListener(v -> {
            email = userEmail.getText().toString().trim();
            password = userPassword.getText().toString().trim();
            if(TextUtils.isEmpty(email)){
                userEmail.setError("Email is required");
                userEmail.requestFocus();
                return;
            }
            if(TextUtils.isEmpty(password)){
                userPassword.setError("Password is required");
                userPassword.requestFocus();
                return;
            }
            Signin();
        });

        signupBtn.setOnClickListener(v -> {
            startActivity(new Intent(SigninActivity.this, SignupActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(SigninActivity.this, MainActivity.class));
            finish();
        }
    }

    private void Signin() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthInvalidUserException){
                            Toast.makeText(SigninActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(SigninActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}