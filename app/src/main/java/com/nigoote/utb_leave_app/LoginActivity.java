package com.nigoote.utb_leave_app;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    public static final String SHARED_PREFERENCES_NAME = "login_portal";
    TextView nextActi;
    Button loginAction;
    EditText email,password;
    String HOST = Constant.host;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nextActi = (TextView) findViewById(R.id.l_new);
        loginAction = (Button) findViewById(R.id.login);
        email = (EditText) findViewById(R.id.l_email);
        password = (EditText) findViewById(R.id.l_pwd);

//        go to create account Activity
        nextActi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this,New_Account_Activity.class);
                finish();
                startActivity(i);
            }
        });

//        login button with method
        loginAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String txtEmail= email.getText().toString();
                String txtPass= password.getText().toString();
                if (TextUtils.isEmpty(txtEmail) || TextUtils.isEmpty(txtPass)){
                    Toast.makeText(LoginActivity.this, "All Fields are Required", Toast.LENGTH_SHORT).show();
                }else {
                    loginMethod(txtEmail, txtPass);
                }
            }
        });
    }

    private void loginMethod(String txtEmail, String txtPass) {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Login ...");
        progressDialog.show();

        Constant constant = new Constant(LoginActivity.this,"Message");
        String url2 = HOST+"/utb_api1/login.php";
        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        StringRequest request = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//one
                try {

                    JSONObject resp = new JSONObject(response);
                    String res = resp.getString("statuss");
                    String user_id = resp.optString("UserId");
                    String name1 = resp.optString("Names");
                    String email1 = resp.optString("Email");
                    String dept1 = resp.optString("Position");
                    String campus = resp.optString("Campus");
                    if(res.contains("Login_Success")){
                        progressDialog.dismiss();

                        if (res.equals("Login_Success")){
                            progressDialog.dismiss();
                            Log.d("LogResp", name1);
                            //shared pref
                            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("id", user_id);
                            editor.putString("names", name1);
                            editor.putString("email", email1);
                            editor.putString("dept", dept1);
                            editor.putString("campus", campus);
                            editor.commit();

//g
                            Toast.makeText(LoginActivity.this, res, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                            finish();
                            startActivity(intent);
                            // clean data in EditText
                            constant.cleanData(email,password);
                        }
                    }else if(res.contains("Wrong_Email")){
                        progressDialog.dismiss();
                        constant.openDialog("Wrong Email");
                    }else if(res.contains("Wrong_Password")){
                        progressDialog.dismiss();
                        constant.openDialog("Wrong Password...");
                    }else{
                        progressDialog.dismiss();
                        constant.openDialog("Connection_Error");
                    }
                } catch (JSONException e) {
                    progressDialog.dismiss();
                    constant.openDialog("error 1"+e.toString());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Constant constant = new Constant(LoginActivity.this,"Error from Volley");
                constant.openDialog("error 1"+ error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("username",txtEmail);
                param.put("password",txtPass);
                return param;
            }
        };

//        add request to requestQueue
        requestQueue.add(request);
    }
}