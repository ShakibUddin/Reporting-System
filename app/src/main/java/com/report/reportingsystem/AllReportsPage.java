package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class AllReportsPage extends AppCompatActivity {
    private RequestQueue rq;
    private Bitmap userBitmap;
    private int i=0;


    public static final String UPLOAD_URL = "http://"+ScannerConstants.ip+"/ReportingSystem/fetchAPI.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_adminhomepage);

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
        linearLayout.setBackgroundResource(R.color.background);
        scroll.setBackgroundResource(R.color.background);
        setContentView(scroll);

        TextView user = new TextView(getApplicationContext());

        LinearLayout.LayoutParams usertextviewparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        usertextviewparams.width=600;
        usertextviewparams.gravity=Gravity.CENTER;
        user.setLayoutParams(usertextviewparams);
        user.setText(ScannerConstants.user);
        user.setTextColor(Color.WHITE);
        user.setTextSize(35);
        user.setAllCaps(false);
        user.setBackgroundResource(R.drawable.submitbutton);
        usertextviewparams.setMargins(0,100,0,10);
        user.setGravity(Gravity.CENTER);
        linearLayout.addView(user);

        scroll.addView(linearLayout);

        ScannerConstants.issue.clear();
        ScannerConstants.USERID.clear();
        ScannerConstants.USER_ISSUE.clear();
        ScannerConstants.USER_REPORT.clear();
        ScannerConstants.selectedImageName.clear();


        rq= Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, UPLOAD_URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("retrieved_data");
                    ScannerConstants.jsonArraySize=jsonArray.length();
                    if(jsonArray.length()>0){
                    for(i=0;i<jsonArray.length();++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String userid = jsonObject.getString("id");
                        String userissue = jsonObject.getString("issue");
                        String userreport = jsonObject.getString("report");
                        String userimagename = jsonObject.getString("image_name");


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

                        innerlinearLayout.setBackgroundResource(R.drawable.submitbutton);

                        TextView id = new TextView(getApplicationContext());
                        TextView issue = new TextView(getApplicationContext());


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

                        ScannerConstants.USERID.add(userid);//inserting each id in report arraylist declared in ScannerConstants
                        ScannerConstants.USER_ISSUE.add(userissue);//inserting each issue in report arraylist declared in ScannerConstants
                        ScannerConstants.USER_REPORT.add(userreport);//inserting each report in report arraylist declared in ScannerConstants
                        ScannerConstants.issue.add(userissue);//this line is just for members who will solve the issue...i need the issue separately
                        ScannerConstants.selectedImageName.add(userimagename);


                        innerlinearLayout.addView(id);
                        innerlinearLayout.addView(issue);

                        innerlinearLayout.setClickable(true);
                        innerlinearLayout.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ScannerConstants.index = innerlinearLayout.getId();
                                openFullReportPage();
                            }
                        });
                    }
                    }
                    else{

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    //Toast.makeText(AllReportsPage.this,"Check your internet connection",Toast.LENGTH_LONG).show();
                final LinearLayout innerlinearLayout = new LinearLayout(getApplicationContext());
                LinearLayout.LayoutParams innerlayoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );

                TextView message = new TextView(getApplicationContext());
                LinearLayout.LayoutParams textviewparams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                innerlinearLayout.setLayoutParams(innerlayoutParams);
                innerlinearLayout.setOrientation(LinearLayout.VERTICAL);
                innerlayoutParams.setMargins(40, 60, 40, 40);
                linearLayout.addView(innerlinearLayout);
                innerlinearLayout.setBackgroundResource(R.drawable.submitbutton);
                message.setLayoutParams(textviewparams);
                message.setText("There are no reports at this moment");
                message.setTextColor(Color.WHITE);
                message.setTextSize(30);
                message.setAllCaps(false);
                message.setPadding(10,10,10,10);
                message.setGravity(Gravity.CENTER);

                innerlinearLayout.addView(message);
            }
        });
        rq.add(jsonObjectRequest);
    }
    public void openFullReportPage(){
        Intent intent = new Intent(this,fullreportpage.class);
        startActivityForResult(intent,1);
    }

}
