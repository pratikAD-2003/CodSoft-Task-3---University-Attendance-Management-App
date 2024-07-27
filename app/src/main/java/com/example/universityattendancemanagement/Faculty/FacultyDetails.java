package com.example.universityattendancemanagement.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.universityattendancemanagement.Student.StudentModel;
import com.example.universityattendancemanagement.databinding.ActivityFacultyDetailsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FacultyDetails extends AppCompatActivity {
    ActivityFacultyDetailsBinding binding;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityFacultyDetailsBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.submitFacultyBtnSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkValid()) {
                    submitData();
                }
            }
        });
    }


    private boolean checkValid() {
        String name = binding.enterFacultyFD.getText().toString();
        String roleNumber = binding.enterFacultyRoleSC.getText().toString();
        String pin = binding.enterFacultyPinSC.getText().toString();

        if (name.isEmpty() && roleNumber.isEmpty() && pin.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (name.isEmpty()) {
            Toast.makeText(this, "Please enter your name.", Toast.LENGTH_SHORT).show();
            return false;
        } else if (roleNumber.isEmpty()) {
            Toast.makeText(this, "Please enter your role number..", Toast.LENGTH_SHORT).show();
            return false;
        } else if (pin.isEmpty()) {
            Toast.makeText(this, "Please create your pin.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void submitData() {
        binding.facultyProgressbarDetails.setVisibility(View.VISIBLE);
        StudentModel model = new StudentModel(uid, binding.enterFacultyFD.getText().toString(), binding.enterFacultyRoleSC.getText().toString(), "", "", "", binding.enterFacultyPinSC.getText().toString(), "", StudentModel.FACULTY, 0, 0);
        reference.child("Students").child(uid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    binding.facultyProgressbarDetails.setVisibility(View.GONE);
                    startActivity(new Intent(FacultyDetails.this, FacultyDashboard.class));
                    finish();
                } else {
                    binding.facultyProgressbarDetails.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.facultyProgressbarDetails.setVisibility(View.GONE);
            }
        });
    }

}
