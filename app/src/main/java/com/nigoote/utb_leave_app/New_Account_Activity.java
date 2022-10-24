package com.nigoote.utb_leave_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class New_Account_Activity extends AppCompatActivity {

    TextView backLoggin;
    EditText names,email,password,repassword;
    Button newBtn;
    Constant constant;
    String URL = Constant.host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_account);

        backLoggin = (TextView) findViewById(R.id.go_login);
        names = (EditText) findViewById(R.id.e_name);
        email = (EditText) findViewById(R.id.e_email);
        password = (EditText) findViewById(R.id.e_pwd);
        repassword = (EditText) findViewById(R.id.re_pwd);
        newBtn = (Button) findViewById(R.id.submit_btn);


//        back to login
        backLoggin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(New_Account_Activity.this,LoginActivity.class);
                finish();
                startActivity(i);
            }
        });

//        make new account btn
        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String txtEmail= email.getText().toString();
                String txtPass= password.getText().toString();
                String txtNames= names.getText().toString();
                String txtRePass= repassword.getText().toString();
                constant = new Constant(New_Account_Activity.this,"Message");
                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPass)
                        || TextUtils.isEmpty(txtNames) || TextUtils.isEmpty(txtRePass)){
                    Toast.makeText(New_Account_Activity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                    constant.openDialog("ll Fields are Required!");
                }else {
                    createNewAccountMethod(txtNames, txtEmail,txtPass,txtRePass);
                }
            }
        });
    }

    private void createNewAccountMethod(String txtNames, String txtEmail, String txtPass, String txtRePass) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Registration process...");
        progressDialog.show();

        String url1 = URL+"/utb_api1/new_account.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Successfully_Registered")){
                    Log.d("enock",response+"working");
                    progressDialog.dismiss();
                    constant.openDialog(response);
                    clearEditText();
                }else{
                    progressDialog.dismiss();
                    constant.openDialog(response);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                constant.openDialog(error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();

                param.put("names",txtNames);
                param.put("email",txtEmail);
                param.put("password",txtPass);
                param.put("retype",txtRePass);
                return param;
            }
        };
        requestQueue.add(request);
    }

    public  void clearEditText(){
        names.setText("");
        email.setText("");
        password.setText("");
        repassword.setText("");
    }
}