package com.report.reportingsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class report_submission_page extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText id;
    private EditText username;
    private Spinner issue;
    private EditText report;
    private EditText imagename;
    private Button image;
    private Button submit;
    private Button refresh;
    private String inputId="";
    private String inputReport="";
    private String inputIssue="";
    private String currentPhotoPath;
    static final int REQUEST_TAKE_PHOTO = 1;
    private String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private String issues[] = {"Room","Electronics","Library","Lab","Other"};
    public static final String UPLOAD_URL = "http://"+ScannerConstants.ip+"/ReportingSystem/reportdata.php";
    private int SELECT_FILE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_submission_page);
        id=(EditText)findViewById(R.id.employeeid);
        username=(EditText)findViewById(R.id.employeeusername);
        issue=(Spinner) findViewById(R.id.issueid);
        report=(EditText)findViewById(R.id.reportid);
        submit=(Button)findViewById(R.id.submitbutton);
        refresh=(Button)findViewById(R.id.refreshbutton);
        image=(Button)findViewById(R.id.imagebutton);

        ScannerConstants.image= BitmapFactory.decodeResource(getApplicationContext().getResources(),
                R.drawable.default_image);
        ScannerConstants.image_name="default.png";

        ArrayAdapter<String> Adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, issues);
        Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        issue.setAdapter(Adapter);
        issue.setOnItemSelectedListener(this);

        username.setText(ScannerConstants.user);
        id.setText(ScannerConstants.id);
        id.setEnabled(false);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Build an AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(report_submission_page.this);

                // Set a title for alert dialog
                builder.setTitle("");

                // Ask the final question
                builder.setMessage("Choose a method");

                // Set click listener for alert dialog buttons
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                // User clicked the Yes button
                                openGallery();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                               openCamera();
                                break;
                        }
                    }
                };

                // Set the alert dialog yes button click listener
                builder.setPositiveButton("Gallery", dialogClickListener);

                // Set the alert dialog no button click listener
                builder.setNegativeButton("Camera",dialogClickListener);

                AlertDialog dialog = builder.create();
                // Display the alert dialog on interface
                dialog.show();
            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScannerConstants.image= BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.drawable.default_image);
                ScannerConstants.image_name="default.png";
                Toast.makeText(report_submission_page.this,"Image cleared",Toast.LENGTH_LONG).show();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputId=id.getText().toString().trim(); // match this id with users actual id or just fetch actual id from database and input in report form
                inputIssue=issue.getSelectedItem().toString();
                inputReport=report.getText().toString().trim();
                if( inputIssue.equals("")){
                    ((TextView)issue.getSelectedView()).setError("Choose an issue");
                }
                if(inputReport.equals("")){
                    report.setError("Invalid format");
                }
                if(!inputId.isEmpty() && !inputIssue.isEmpty() && !inputReport.isEmpty()){
                    uploadData();
                }
            }
        });

    }
    private void  uploadData() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    Toast.makeText(report_submission_page.this, Response, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(report_submission_page.this, "Check your internet connection", Toast.LENGTH_LONG).show();
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", inputId);
                params.put("issue", inputIssue);
                params.put("report", inputReport);
                params.put("name", ScannerConstants.image_name);
                params.put("image",imageToString(ScannerConstants.image));

                return params;
            }
        };
        MySingleton.getInstance(report_submission_page.this).addToRequestQue(stringRequest);
    }
    public void openGallery(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select File"),SELECT_FILE);
    }
    public void openCamera(){
        if(ContextCompat.checkSelfPermission(report_submission_page.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED)
        {
            Toast.makeText(report_submission_page.this,"Provide camera access from your settings",Toast.LENGTH_LONG).show();
        }
        else{
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //IMAGE CAPTURE CODE
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        }

    }
    public void onActivityResult(int request_code,int result_code,Intent data) {

        super.onActivityResult(request_code, result_code, data);

        if(result_code== Activity.RESULT_OK){
            if(request_code==SELECT_FILE){
                Uri uri = data.getData();
                try {
                    ScannerConstants.image=MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                    Toast.makeText(report_submission_page.this,"Image selection successful",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(request_code==REQUEST_IMAGE_CAPTURE){
                Bundle extras = data.getExtras();
                ScannerConstants.image =(Bitmap) extras.get("data");
                Toast.makeText(report_submission_page.this,"Image capture successful",Toast.LENGTH_SHORT).show();
            }
            ScannerConstants.image_name=timeStamp + ".png";
        }

    }
    private String imageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes = byteArrayOutputStream.toByteArray();
        return  Base64.encodeToString(imgBytes,Base64.DEFAULT);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
