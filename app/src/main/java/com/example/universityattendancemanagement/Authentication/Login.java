package com.example.universityattendancemanagement.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.example.universityattendancemanagement.R;
import com.example.universityattendancemanagement.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    ActivityLoginBinding binding;
    FirebaseAuth mAuth;
    boolean isPassToggle = false;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.goToSignUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, MainActivity.class));
            }
        });

        binding.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValid()) {
                    LoginUser(binding.enterEmailLogin.getText().toString(), binding.enterPassLogin.getText().toString());
                }
            }
        });

        binding.passToggleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPassToggle) {
                    binding.enterPassLogin.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    binding.passToggleLogin.setImageDrawable(getDrawable(R.drawable.pass_on));
                    isPassToggle = false;
                } else {
                    binding.passToggleLogin.setImageDrawable(getDrawable(R.drawable.pass_off));
                    binding.enterPassLogin.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    isPassToggle = true;
                }
            }
        });

        binding.forgetPassBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ForgetPassword.class));
            }
        });
    }

    private boolean checkValid() {
        String username = binding.enterEmailLogin.getText().toString();
        String password = binding.enterPassLogin.getText().toString();
        if (username.isEmpty() && password.isEmpty()) {
            Toast.makeText(this, "Please fill all required details.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (username.isEmpty()) {
            Toast.makeText(this, "Please enter your username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password must be longer than 8 Characters.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void LoginUser(String username, String password) {
        binding.loginProgressbarLogin.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    binding.loginProgressbarLogin.setVisibility(View.GONE);
                    binding.enterEmailLogin.setText("");
                    binding.enterPassLogin.setText("");
                    startActivity(new Intent(Login.this, OptionScreen.class));
                    finish();
                } else {
                    binding.loginProgressbarLogin.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Wrong User Credential!", Toast.LENGTH_SHORT).show();
                binding.loginProgressbarLogin.setVisibility(View.GONE);
            }
        });
    }
}