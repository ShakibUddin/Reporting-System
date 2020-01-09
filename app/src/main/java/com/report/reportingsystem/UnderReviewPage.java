package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UnderReviewPage extends AppCompatActivity {

    private TextView status;
    private int i=0;
    public static final String UPLOAD_URL = "http://"+ScannerConstants.ip+"/ReportingSystem/fetch_under_review.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_report_status_page);
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

        TextView status = new TextView(getApplicationContext());

        LinearLayout.LayoutParams statustextviewparams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        statustextviewparams.width=600;
        statustextviewparams.gravity= Gravity.CENTER;
        status.setLayoutParams(statustextviewparams);
        status.setText("Under Review");
        status.setTextColor(Color.WHITE);
        status.setTextSize(30);
        status.setPadding(10,10,10,10);
        status.setAllCaps(false);
        status.setBackgroundResource(R.drawable.textviewstyle);
        statustextviewparams.setMargins(0,100,0,10);
        status.setGravity(Gravity.CENTER);
        linearLayout.addView(status);

        scroll.addView(linearLayout);


        StringRequest stringRequest = new StringRequest(Request.Method.GET, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonobject = new JSONObject(response);
                    JSONArray jsonArray = jsonobject.getJSONArray("under_review_data");
                    ScannerConstants.jsonArraySize=jsonArray.length();

                    for(i=0;i<jsonArray.length();++i){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String userid=jsonObject.getString("id");
                        String userissue=jsonObject.getString("issue");
                        String userreport=jsonObject.getString("report");

                        final LinearLayout innerlinearLayout = new LinearLayout(getApplicationContext());
                        LinearLayout.LayoutParams innerlayoutParams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        innerlinearLayout.setLayoutParams(innerlayoutParams);
                        innerlinearLayout.setOrientation(LinearLayout.VERTICAL);
                        innerlayoutParams.setMargins(40,40,40,40);
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

                        textviewparams.setMargins(15,15,15,15);

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

                        report.setLayoutParams(textviewparams);
                        report.setText(userreport);
                        report.setTextColor(Color.WHITE);
                        report.setTextSize(25);
                        report.setAllCaps(false);


                        innerlinearLayout.addView(id);
                        innerlinearLayout.addView(issue);
                        innerlinearLayout.addView(report);


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Toast.makeText(UnderReviewPage.this,"Check your internet connection",Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(UnderReviewPage.this).addToRequestQue(stringRequest);
    }
}
