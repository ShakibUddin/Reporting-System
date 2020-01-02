package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

public class signup extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private EditText id;
    private EditText ConfirmPassword;
    private Button signup;
    private Switch viewPassword;
    private String inputUserName;
    private int i=0;
    private boolean pass=false;
    private String inputid;
    private String inputpost="Employee";
    private String inputPassWord;
    private String inputConfirmPassWord;
    private String passwordMessage="Password does not match.";
    public static final String UPLOAD_URL = "http://"+ScannerConstants.ip+"/ReportingSystem/signupAPI.php";

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username=(EditText)findViewById(R.id.usernameid);
        id=(EditText)findViewById(R.id.employee_org_id);
        password=(EditText)findViewById(R.id.passwordid);
        ConfirmPassword=(EditText)findViewById(R.id.confirmpasswordid);
        signup=(Button)findViewById(R.id.buttonsignup);
        viewPassword=(Switch)findViewById(R.id.viewpassid);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        ConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        viewPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    password.setInputType(InputType.TYPE_CLASS_TEXT);
                    ConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT);

                }
                else{
                    password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputUserName=username.getText().toString().trim();
                if(inputUserName.length()==0){
                    username.setError("Set username");
                }
                inputid=id.getText().toString().trim();
                for(i=0;i<inputid.length();++i){
                    char element=inputid.charAt(i);
                    if(!Character.isDigit(element)){
                        id.setError("Only numeric value is allowed in ID");
                    }
                }
                inputPassWord=password.getText().toString().trim();
                if(inputPassWord.length()==0){
                    password.setError("Set password");
                }
                inputConfirmPassWord=ConfirmPassword.getText().toString().trim();
                if(inputConfirmPassWord.length()==0){
                    ConfirmPassword.setError("Confirm your password");
                }
                if(username.getError()!=null || id.getError()!=null || password.getError()!=null || ConfirmPassword.getError()!=null){
                    Toast.makeText(signup.this, "Please, Fill up required fields", Toast.LENGTH_LONG).show();
                }
                if(username.getError()==null && id.getError()==null && inputPassWord.equals(inputConfirmPassWord)){
                    uploadData();
                    goToLoginPage();
                }
                else{
                    Toast.makeText(signup.this, passwordMessage, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void goToLoginPage(){
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }
    private void  uploadData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1")){
                        Toast.makeText(signup.this, "Registration Completed. Please Login", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(signup.this, "Username or ID already exists in database", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(signup.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username", inputUserName);
                params.put("password", inputPassWord);
                params.put("id", inputid);
                params.put("post", inputpost);


                return params;
            }
        };
        MySingleton.getInstance(signup.this).addToRequestQue(stringRequest);
    }
}

