package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SecureCacheResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class fullreportpage extends AppCompatActivity {

    private TextView fullreport;
    private Button approve;
    private Button reject;
    private ImageView seeimage;
    private String current_id="";
    private String current_issue="";
    private String current_report="";
    private Bitmap current_image;
    private String issue_for_this_report="";
    private String report_to_delete="";
    public static final String UPLOAD_URL = "http://"+ScannerConstants.ip+"/ReportingSystem/uploadin_under_review.php";
    public static String TASK_UPLOAD_URL = "http://"+ScannerConstants.ip+"/ReportingSystem/task.php";
    public static final String DELETE_URL = "http://"+ScannerConstants.ip+"/ReportingSystem/delete_report.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullreportpage);

        fullreport=(TextView)findViewById(R.id.full_user_reportid);
        approve=(Button)findViewById(R.id.approvebutton);
        reject=(Button)findViewById(R.id.rejectbutton);
        seeimage=(ImageView) findViewById(R.id.image);

        current_id=ScannerConstants.USERID.get(ScannerConstants.index);
        current_issue=ScannerConstants.USER_ISSUE.get(ScannerConstants.index);
        current_report=ScannerConstants.USER_REPORT.get(ScannerConstants.index);
        current_image=ScannerConstants.selectedImageBitmap.get(ScannerConstants.index);

        fullreport.setText("ID : "+current_id+"\n\nIssue : "+current_issue+"\n\nReport : "+current_report);
        seeimage.setImageBitmap(current_image);
        if(seeimage.getDrawable() == null){
            Toast.makeText(fullreportpage.this,"View is empty",Toast.LENGTH_LONG).show();
        }

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_report_in_under_review_database();
                notify_concerned_person();
                delete_that_report_from_database();
                admin_page();
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete_that_report_from_database();
                clear_previous_page();
                admin_page();
            }
        });
    }
    public  void upload_report_in_under_review_database(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
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
                Toast.makeText(fullreportpage.this, "Check your internet connection", Toast.LENGTH_LONG).show();
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
            MySingleton.getInstance(fullreportpage.this).addToRequestQue(stringRequest);

    }
    public void delete_that_report_from_database(){

        ScannerConstants.sql="";
        report_to_delete = "DELETE "+" FROM "+ScannerConstants.data_table+" WHERE id='"+current_id+"' AND issue='"+current_issue+"' AND report='"+current_report+"'";
        ScannerConstants.sql=report_to_delete;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    //Toast.makeText(fullreportpage.this, Response, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(fullreportpage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(fullreportpage.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("query", report_to_delete);

                return params;
            }
        };
        MySingleton.getInstance(fullreportpage.this).addToRequestQue(stringRequest);
    }
    public void notify_concerned_person(){
        //upload in task table
        StringRequest stringRequest = new StringRequest(Request.Method.POST, TASK_UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toast.makeText(fullreportpage.this, Response, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(fullreportpage.this, "Check your internet connection", Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(fullreportpage.this).addToRequestQue(stringRequest);
    }
    public void admin_page(){
        Intent intent = new Intent(this,adminhomepage.class);
        startActivity(intent);
    }
    public void clear_previous_page(){
        Intent intent = new Intent(getApplicationContext(), all_reports_page.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
