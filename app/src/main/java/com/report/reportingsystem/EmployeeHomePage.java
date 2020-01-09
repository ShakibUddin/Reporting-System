package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EmployeeHomePage extends AppCompatActivity {

    private TextView username;
    private Button submit_report;
    private Button under_review_problems;
    private Button solved_problems;
    private Button settings;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employeehomepage);

        username=(TextView)findViewById(R.id.employeehomepage_usernameid);
        submit_report=(Button)findViewById(R.id.submit_report_button);
        under_review_problems=(Button)findViewById(R.id.employee_inprogress_report);
        solved_problems=(Button)findViewById(R.id.employee_solved_issues);
        settings=(Button)findViewById(R.id.employee_settings_button);
        logout=(Button)findViewById(R.id.employee_logout);

        username.setText(ScannerConstants.user.toUpperCase());

        submit_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_report_submission();
            }
        });
        under_review_problems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_under_review_page();
            }
        });
        solved_problems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_solved_issue_page();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_settings_page();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_account();
            }
        });

    }
    public void open_report_submission(){
        Intent intent = new Intent(this, ReportSubmissionPage.class);
        startActivity(intent);
    }
    public void open_under_review_page(){
        Intent intent = new Intent(this, UnderReviewPage.class);
        startActivity(intent);
    }
    public void open_solved_issue_page(){
        Intent intent = new Intent(this, SolvedIssuePage.class);
        startActivity(intent);
    }
    public void open_settings_page(){
        Intent intent = new Intent(this, SettingsPage.class);
        startActivity(intent);
    }
    public void logout_account(){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
