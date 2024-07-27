package com.example.universityattendancemanagement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.universityattendancemanagement.Authentication.Login;
import com.example.universityattendancemanagement.Authentication.OptionScreen;
import com.example.universityattendancemanagement.Faculty.FacultyDashboard;
import com.example.universityattendancemanagement.Student.DashBoard;
import com.example.universityattendancemanagement.Student.StudentModel;
import com.example.universityattendancemanagement.databinding.ActivitySplashScreenBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SplashScreen extends AppCompatActivity {
    ActivitySplashScreenBinding binding;
    DatabaseReference reference;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onStart() {
        super.onStart();
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.alpha);
        binding.appTextSC.setAnimation(animation);
        animation.start();
        if (user != null) {
            reference = FirebaseDatabase.getInstance().getReference();
            String uid = user.getUid();
            reference.child("Students").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            StudentModel model = snapshot1.getValue(StudentModel.class);
                            if (Objects.equals(model.getUid(), uid)) {
//                                startActivity(new Intent(SplashScreen.this, DashBoard.class));
//                                finish();
                                if (Objects.equals(model.getType(), StudentModel.STUDENT)) {
                                    startActivity(new Intent(SplashScreen.this, DashBoard.class));
                                    finish();
                                    break;
                                } else if (Objects.equals(model.getType(), StudentModel.FACULTY)) {
                                    startActivity(new Intent(SplashScreen.this, FacultyDashboard.class));
                                    finish();
                                    break;
                                }
                                break;
                            } else {
                                startActivity(new Intent(SplashScreen.this, OptionScreen.class));
                                finish();
                                break;
                            }
                        }
                    } else {
                        startActivity(new Intent(SplashScreen.this, Login.class));
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    startActivity(new Intent(SplashScreen.this, Login.class));
                    finish();
                }
            });
        } else {
            startActivity(new Intent(SplashScreen.this, Login.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
    }
}