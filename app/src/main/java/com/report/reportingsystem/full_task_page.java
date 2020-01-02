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
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class full_task_page extends AppCompatActivity {

    private TextView full_task;
    private Button task_completed;
    private String current_id="";
    private String current_issue="";
    private String current_report="";
    private String task_to_delete_query = "";
    private String issue_to_delete_query = "";
    public static final String TASK_REMOVE_URL = "http://" + ScannerConstants.ip + "/ReportingSystem/task_remove.php";
    public static final String UNDER_REVIEW_ISSUE_REMOVE_URL = "http://" + ScannerConstants.ip + "/ReportingSystem/under_review_issue_remove.php";
    public static final String SOLVED_ISSUE_UPLOAD_URL = "http://" + ScannerConstants.ip + "/ReportingSystem/upload_in_solved_issue.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_task_page);

        full_task=(TextView)findViewById(R.id.full_task_id);
        task_completed=(Button)findViewById(R.id.task_completed_button);

        current_id=ScannerConstants.USERID.get(ScannerConstants.index);
        current_issue=ScannerConstants.USER_ISSUE.get(ScannerConstants.index);
        current_report=ScannerConstants.USER_REPORT.get(ScannerConstants.index);

        full_task.setText("ID : "+current_id+"\n\nIssue : "+current_issue+"\n\nReport : "+current_report);

        task_completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove_issue_from_task_table();
                remove_issue_from_under_review_table();
                add_issue_in_solved_issue_table();
                clear_previous_page();
            }
        });
    }
    public void remove_issue_from_task_table() {
        task_to_delete_query = "DELETE "+" FROM "+ScannerConstants.task_table+" WHERE id='"+current_id+"' AND issue='"+current_issue+"' AND report='"+current_report+"'";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TASK_REMOVE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toast.makeText(full_task_page.this, Response, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(full_task_page.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(full_task_page.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("query", task_to_delete_query);
                return params;
            }
        };
        MySingleton.getInstance(full_task_page.this).addToRequestQue(stringRequest);
    }

    public void remove_issue_from_under_review_table() {
        issue_to_delete_query = "DELETE "+" FROM "+ScannerConstants.under_review_table+" WHERE id='"+current_id+"' AND issue='"+current_issue+"' AND report='"+current_report+"'";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UNDER_REVIEW_ISSUE_REMOVE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    //Toast.makeText(task_page.this, Response, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(full_task_page.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(full_task_page.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("query", issue_to_delete_query);
                return params;
            }
        };
        MySingleton.getInstance(full_task_page.this).addToRequestQue(stringRequest);
    }

    public void add_issue_in_solved_issue_table() {
        //upload in solved issue table
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SOLVED_ISSUE_UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    //Toast.makeText(fullreportpage.this, Response, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(full_task_page.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", current_id);
                params.put("issue", current_issue);
                params.put("report", current_report);

                return params;
            }
        };
        MySingleton.getInstance(full_task_page.this).addToRequestQue(stringRequest);
    }
    public void clear_previous_page(){
        Intent intent = new Intent(getApplicationContext(), memberPage.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
