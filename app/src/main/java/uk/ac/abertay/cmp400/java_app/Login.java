package uk.ac.abertay.cmp400.java_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

public class Login extends AppCompatActivity implements View.OnClickListener {

    //firebase Authentication
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //values
    boolean showInfoPage;

    //views
    EditText mEmail, mPassword;
    Button mloginBtn;

    DocumentReference documentReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //hide Action Bar
        this.getSupportActionBar().hide();

        //get auth
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();

        //check if user is already logged in.
        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), DisplayScreen.class));
        }

        mEmail = findViewById(R.id.LoginEmailTextView);
        mPassword = findViewById(R.id.LoginPasswordTextView);
        mloginBtn = findViewById(R.id.LoginButton);
    }

    @Override
    protected void onStart() {
        mloginBtn.setOnClickListener(view -> {
            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

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

            //authenticate user

             fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    userID = fAuth.getCurrentUser().getUid();
                    documentReference = fStore.collection("users").document(userID);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            showInfoPage = documentSnapshot.getBoolean("ShowInfoPage");
                            ShowInfoPageCheck(showInfoPage);
                        }
                    });
                }else{
                    Toast.makeText(Login.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        });
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void ShowInfoPageCheck(Boolean bool){
        Intent intent;
        if(bool){
            intent = new Intent(getApplicationContext(), InfoPage.class);
        }else{
            intent = new Intent(getApplicationContext(), HomeScreen.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View view){
        if (view.getId() == R.id.SignUpTextView) {
            openRegisterPage();
        }
    }

    public void openRegisterPage(){
        Intent intent = new Intent(this, Register.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}