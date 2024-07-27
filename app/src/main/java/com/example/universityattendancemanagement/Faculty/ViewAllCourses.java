package com.example.universityattendancemanagement.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.universityattendancemanagement.R;
import com.example.universityattendancemanagement.databinding.ActivityViewAllCoursesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewAllCourses extends AppCompatActivity {
    ActivityViewAllCoursesBinding binding;
    ArrayList<CourseModel> list;
    CourseAdapter adapter;

    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityViewAllCoursesBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        list = new ArrayList<>();
        binding.coursesRecyclerviewVAC.setLayoutManager(new LinearLayoutManager(this));
        binding.coursesRecyclerviewVAC.setHasFixedSize(true);
        adapter = new CourseAdapter(this, list);
        binding.coursesRecyclerviewVAC.setAdapter(adapter);

        binding.viewAllProgressbarVAC.setVisibility(View.VISIBLE);
        reference.child("Courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    binding.viewAllProgressbarVAC.setVisibility(View.GONE);
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        CourseModel model = snapshot1.getValue(CourseModel.class);
                        list.add(model);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    binding.viewAllProgressbarVAC.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.viewAllProgressbarVAC.setVisibility(View.GONE);
            }
        });
    }
}