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

public class ChangePasswordPage extends AppCompatActivity {

    private Button change;
    private EditText previous_password;
    private EditText new_password;
    private EditText confirm_new_password;
    private String PASSWORD_URL="http://"+ScannerConstants.ip+"/ReportingSystem/change_password.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_page);

        change=(Button)findViewById(R.id.change_password_button);
        previous_password=(EditText)findViewById(R.id.previous_passwordid);
        new_password=(EditText)findViewById(R.id.new_passwordid);
        confirm_new_password=(EditText)findViewById(R.id.confirm_new_passwordid);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //user can also change to previous password(this is for security purpose, this can be changed)
                if( previous_password.getText().length()==0){
                    previous_password.setError("Enter previous password");
                }
                if(new_password.getText().length()==0){
                    new_password.setError("Enter new password");
                }
                if(confirm_new_password.getText().length()==0){
                    confirm_new_password.setError("Confirm new password");
                }
                if(new_password.getError()!=null || previous_password.getError()!=null || confirm_new_password.getError()!=null){
                    Toast.makeText(ChangePasswordPage.this,"Fill in the required fields",Toast.LENGTH_LONG).show();
                }
                else{
                    change_password_code();
                }



            }
        });
    }
    public void change_password_code(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PASSWORD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String message = jsonObject.getString("message");
                    if(message.equals("1")){
                        Toast.makeText(ChangePasswordPage.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();
                        try {
                            Thread.sleep(2000);
                            logout_account();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    else if(message.equals("2")){
                        Toast.makeText(ChangePasswordPage.this, "Passwords don't match", Toast.LENGTH_LONG).show();
                    }
                    else if(message.equals("3")){
                        Toast.makeText(ChangePasswordPage.this, "Please, Enter previous password correctly", Toast.LENGTH_LONG).show();
                    }
                    else if(message.equals("-1")){
                        Toast.makeText(ChangePasswordPage.this, "Passsword could not be changed", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(ChangePasswordPage.this, "Error", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ChangePasswordPage.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ChangePasswordPage.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        })
        {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", ScannerConstants.user);
                params.put("input_password", previous_password.getText().toString());
                params.put("new_password", new_password.getText().toString());
                params.put("confirm_new_password", confirm_new_password.getText().toString());
                return params;
            }
        };
        MySingleton.getInstance(ChangePasswordPage.this).addToRequestQue(stringRequest);
    }
    public void logout_account(){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
