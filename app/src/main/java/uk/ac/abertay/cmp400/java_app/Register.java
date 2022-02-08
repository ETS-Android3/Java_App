package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity implements View.OnClickListener{

    EditText mUsername, mEmail, mPassword, mCnfPassword;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //assign values
        mUsername = findViewById(R.id.UsernameTextBox);
        mEmail = findViewById(R.id.EmailTextBox);
        mPassword = findViewById(R.id.PasswordTextBox);
        mCnfPassword = findViewById(R.id.ConfirmPasswordTextBox);
        mRegisterBtn = findViewById(R.id.RegisterButton);
        mLoginBtn = findViewById(R.id.LoginButton);
        progressBar = findViewById(R.id.progressBar);

        //Firebase Auth
        fAuth = FirebaseAuth.getInstance();

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
            finish();
        }

        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                String ConfirmPassword = mCnfPassword.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required");
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must be grater than 5 characters.");
                    return;
                }
                if (!password.equals(ConfirmPassword)){
                    mCnfPassword.setError("Password dose not match.");
                    return;
                }
                if (mUsername.length() < 5){
                    mUsername.setError("Username must be more that 4 characters.");
                }

                progressBar.setVisibility(View.VISIBLE);

                fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this, "User Created", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), HomeScreen.class));
                        }else{
                            Toast.makeText(Register.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });


            }
        });

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.SignInTextView:
                openLoginPage();
                break;
        }
    }

    public void openLoginPage(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}