package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
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
import java.util.LinkedList;
import java.util.Map;

public class member_signup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText username;
    private EditText password;
    private Spinner post;
    private EditText ConfirmPassword;
    private Button signup;
    private Switch viewPassword;
    private String inputUserName;
    private String inputpost="";
    private String inputid="";
    private String inputPassWord;
    private String inputConfirmPassWord;
    private String passwordMessage="Password does not match.";;
    private LinkedList<String> members=new LinkedList<String>();
    public static final String UPLOAD_URL = "http://"+ScannerConstants.ip+"/ReportingSystem/signupAPI.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_signup);

        username=(EditText)findViewById(R.id.memberusernameid);
        post=(Spinner)findViewById(R.id.memberpostid);
        password=(EditText)findViewById(R.id.memberpasswordid);
        ConfirmPassword=(EditText)findViewById(R.id.memberconfirmpasswordid);
        signup=(Button)findViewById(R.id.memberbuttonsignup);
        viewPassword=(Switch)findViewById(R.id.memberviewpassid);
        password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        ConfirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        members.add("Post");//if inputpost==post then show error
        members.add("Librarian");//for library issues
        members.add("Electrician");//for electric issues
        members.add("Lab Coordinator");//for lab issues
        members.add("Room Coordinator");//for room issues
        members.add("Moderator");//for other issues
        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, members);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        post.setAdapter(Adapter);
        post.setOnItemSelectedListener(this);
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
                inputpost=post.getSelectedItem().toString().trim();
                inputPassWord=password.getText().toString().trim();
                if(inputPassWord.length()==0){
                    password.setError("Set password");
                }
                inputConfirmPassWord=ConfirmPassword.getText().toString().trim();
                if(inputConfirmPassWord.length()==0){
                    ConfirmPassword.setError("Confirm your password");
                }
                if(inputpost.equals("") || inputpost.equals("Post")){
                    post.setBackgroundColor(Color.RED);
                    post.setPopupBackgroundResource(R.drawable.spinner_error);
                }
                if(inputpost.length()>0 && !inputpost.equals("Post")){
                    post.setBackgroundColor(Color.GREEN);
                    post.setPopupBackgroundResource(R.drawable.spinner_background);
                }
                if(username.getError()!=null || password.getError()!=null || ConfirmPassword.getError()!=null || inputpost.length()==0|| inputpost.equals("Post")){
                    Toast.makeText(member_signup.this, "Please, Fill up required fields", Toast.LENGTH_LONG).show();
                }
                else if(username.getError()==null && inputpost.length()>0 && inputPassWord.equals(inputConfirmPassWord)){
                    uploadData();
                }
                else{
                    Toast.makeText(member_signup.this, passwordMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void  uploadData(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    if(success.equals("1")){
                        Toast.makeText(member_signup.this, "Registration Completed.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(member_signup.this, "Username already exists in database", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(member_signup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(member_signup.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                inputid=inputUserName;//members username is chosen as members id...for now
                params.put("username", inputUserName);
                params.put("password", inputPassWord);
                params.put("id", inputid);
                params.put("post", inputpost);

                return params;
            }
        };
        MySingleton.getInstance(member_signup.this).addToRequestQue(stringRequest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
