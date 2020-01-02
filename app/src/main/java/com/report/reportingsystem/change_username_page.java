package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class change_username_page extends AppCompatActivity {


    private String change_name_sql="";
    private String change_id_sql="";
    private Button change;
    private EditText previous_name;
    private EditText new_name;
    private String USERNAME_URL="http://"+ScannerConstants.ip+"/ReportingSystem/change_username.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_username_page);

        change=(Button)findViewById(R.id.change_username_button);
        previous_name=(EditText)findViewById(R.id.previous_usernameid);
        new_name=(EditText)findViewById(R.id.new_usernameid);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( previous_name.getText().length()==0){
                    previous_name.setError("Enter previous username");
                }
                if(new_name.getText().length()==0){
                    new_name.setError("Enter new username");
                }
                if(new_name.getError()!=null || previous_name.getError()!=null){
                    Toast.makeText(change_username_page.this,"Fill in the required fields",Toast.LENGTH_LONG).show();
                }
                else if(!ScannerConstants.user.equals(previous_name.getText().toString())){
                    Toast.makeText(change_username_page.this,"Enter the previous username correctly",Toast.LENGTH_LONG).show();
                }
                if(ScannerConstants.user.equals(previous_name.getText().toString()) && ScannerConstants.user.equals(previous_name.getText().toString()) && ScannerConstants.user.equals(new_name.getText().toString())){
                    Toast.makeText(change_username_page.this,"Please enter a new username",Toast.LENGTH_LONG).show();
                }
                else if(ScannerConstants.user.equals(previous_name.getText().toString())){
                    change_username_code();
                }

            }
        });

    }
    public void change_username_code(){
        change_name_sql = "UPDATE "+ScannerConstants.login_data_table+" SET username='"+new_name.getText().toString()+"' WHERE username='"+ScannerConstants.user+"'";
        //only for problem solving members...

        change_id_sql = "UPDATE "+ScannerConstants.login_data_table+" SET id='"+new_name.getText().toString()+"' WHERE id='"+ScannerConstants.user+"'";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, USERNAME_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if(message.equals("1")){
                        Toast.makeText(change_username_page.this, "Username Changed Successfully", Toast.LENGTH_LONG).show();
                        try {
                            Thread.sleep(2000);
                            logout_account();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        //Toast.makeText(change_username_page.this, "Error", Toast.LENGTH_LONG).show();
                        Toast.makeText(change_username_page.this, change_name_sql, Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(change_username_page.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(change_username_page.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name_query", change_name_sql);
                params.put("post", ScannerConstants.member_post);//check member post in php script
                //if member post is employee then don't process the id_sql just change_name
                //else just leave it there
                params.put("id_query", change_id_sql);


                return params;
            }
        };
        MySingleton.getInstance(change_username_page.this).addToRequestQue(stringRequest);
    }
    public void logout_account(){
        Intent intent = new Intent(getApplicationContext(), login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
