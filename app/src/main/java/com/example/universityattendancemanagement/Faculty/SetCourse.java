package com.example.universityattendancemanagement.Faculty;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.universityattendancemanagement.R;
import com.example.universityattendancemanagement.Student.DashBoard;
import com.example.universityattendancemanagement.Student.SelectCourse;
import com.example.universityattendancemanagement.Student.StudentModel;
import com.example.universityattendancemanagement.databinding.ActivitySelectCourseBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SetCourse extends AppCompatActivity {
    com.example.universityattendancemanagement.databinding.ActivitySetCourseBinding binding;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String uid = user.getUid();
    String[] sessionList = {"2028", "2027", "2026", "2025", "2024", "2023", "2022", "2021", "2020"};
    String[] courseList = {"B.Tech (Bachelor of Technology)", "M.Tech (Master of Technology)", "MBBS (Bachelor of Medicine and Bachelor of Surgery)", "BDS (Bachelor of Dental Surgery)", "BAMS (Bachelor of Ayurvedic Medicine and Surgery)", "BHMS (Bachelor of Homeopathic Medicine and Surgery)", "BUMS (Bachelor of Unani Medicine and Surgery)", "BPT (Bachelor of Physiotherapy)", "B.Pharm (Bachelor of Pharmacy)", "B.Sc Nursing", "B.V.Sc & AH (Bachelor of Veterinary Science and Animal Husbandry)", "M.D (Doctor of Medicine)", "M.S (Master of Surgery)", "MDS (Master of Dental Surgery)", "M.Pharm (Master of Pharmacy)", "M.Sc Nursing", "BA (Bachelor of Arts)", "MA (Master of Arts)", "B.Sc (Bachelor of Science)", "M.Sc (Master of Science)", "B.Com (Bachelor of Commerce)", "M.Com (Master of Commerce)", "BBA (Bachelor of Business Administration)", "MBA (Master of Business Administration)", "LLB (Bachelor of Laws)", "BA LLB (Integrated Bachelor of Arts and Bachelor of Laws)", "BBA LLB (Integrated Bachelor of Business Administration and Bachelor of Laws)", "LLM (Master of Laws)", "B.Ed (Bachelor of Education)", "M.Ed (Master of Education)", "B.Arch (Bachelor of Architecture)", "M.Arch (Master of Architecture)", "B.Des (Bachelor of Design)", "M.Des (Master of Design)", "B.Sc Agriculture", "B.Sc Forestry", "M.Sc Agriculture", "M.Sc Forestry", "BHM (Bachelor of Hotel Management)", "BTTM (Bachelor of Tourism and Travel Management)", "MHM (Master of Hotel Management)", "MTTM (Master of Tourism and Travel Management)", "BPA (Bachelor of Performing Arts)", "MPA (Master of Performing Arts)"};
    String[] branchList;
    String branch = "Not Available";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = com.example.universityattendancemanagement.databinding.ActivitySetCourseBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(SetCourse.this, android.R.layout.simple_list_item_1, courseList);
        binding.courseSpinnerSC.setAdapter(courseAdapter);

        ArrayAdapter<String> sessionAdapter = new ArrayAdapter<String>(SetCourse.this, android.R.layout.simple_list_item_1, sessionList);
        binding.sessionSpinnerSC.setAdapter(sessionAdapter);

        binding.courseSpinnerSC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getBranch(courseList[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.branchSpinnerSC.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = branchList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.launchNewCourseSC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

    }


    private void submitData() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Are you sure ?");
        builder.setMessage("You want to launch new Course!");
        builder.setPositiveButton("Launch", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                binding.setCourseProgressbarSC.setVisibility(View.VISIBLE);
                String putId = reference.push().getKey();
                CourseModel model = new CourseModel(0, binding.courseSpinnerSC.getSelectedItem().toString(), branch, binding.sessionSpinnerSC.getSelectedItem().toString(), "true", "", putId);
                reference.child("Courses").child(putId).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            binding.setCourseProgressbarSC.setVisibility(View.GONE);
                            finish();
                        } else {
                            binding.setCourseProgressbarSC.setVisibility(View.GONE);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        binding.setCourseProgressbarSC.setVisibility(View.GONE);
                    }
                });
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    private void getBranch(String course) {
        switch (course) {
            case "B.Tech (Bachelor of Technology)":
                branchList = new String[]{"Computer Science Engineering", "Information Technology", "Electronics and Communication Engineering", "Mechanical Engineering", "Civil Engineering", "Electrical Engineering", "Chemical Engineering", "Aerospace Engineering", "Biotechnology", "Automobile Engineering", "Metallurgical Engineering", "Petroleum Engineering", "Environmental Engineering"};
                if (branchList.length > 0) {
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SetCourse.this, android.R.layout.simple_list_item_1, branchList);
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
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SetCourse.this, android.R.layout.simple_list_item_1, branchList);
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
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SetCourse.this, android.R.layout.simple_list_item_1, branchList);
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
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SetCourse.this, android.R.layout.simple_list_item_1, branchList);
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
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SetCourse.this, android.R.layout.simple_list_item_1, branchList);
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
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SetCourse.this, android.R.layout.simple_list_item_1, branchList);
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
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SetCourse.this, android.R.layout.simple_list_item_1, branchList);
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
                    ArrayAdapter<String> branchAdapter = new ArrayAdapter<String>(SetCourse.this, android.R.layout.simple_list_item_1, branchList);
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
                branch = "Not Available";
                break;
        }
    }
}