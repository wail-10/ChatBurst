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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import ma.enset.chatburst.models.UserModel;

public class SignupActivity extends AppCompatActivity {
    EditText userEmail, userPassword, userName;
    TextView signupBtn, signinBtn;
    String email, password, name;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        userEmail = findViewById(R.id.email);
        userPassword = findViewById(R.id.password);
        userName = findViewById(R.id.username);
        signupBtn = findViewById(R.id.signup);
        signinBtn = findViewById(R.id.signin);

        signupBtn.setOnClickListener(v -> {
            name = userName.getText().toString().trim();
            email = userEmail.getText().toString().trim();
            password = userPassword.getText().toString().trim();
            if(TextUtils.isEmpty(name)){
                userName.setError("Name is required");
                userName.requestFocus();
                return;
            }
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
            Signup();
        });

        signinBtn.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, SigninActivity.class));
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            startActivity(new Intent(SignupActivity.this, MainActivity.class));
            finish();
        }
    }

    private void Signup() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//                        FirebaseUser.updateProfile(userProfileChangeRequest);
                        user.updateProfile(userProfileChangeRequest);
                        UserModel userModel = new UserModel(FirebaseAuth.getInstance().getUid(), name, email, password);
                        databaseReference.child(FirebaseAuth.getInstance().getUid()).setValue(userModel);
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        intent.putExtra("name", name);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignupActivity.this, "Signup failed for some reasons", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}