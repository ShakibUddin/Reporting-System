package com.report.reportingsystem;

import androidx.appcompat.app.AppCompatActivity;

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

public class MemberSignup extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

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
    private int check=0;
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
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.post,
                R.layout.color_spinner_layout
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        post.setAdapter(adapter);
        //post.setOnItemSelectedListener(this);
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
                if(inputpost.equals("")){
                    Toast.makeText(MemberSignup.this,"Select post",Toast.LENGTH_LONG).show();
                }
                if(inputpost.length()>0 && !inputpost.equals("Post")){
                    post.setBackgroundResource(R.drawable.submitbutton);
                    post.setPopupBackgroundResource(R.drawable.submitbutton);
                }
                if(username.getError()!=null || password.getError()!=null || ConfirmPassword.getError()!=null || inputpost.length()==0|| inputpost.equals("Post")){
                    Toast.makeText(MemberSignup.this, "Please, Fill up required fields", Toast.LENGTH_LONG).show();
                }
                else if(username.getError()==null && inputpost.length()>0 && inputPassWord.equals(inputConfirmPassWord)){
                    uploadData();
                }
                else{
                    Toast.makeText(MemberSignup.this, passwordMessage, Toast.LENGTH_LONG).show();
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
                        Toast.makeText(MemberSignup.this, "Registration Completed.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(MemberSignup.this, "Username already exists in database", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MemberSignup.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MemberSignup.this, "Check your internet connection", Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(MemberSignup.this).addToRequestQue(stringRequest);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }


    public void onNothingSelected(AdapterView<?> parent) {

    }
}
