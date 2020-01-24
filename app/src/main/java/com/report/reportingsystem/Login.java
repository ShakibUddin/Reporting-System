package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
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

public class Login extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private Button login;
    private Button signup;
    private String inputUserName="";
    private String inputPassWord="";
    private Switch viewPassword;
    public static final String LOGIN_URL = "http://"+ScannerConstants.ip+"/ReportingSystem/reportSystemLoginAPI.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        username=(EditText)findViewById(R.id.usernameid);
        password=(EditText)findViewById(R.id.passwordid);
        login=(Button)findViewById(R.id.buttonlogin);
        signup=(Button)findViewById(R.id.buttonsignup);
        viewPassword=(Switch)findViewById(R.id.viewpassid);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        viewPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                }
                else{
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUserName=username.getText().toString().trim();
                inputPassWord=password.getText().toString().trim();
                if(inputUserName.isEmpty()){
                    username.setError("Please enter username");

                }
                if(inputPassWord.isEmpty()){
                    password.setError("Please enter password");
                }
                if(inputUserName.equals("admin") && inputPassWord.equals("1234")){
                    ScannerConstants.user="Admin";
                    adminpage();
                }
                else{
                    login(inputUserName,inputPassWord);
                }

            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });
    }
    public void login(final String username, String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    String member = jsonObject.getString("member");
                    String password = jsonObject.getString("password");
                    if(success.equals("1")){
                        ScannerConstants.user=inputUserName;
                        ScannerConstants.id=jsonObject.getString("id");//saving employee id,
                        // so he/she can'tsubmit report using someone elses id
                        employeepage();
                    }
                    else if(success.equals("2")){
                        ScannerConstants.user=inputUserName;
                        ScannerConstants.member_post=member;
                        memberpage();
                    }
                    else{
                        Toast.makeText(Login.this,  "Username or Password is incorrect", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Login.this, "Username or Password is incorrect", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Login.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username", inputUserName);
                params.put("password", inputPassWord);

                return params;
            }
        };
        MySingleton.getInstance(Login.this).addToRequestQue(stringRequest);

    }
    public void signup(){
        Intent intent = new Intent(this, SignUp.class);
        startActivity(intent);
    }
    public void employeepage(){
        Intent intent = new Intent(this, EmployeeHomePage.class);
        startActivity(intent);
    }
    public void adminpage(){
        Intent intent = new Intent(this, AdminHomePage.class);
        startActivity(intent);
    }
    private void memberpage(){
        Intent intent = new Intent(this, MemberPage.class);
        startActivity(intent);
    }
}
