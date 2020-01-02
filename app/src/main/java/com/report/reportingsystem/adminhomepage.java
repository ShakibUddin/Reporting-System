package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class adminhomepage extends AppCompatActivity {

    private TextView admin_home_user;
    private Button seeReports;
    private Button addMember;
    private Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminhomepage);

        admin_home_user=(TextView)findViewById(R.id.admin_home_usernameid);
        seeReports=(Button)findViewById(R.id.see_reports_button);
        addMember=(Button)findViewById(R.id.add_member_button);
        logout=(Button)findViewById(R.id.admin_logout);

        seeReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                allreportpage();
            }
        });
        addMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                member_signup_page();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout_account();
            }
        });

    }
    public void allreportpage(){
        Intent intent = new Intent(this,all_reports_page.class);
        startActivity(intent);
    }
    public void member_signup_page(){
        Intent intent = new Intent(this,member_signup.class);
        startActivity(intent);
    }
    public void logout_account(){
        Intent intent = new Intent(getApplicationContext(), login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
}
