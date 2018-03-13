package com.example.envy13.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText Email;
    private EditText Password;
    private Button Login;
    private Button userRegistration;

    private FirebaseAuth firebaseAuth;
    String TAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = (EditText)findViewById(R.id.etEmail);
        Password = (EditText)findViewById(R.id.etPassword);
        Login = (Button)findViewById(R.id.btnLogin);
        userRegistration = (Button)findViewById(R.id.tvRegister);

        firebaseAuth = FirebaseAuth.getInstance();

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validate()) {
                    loginUser();
                }
            }
        });
        userRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, RegistrationActivity.class));
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            startActivity(new Intent(MainActivity.this, SecondActivity.class));
            finish();
        }
    }

    private void loginUser() {
        
        String email = Email.getText().toString().trim();
        String password = Password.getText().toString().trim();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                if the login task is successful, opens secondactivity else shows error
                if (task.isSuccessful()){
                    Log.d(TAG, "signInWithEmail: SUCCESS");
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    updateUI(user);
                    startActivity(new Intent(MainActivity.this, SecondActivity.class));
                    finish();
                } else {
                    Log.w(TAG, "signInWithEmail: FAILED", task.getException());
                    Toast.makeText(MainActivity.this, "AUTHENTICATION ERROR. TRY AGAIN", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });

    }

    private Boolean validate(){
        Boolean result = false;

        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if(email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Please enter all the Details!", Toast.LENGTH_SHORT).show();
        }else{
            result = true;
        }
        return result;
    }

}
