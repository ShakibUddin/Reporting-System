package com.report.reportingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class task_page extends AppCompatActivity {

    private int i = 0;
    public static String TASK_FETCH_URL = "http://" + ScannerConstants.ip + "/ReportingSystem/fetch_task.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_task_page);

        ScrollView scroll = new ScrollView(this);
        scroll.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT)
        );


        final LinearLayout linearLayout = new LinearLayout(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.WHITE);
        setContentView(scroll);

        TextView user = new TextView(getApplicationContext());

        LinearLayout.LayoutParams usertextviewparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        usertextviewparams.width = 600;
        usertextviewparams.gravity = Gravity.CENTER;
        user.setLayoutParams(usertextviewparams);
        user.setText("Tasks");
        user.setTextColor(Color.WHITE);
        user.setTextSize(35);
        user.setPadding(10, 10, 10, 10);
        user.setAllCaps(false);
        user.setBackgroundResource(R.drawable.textviewstyle);
        usertextviewparams.setMargins(0, 100, 0, 10);
        user.setGravity(Gravity.CENTER);
        linearLayout.addView(user);

        scroll.addView(linearLayout);

        ScannerConstants.USERID.clear();
        ScannerConstants.USER_ISSUE.clear();
        ScannerConstants.USER_REPORT.clear();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, TASK_FETCH_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonArray = jsonobject.getJSONArray("retrieved_data");
                    ScannerConstants.jsonArraySize = jsonArray.length();

                    for (i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        final String userid = jsonObject.getString("id");
                        final String userissue = jsonObject.getString("issue");
                        final String userreport = jsonObject.getString("report");

                        ScannerConstants.USERID.add(userid);//inserting each id in report arraylist declared in ScannerConstants
                        ScannerConstants.USER_ISSUE.add(userissue);//inserting each issue in report arraylist declared in ScannerConstants
                        ScannerConstants.USER_REPORT.add(userreport);//inserting each report in report arraylist declared in ScannerConstants

                        ScannerConstants.task_to_remove.add(userreport);

                        final LinearLayout innerlinearLayout = new LinearLayout(getApplicationContext());
                        LinearLayout.LayoutParams innerlayoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        innerlinearLayout.setLayoutParams(innerlayoutParams);
                        innerlinearLayout.setOrientation(LinearLayout.VERTICAL);
                        innerlayoutParams.setMargins(40, 40, 40, 40);
                        linearLayout.addView(innerlinearLayout);

                        innerlinearLayout.setId(i);//giving unique id to each inner linear layouts

                        innerlinearLayout.setBackgroundResource(R.drawable.textviewstyle);

                        TextView id = new TextView(getApplicationContext());
                        TextView issue = new TextView(getApplicationContext());
                        TextView report = new TextView(getApplicationContext());


                        LinearLayout.LayoutParams textviewparams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );

                        textviewparams.setMargins(15, 15, 15, 15);

                        id.setLayoutParams(textviewparams);
                        id.setText(userid);
                        id.setTextColor(Color.WHITE);
                        id.setTextSize(25);
                        id.setAllCaps(false);

                        issue.setLayoutParams(textviewparams);
                        issue.setText(userissue);
                        issue.setTextColor(Color.WHITE);
                        issue.setTextSize(25);
                        issue.setAllCaps(false);

                        innerlinearLayout.addView(id);
                        innerlinearLayout.addView(issue);

                        innerlinearLayout.setClickable(true);
                        innerlinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ScannerConstants.index = innerlinearLayout.getId();
                                open_full_task_page();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(task_page.this,"Check your internet connection",Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(task_page.this).addToRequestQue(stringRequest);
    }
    public void open_full_task_page(){
        Intent intent = new Intent(this,full_task_page.class);
        startActivity(intent);
    }
}

