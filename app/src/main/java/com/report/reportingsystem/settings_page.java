package com.report.reportingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class settings_page extends AppCompatActivity {

    private Button change_username;
    private Button change_password;
    private Button delete_account;
    private String account_delete_sql="";
    private String change_name_sql="";
    private String change_passsword_sql="";

    private String PASSOWRD_URL="http://"+ScannerConstants.ip+"/ReportingSystem/change_data.php";
    private String DELETE_ACCOUNT_URL="http://"+ScannerConstants.ip+"/ReportingSystem/delete_account.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_page);

        change_username=(Button)findViewById(R.id.change_username_button);
        change_password=(Button)findViewById(R.id.change_password_button);
        delete_account=(Button)findViewById(R.id.delete_account_button);


        change_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_username_page();
            }
        });
        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change_password_page();
            }
        });
        delete_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(settings_page.this);
                alertDialog.setTitle("Alert");
                alertDialog.setMessage("Do you really want to delete your account?");
                alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(settings_page.this,"Thank You.",Toast.LENGTH_LONG).show();
                        delete_account_code();
                        logout_account();
                    }
                });
                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alert = alertDialog.create();
                alert.setCanceledOnTouchOutside(true);
                alert.show();
            }
        });
    }

    public void change_username_page(){
        Intent intent = new Intent(this,change_username_page.class);
        startActivity(intent);
    }
    public void change_password_page(){
        Intent intent = new Intent(this,change_password_page.class);
        startActivity(intent);
    }
    public void delete_account_code(){
        account_delete_sql = "DELETE "+" FROM "+ScannerConstants.login_data_table+" WHERE username='"+ScannerConstants.user+"'";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DELETE_ACCOUNT_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toast.makeText(settings_page.this, Response, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(settings_page.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(settings_page.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("query", account_delete_sql);

                return params;
            }
        };
        MySingleton.getInstance(settings_page.this).addToRequestQue(stringRequest);
    }
    public void logout_account(){
        Intent intent = new Intent(getApplicationContext(), login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
