package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class memberPage extends AppCompatActivity {

    private TextView post;
    private Button task;
    private Button settings;
    private Button logout;
    private String issue_for_this_member="";
    private String task_sql="";
    public static String issue_URL ="http://"+ScannerConstants.ip+"/ReportingSystem/issue.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_page);

        post=(TextView)findViewById(R.id.memberdesignation);
        task=(Button)findViewById(R.id.task_button);
        settings=(Button)findViewById(R.id.member_settings_button);
        logout=(Button)findViewById(R.id.member_logout);

        post.setText(ScannerConstants.user);

        if(ScannerConstants.member_post.equals("Moderator")){
            issue_for_this_member="Other";
        }
        else if(ScannerConstants.member_post.equals("Electrician")){
            issue_for_this_member="Electronics";
        }
        else if(ScannerConstants.member_post.equals("Librarian")){
            issue_for_this_member="Library";
        }
        else if(ScannerConstants.member_post.equals("Room Coordinator")){
            issue_for_this_member="Room";
        }
        else if(ScannerConstants.member_post.equals("Lab Coordinator")){
            issue_for_this_member="Lab";
        }


        task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_task_sql_to_php();
                open_task_page();
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
    public void open_settings_page(){
        Intent intent = new Intent(this,settings_page.class);
        startActivity(intent);
    }
    public void logout_account(){
        Intent intent = new Intent(getApplicationContext(), login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
    public void open_task_page(){
        Intent intent = new Intent(this,task_page.class);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }
    public void send_task_sql_to_php(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, issue_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    //Toast.makeText(memberPage.this,Response,Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(memberPage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(memberPage.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("issue",issue_for_this_member);
                return params;
            }

        };
        MySingleton.getInstance(memberPage.this).addToRequestQue(stringRequest);
    }
}
