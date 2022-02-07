package uk.ac.abertay.cmp400.java_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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