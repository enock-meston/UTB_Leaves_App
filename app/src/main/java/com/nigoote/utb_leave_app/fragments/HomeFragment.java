package com.nigoote.utb_leave_app.fragments;

import static com.nigoote.utb_leave_app.LoginActivity.SHARED_PREFERENCES_NAME;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nigoote.utb_leave_app.Constant;
import com.nigoote.utb_leave_app.HomeActivity;
import com.nigoote.utb_leave_app.LoginActivity;
import com.nigoote.utb_leave_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {


    SharedPreferences sharedPreferences;
    TextView MyLeavesNum,MyPendingNum;
    String HOST = Constant.host;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        MyLeavesNum = view.findViewById(R.id.dash_myLeaves);
        MyPendingNum = view.findViewById(R.id.dash_pending);

//        shared Preferences
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String str_userid = sharedPreferences.getString("id", "");
        loadDashboard(str_userid);
        loadDashboard1(str_userid);



        return view;
    }

    private void loadDashboard(String str_userid) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("DashBoard");
        progressDialog.show();

        Constant constant = new Constant(getContext(),"Message");
        String url2 = HOST+"/utb_api1/DashBoard.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray resp = new JSONArray(response);
                    JSONObject respObj = resp.getJSONObject(0);
                    String myLeaves = respObj.getString("myLeaves");
                    progressDialog.dismiss();
                    if (myLeaves!=null){
                        MyLeavesNum.setText(myLeaves);
                    }
                    else{
                        MyLeavesNum.setText("OO");
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
                Constant constant = new Constant(getContext(),"Error from Volley");
                constant.openDialog(error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("LeaveEmployeeID",str_userid);
                return param;
            }
        };

//        add request to requestQueue
        requestQueue.add(request);
    }
//function two
    private void loadDashboard1(String str_userid) {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        progressDialog.setTitle("DashBoard");
        progressDialog.show();

        Constant constant = new Constant(getContext(),"Message");
        String url2 = HOST+"/utb_api1/DashBoard1.php";
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONArray resp = new JSONArray(response);
                    JSONObject respObj = resp.getJSONObject(0);
                    String pen = respObj.getString("pen");
                    progressDialog.dismiss();
                    if (pen!=null){
                        MyPendingNum.setText(pen);
                    }
                    else{
                        MyPendingNum.setText("OO");
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
                Constant constant = new Constant(getContext(),"Error from Volley");
                constant.openDialog(error.toString());
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<String,String>();
                param.put("LeaveEmployeeID",str_userid);
                return param;
            }
        };

//        add request to requestQueue
        requestQueue.add(request);
    }
    }
