package com.example.universityattendancemanagement.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.universityattendancemanagement.R;
import com.example.universityattendancemanagement.databinding.ActivityForgetPasswordBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityForgetPasswordBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        binding.forgetPassBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValid()) {
                    forgetPass(binding.enterEmailForgetPass.getText().toString());
                }
            }
        });

        binding.backFromForgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void forgetPass(String email) {
        binding.forgetPassProgressbarFP.setVisibility(View.VISIBLE);
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgetPassword.this, "Check your email for Reset password!", Toast.LENGTH_SHORT).show();
                    binding.forgetPassProgressbarFP.setVisibility(View.GONE);
                    finish();
                } else {
                    binding.forgetPassProgressbarFP.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.forgetPassProgressbarFP.setVisibility(View.GONE);
            }
        });
    }

    private boolean checkValid() {
        String email = binding.enterEmailForgetPass.getText().toString();
        if (email.isEmpty()) {
            binding.enterEmailForgetPass.setError("Please enter your email.");
            return false;
        }
        return true;
    }
}