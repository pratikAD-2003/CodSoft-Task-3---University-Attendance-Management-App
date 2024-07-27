package com.example.universityattendancemanagement.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.example.universityattendancemanagement.Student.StudentModel;
import com.example.universityattendancemanagement.databinding.ActivityStudentListBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class StudentList extends AppCompatActivity {
    ActivityStudentListBinding binding;
    String course = "";
    String branch = "";
    String session = "";
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    ArrayList<StudentModel> list;
    ArrayList<StudentModel> tempList;

    StudentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityStudentListBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        tempList = new ArrayList<>();

        binding.studentRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.studentRecyclerview.setHasFixedSize(true);
        adapter = new StudentAdapter(this, list);
        binding.studentRecyclerview.setAdapter(adapter);

        course = getIntent().getStringExtra("course");
        branch = getIntent().getStringExtra("branch");
        session = getIntent().getStringExtra("session");

        binding.studentCourseSL.setText(course);
        binding.studentSessionSL.setText(session);
        if (!Objects.equals(branch, "Not Available")) {
            binding.studentBranchSL.setText(branch);
        } else {
            binding.studentBranchSL.setText("");
        }
        getData(course, branch, session);
    }


    private void getData(String course, String branch, String session) {
        binding.studentListProgressbarSL.setVisibility(View.VISIBLE);
        list.clear();
        reference.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        StudentModel model = snapshot1.getValue(StudentModel.class);
                        if (Objects.equals(model.getCourse(), course) && Objects.equals(model.getBranch(), branch) && Objects.equals(model.getSession(), session) && Objects.equals(model.getType(), StudentModel.STUDENT)) {
                            binding.studentListProgressbarSL.setVisibility(View.GONE);
                            list.add(model);
                        } else {
                            binding.studentListProgressbarSL.setVisibility(View.GONE);
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    binding.studentListProgressbarSL.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.studentListProgressbarSL.setVisibility(View.GONE);
            }
        });
    }
}