package com.arturofilio.chatapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    MaterialEditText username, email, password, password2;
    Button btnRegister;

    FirebaseAuth mAuth;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);

        // Setting up the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = findViewById(R.id.edt_username);
        email = findViewById(R.id.edt_email);
        password = findViewById(R.id.edt_password);
        password2 = findViewById(R.id.edt_password2);
        btnRegister = findViewById(R.id.btn_register);

        mAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_password2 = password2.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) ||
                        TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_password2)) {

                    Toast.makeText(RegisterActivity.this,
                            "All fields are required", Toast.LENGTH_SHORT).show();

                } else if (txt_password.length() < 6) {
                    Toast.makeText(RegisterActivity.this,
                            "Password needs to be min. 6 digits long", Toast.LENGTH_SHORT).show();

                } else {

                    if (!txt_password.equals(txt_password2)) {
                        Toast.makeText(RegisterActivity.this, "Passwords must be the same"
                                , Toast.LENGTH_SHORT).show();
                        return;
                    } else {

                        registerUser(txt_username, txt_email, txt_password);
                    }

                }
            }
        });

    }

    private void registerUser(final String username, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            assert firebaseUser != null;
                            String userId = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userId);
                            hashMap.put("username", username);
                            hashMap.put("imageURL", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()) {
                                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();

                                    } else {

                                        Toast.makeText(RegisterActivity.this,
                                                "Something went wrong, please try again", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "That email is already taken.", Toast.LENGTH_SHORT).show();
                            
                        }
                    }
                });
    }
}
