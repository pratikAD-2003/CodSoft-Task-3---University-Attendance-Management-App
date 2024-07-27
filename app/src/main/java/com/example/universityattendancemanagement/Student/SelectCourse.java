package com.example.universityattendancemanagement.Student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.universityattendancemanagement.databinding.ActivitySelectCourseBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SelectCourse extends AppCompatActivity {
    ActivitySelectCourseBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String[] sessionList = {"2028", "2027", "2026", "2025", "2024", "2023", "2022", "2021", "2020"};

    @Override
    protected void onStart() {
        super.onStart();
        binding.selectCourseProgressbarSC.setVisibility(View.VISIBLE);
        reference.child("Students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        StudentModel model = snapshot1.getValue(StudentModel.class);
                        if (Objects.equals(model.getUid(), uid)) {
                            binding.selectCourseProgressbarSC.setVisibility(View.GONE);
                            startActivity(new Intent(SelectCourse.this, DashBoard.class));
                            finish();
                            break;
                        } else {
                            binding.selectCourseProgressbarSC.setVisibility(View.GONE);
                            break;
                        }
                    }
                } else {
                    binding.selectCourseProgressbarSC.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                binding.selectCourseProgressbarSC.setVisibility(View.GONE);
            }
        });
    }

    String[] courseList = {"B.Tech (Bachelor of Technology)", "M.Tech (Master of Technology)", "MBBS (Bachelor of Medicine and Bachelor of Surgery)", "BDS (Bachelor of Dental Surgery)", "BAMS (Bachelor of Ayurvedic Medicine and Surgery)", "BHMS (Bachelor of Homeopathic Medicine and Surgery)", "BUMS (Bachelor of Unani Medicine and Surgery)", "BPT (Bachelor of Physiotherapy)", "B.Pharm (Bachelor of Pharmacy)", "B.Sc Nursing", "B.V.Sc & AH (Bachelor of Veterinary Science and Animal Husbandry)", "M.D (Doctor of Medicine)", "M.S (Master of Surgery)", "MDS (Master of Dental Surgery)", "M.Pharm (Master of Pharmacy)", "M.Sc Nursing", "BA (Bachelor of Arts)", "MA (Master of Arts)", "B.Sc (Bachelor of Science)", "M.Sc (Master of Science)", "B.Com (Bachelor of Commerce)", "M.Com (Master of Commerce)", "BBA (Bachelor of Business Administration)", "MBA (Master of Business Administration)", "LLB (Bachelor of Laws)", "BA LLB (Integrated Bachelor of Arts and Bachelor of Laws)", "BBA LLB (Integrated Bachelor of Business Administration and Bachelor of Laws)", "LLM (Master of Laws)", "B.Ed (Bachelor of Education)", "M.Ed (Master of Education)", "B.Arch (Bachelor of Architecture)", "M.Arch (Master of Architecture)", "B.Des (Bachelor of Design)", "M.Des (Master of Design)", "B.Sc Agriculture", "B.Sc Forestry", "M.Sc Agriculture", "M.Sc Forestry", "BHM (Bachelor of Hotel Management)", "BTTM (Bachelor of Tourism and Travel Management)", "MHM (Master of Hotel Management)", "MTTM (Master of Tourism and Travel Management)", "BPA (Bachelor of Performing Arts)", "MPA (Master of Performing Arts)"};
    String[] branchList;

    String selectedBranch = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivitySelectCourseBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        reference = FirebaseDatabase.getInstance().getReference();

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_list_item_1, courseList);
        binding.courseSpinnerSC.setAdapter(courseAdapter);

        ArrayAdapter<String> sessionAdapter = new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_list_item_1, sessionList);
        binding.sessionSpinnerSC.setAdapter(sessionAdapter);

        binding.courseSpinnerSC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("Chek", courseList[position]);
                getBranch(courseList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.branchSpinnerSC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBranch = branchList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.submitBtnSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("Check_Branch", selectedBranch);
                if (checkValid()) {
                    submitData();
                }
            }
        });
    }

    private void submitData() {
        binding.selectCourseProgressbarSC.setVisibility(View.VISIBLE);
//        String branch = binding.branchSpinnerSC.getSelectedItem().toString().isEmpty() ? "Not Available" : binding.branchSpinnerSC.getSelectedItem().toString();
        StudentModel model = new StudentModel(uid, binding.enterNameSC.getText().toString(), binding.enterRoleSC.getText().toString(), binding.sessionSpinnerSC.getSelectedItem().toString(), binding.courseSpinnerSC.getSelectedItem().toString(), selectedBranch, binding.enterPinSC.getText().toString(), "", StudentModel.STUDENT, 0, 0);
        reference.child("Students").child(uid).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    binding.selectCourseProgressbarSC.setVisibility(View.GONE);
                    startActivity(new Intent(SelectCourse.this, DashBoard.class));
                    finish();
                } else {
                    binding.selectCourseProgressbarSC.setVisibility(View.GONE);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                binding.selectCourseProgressbarSC.setVisibility(View.GONE);
            }
        });
    }

    private void getBranch(String course) {
        switch (course) {
            case "B.Tech (Bachelor of Technology)":
                branchList = new String[]{"Computer Science Engineering", "Information Technology", "Electronics and Communication Engineering", "Mechanical Engineering", "Civil Engineering", "Electrical Engineering", "Chemical Engineering", "Aerospace Engineering", "Biotechnology", "Automobile Engineering", "Metallurgical Engineering", "Petroleum Engineering", "Environmental Engineering"};
                if (branchList.length > 0) {
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_list_item_1, branchList);
                    binding.branchSpinnerSC.setVisibility(View.VISIBLE);
                    binding.branchTextSC.setVisibility(View.VISIBLE);
                    binding.branchSpinnerSC.setAdapter(branchAdapter);
                } else {
                    binding.branchSpinnerSC.setVisibility(View.GONE);
                    binding.branchTextSC.setVisibility(View.GONE);
                }
                break;
            case "M.Tech (Master of Technology)":
                branchList = new String[]{"Computer Science Engineering", "Information Technology", "Electronics and Communication Engineering", "Civil Engineering", "Mechanical Engineering", "Electrical Engineering", "Chemical Engineering", "Aerospace Engineering", "Biotechnology", "Automobile Engineering", "Metallurgical Engineering", "Petroleum Engineering", "Environmental Engineering"};
                if (branchList.length > 0) {
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_list_item_1, branchList);
                    binding.branchSpinnerSC.setVisibility(View.VISIBLE);
                    binding.branchTextSC.setVisibility(View.VISIBLE);
                    binding.branchSpinnerSC.setAdapter(branchAdapter);
                } else {
                    binding.branchSpinnerSC.setVisibility(View.GONE);
                    binding.branchTextSC.setVisibility(View.GONE);
                }
                break;
            case "BA (Bachelor of Arts)":
                branchList = new String[]{"English", "History", "Political Science", "Psychology", "Sociology", "Economics", "Geography", "Philosophy", "Anthropology", "Journalism and Mass Communication"};
                if (branchList.length > 0) {
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_list_item_1, branchList);
                    binding.branchSpinnerSC.setVisibility(View.VISIBLE);
                    binding.branchTextSC.setVisibility(View.VISIBLE);
                    binding.branchSpinnerSC.setAdapter(branchAdapter);
                } else {
                    binding.branchSpinnerSC.setVisibility(View.GONE);
                    binding.branchTextSC.setVisibility(View.GONE);
                }
                break;
            case "MA (Master of Arts)":
                branchList = new String[]{"English", "History", "Political Science", "Psychology", "Sociology", "Economics", "Geography", "Anthropology", "Philosophy", "Journalism and Mass Communication"};
                if (branchList.length > 0) {
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_list_item_1, branchList);
                    binding.branchSpinnerSC.setVisibility(View.VISIBLE);
                    binding.branchTextSC.setVisibility(View.VISIBLE);
                    binding.branchSpinnerSC.setAdapter(branchAdapter);
                } else {
                    binding.branchSpinnerSC.setVisibility(View.GONE);
                    binding.branchTextSC.setVisibility(View.GONE);
                }
                break;
            case "B.Sc (Bachelor of Science)":
                branchList = new String[]{"Physics", "Chemistry", "Mathematics", "Biology", "Botany", "Zoology", "Computer Science", "Biotechnology", "Microbiology", "Environmental Science"};
                if (branchList.length > 0) {
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_list_item_1, branchList);
                    binding.branchSpinnerSC.setVisibility(View.VISIBLE);
                    binding.branchTextSC.setVisibility(View.VISIBLE);
                    binding.branchSpinnerSC.setAdapter(branchAdapter);
                } else {
                    binding.branchSpinnerSC.setVisibility(View.GONE);
                    binding.branchTextSC.setVisibility(View.GONE);
                }
                break;
            case "M.Sc (Master of Science)":
                branchList = new String[]{"Physics", "Chemistry", "Mathematics", "Biology", "Zoology", "Botany", "Computer Science", "Biotechnology", "Microbiology", "Environmental Science"};
                if (branchList.length > 0) {
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_list_item_1, branchList);
                    binding.branchSpinnerSC.setVisibility(View.VISIBLE);
                    binding.branchSpinnerSC.setAdapter(branchAdapter);
                    binding.branchTextSC.setVisibility(View.VISIBLE);
                } else {
                    binding.branchSpinnerSC.setVisibility(View.GONE);
                    binding.branchTextSC.setVisibility(View.GONE);
                }
                break;
            case "B.Com (Bachelor of Commerce)":
                branchList = new String[]{"General", "Accounting and Finance", "Banking and Insurance", "Taxation", "Computer Applications"};
                if (branchList.length > 0) {
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_list_item_1, branchList);
                    binding.branchSpinnerSC.setVisibility(View.VISIBLE);
                    binding.branchSpinnerSC.setAdapter(branchAdapter);
                    binding.branchTextSC.setVisibility(View.VISIBLE);
                } else {
                    binding.branchSpinnerSC.setVisibility(View.GONE);
                    binding.branchTextSC.setVisibility(View.GONE);
                }
                break;
            case "M.Com (Master of Commerce)":
                branchList = new String[]{"General", "Accounting and Finance", "Taxation", "Banking and Insurance", "Computer Applications"};
                if (branchList.length > 0) {
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SelectCourse.this, android.R.layout.simple_list_item_1, branchList);
                    binding.branchSpinnerSC.setVisibility(View.VISIBLE);
                    binding.branchTextSC.setVisibility(View.VISIBLE);
                    binding.branchSpinnerSC.setAdapter(branchAdapter);
                } else {
                    binding.branchSpinnerSC.setVisibility(View.GONE);
                    binding.branchTextSC.setVisibility(View.GONE);
                }
                break;
            default:
                binding.branchSpinnerSC.setVisibility(View.GONE);
                binding.branchTextSC.setVisibility(View.GONE);
                selectedBranch = "Not Available";
                break;
        }
    }

    private boolean checkValid() {
        String name = binding.enterNameSC.getText().toString();
        String roleNumber = binding.enterRoleSC.getText().toString();
        String pin = binding.enterPinSC.getText().toString();

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
}