package com.example.universityattendancemanagement.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Toast;

import com.example.universityattendancemanagement.R;
import com.example.universityattendancemanagement.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    boolean isPassToggle = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValid()) {
                    Signing(binding.enterEmailMain.getText().toString(), binding.enterPassMain.getText().toString());
                }
            }
        });

        binding.goToSignInMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });

        binding.backFromSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        binding.passToggleMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPassToggle) {
                    binding.enterPassMain.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    binding.passToggleMainBtn.setImageDrawable(getDrawable(R.drawable.pass_on));
                    isPassToggle = false;
                } else {
                    binding.passToggleMainBtn.setImageDrawable(getDrawable(R.drawable.pass_off));
                    binding.enterPassMain.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    isPassToggle = true;
                }
            }
        });
    }

    private boolean checkValid() {
        String username = binding.enterEmailMain.getText().toString();
        String password = binding.enterPassMain.getText().toString();
        String cPass = binding.enterConfirmPassMain.getText().toString();
        if (username.isEmpty() && password.isEmpty() && cPass.isEmpty()) {
            Toast.makeText(this, "Please fill all required details.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (username.isEmpty()) {
            Toast.makeText(this, "Please create your username", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Please create your password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password must be longer than 8 Characters.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (cPass.isEmpty()) {
            Toast.makeText(this, "Please confirm your password", Toast.LENGTH_SHORT).show();
            return false;
        } else if (!Objects.equals(password, cPass)) {
            Toast.makeText(this, "Password not matched!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    private void Signing(String username, String password) {
        binding.signUpProgressbarSignup.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    binding.signUpProgressbarSignup.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Signup Done!", Toast.LENGTH_SHORT).show();
                    binding.enterEmailMain.setText("");
                    binding.enterPassMain.setText("");
                    binding.enterConfirmPassMain.setText("");
                    startActivity(new Intent(MainActivity.this, OptionScreen.class));
                    finish();
                } else {
                    binding.signUpProgressbarSignup.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.signUpProgressbarSignup.setVisibility(View.GONE);
            }
        });
    }
}