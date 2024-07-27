package com.example.universityattendancemanagement.Authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.universityattendancemanagement.Student.DashBoard;
import com.example.universityattendancemanagement.Faculty.FacultyDashboard;
import com.example.universityattendancemanagement.Faculty.FacultyDetails;
import com.example.universityattendancemanagement.Student.SelectCourse;
import com.example.universityattendancemanagement.Student.StudentModel;
import com.example.universityattendancemanagement.databinding.ActivityOptionScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class OptionScreen extends AppCompatActivity {
    ActivityOptionScreenBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();

    @Override
    protected void onStart() {
        super.onStart();
        binding.optionProgressbarOption.setVisibility(View.VISIBLE);
        reference.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        StudentModel model = snapshot1.getValue(StudentModel.class);
                        if (Objects.equals(model.getUid(), uid)) {
                            if (Objects.equals(model.getType(), StudentModel.STUDENT)) {
                                binding.optionProgressbarOption.setVisibility(View.GONE);
                                startActivity(new Intent(OptionScreen.this, DashBoard.class));
                                finish();
                            } else if (Objects.equals(model.getType(), StudentModel.FACULTY)) {
                                binding.optionProgressbarOption.setVisibility(View.GONE);
                                startActivity(new Intent(OptionScreen.this, FacultyDashboard.class));
                                finish();
                            }
                        } else {
                            binding.optionProgressbarOption.setVisibility(View.GONE);
                        }
                    }
                } else {
                    binding.optionProgressbarOption.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.optionProgressbarOption.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityOptionScreenBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        binding.joinByStudentOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent.putExtra("type", StudentModel.STUDENT);
                Intent intent = new Intent(OptionScreen.this, SelectCourse.class);
                startActivity(intent);
            }
        });

        binding.joinByFacultyOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent.putExtra("type", StudentModel.FACULTY);
                Intent intent = new Intent(OptionScreen.this, FacultyDetails.class);
                startActivity(intent);
            }
        });
    }
}