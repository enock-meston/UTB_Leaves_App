package com.nigoote.utb_leave_app;


import static com.nigoote.utb_leave_app.LoginActivity.SHARED_PREFERENCES_NAME;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ApplyActivity extends AppCompatActivity {

    Spinner spinnerModel;
    SharedPreferences sharedPreferences;

    ArrayList<String> supervisorList = new ArrayList<>();
    String[] supervisorArr;
    ArrayAdapter<String> supervisorAdapter;

    EditText dateFrom,dateTo;
    DatePickerDialog datePickerDialog;
    Button submitbtn;
    Constant constant;
    String URL = Constant.host;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        TextView idd = (TextView) findViewById(R.id.leaveID_txt);
        submitbtn = (Button) findViewById(R.id.submit_button_apply);


//        click to pick date
        dateFrom = (EditText) findViewById(R.id.dateFrom);
        dateTo = (EditText) findViewById(R.id.dateTo);
        dateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ApplyActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateFrom.setText(year + "-"
                                        + (monthOfYear + 1) + "-" +dayOfMonth );

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });


//        ========dateTo===============
        dateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calender class's instance and get current date , month and year from calender
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                // date picker dialog
                datePickerDialog = new DatePickerDialog(ApplyActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateTo.setText(year + "-"
                                        + (monthOfYear + 1) + "-" +dayOfMonth );

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

//        end of pick pick date

// this sharedPref comes from  RequestFragment
        sharedPreferences = getSharedPreferences("leaveSharedName", Context.MODE_PRIVATE);
        String str_name = sharedPreferences.getString("LeaveTitle", "");
        String str_Leave_ID = sharedPreferences.getString("LeaveID", "");
        String str_Leave_Days = sharedPreferences.getString("LeaveDay", "");

//        my id
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String str_my_ID = sharedPreferences.getString("id", "");
//set Title to Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar3);
        setSupportActionBar(toolbar);
//        end of toolBar

        idd.setText(str_name +"  "+ str_Leave_ID);
        spinnerModel = (Spinner) findViewById(R.id.spinner_supervisor);

//       select supervisor
        selectSupervisor();

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_dateFrom = dateFrom.getText().toString();
                String str_dateTo = dateTo.getText().toString();
                sendRequest(str_Leave_ID,str_Leave_Days,str_dateFrom,str_dateTo,str_my_ID,spinnerModel.getSelectedItemId());
            }
        });


    }

    private void sendRequest(String str_leave_id,String str_Leave_Days,String str_dateFrom, String str_dateTo, String str_my_id, long selectedItemId) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("Registration process...");
        progressDialog.show();
        Constant constant = new Constant(ApplyActivity.this,"Message");
        String url1 = URL+"/utb_api1/apply_leave.php";
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest request = new StringRequest(Request.Method.POST, url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Successfully_Submitted")){
                    Log.d("enock",response+"working");
                    progressDialog.dismiss();
                    constant.openDialog(response);
                }else{
                    progressDialog.dismiss();
                    constant.openDialog(response);
//
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

                param.put("LeaveType",str_leave_id);
                param.put("Leave_Days",str_Leave_Days);
                param.put("LeaveFrom",str_dateFrom);
                param.put("LeaveTo",str_dateTo);
                param.put("LeaveEmployee",str_my_id);
                param.put("Department", String.valueOf(spinnerModel.getSelectedItemId()+1));
                return param;
            }
        };
        requestQueue.add(request);
    }
    
    private void selectSupervisor() {
        String URL1 = Constant.host +"/utb_api1/select_user_depart.php";
        StringRequest request= new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray userArray = new JSONArray(response);
                    supervisorArr = new String[userArray.length()];

                    for (int user =0; user< userArray.length();user++){
                        JSONObject UserObj = userArray.getJSONObject(user);
                        String superVisorID = UserObj.optString("UserId");
                        String superVisorNames = UserObj.optString("Names");
                        String dept = UserObj.optString("staff_dept_full_name");

                        supervisorList.add(dept);
                        supervisorArr[user] = dept;
                    }
                    supervisorAdapter = new ArrayAdapter<>(ApplyActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,supervisorArr);
                    spinnerModel.setAdapter(supervisorAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(request);
    }
}