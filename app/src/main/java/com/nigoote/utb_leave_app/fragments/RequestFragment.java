package com.nigoote.utb_leave_app.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nigoote.utb_leave_app.ApplyActivity;
import com.nigoote.utb_leave_app.Constant;
import com.nigoote.utb_leave_app.DataLeave;
import com.nigoote.utb_leave_app.LeavesListAdapter;
import com.nigoote.utb_leave_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RequestFragment extends Fragment implements LeavesListAdapter.SelectLeaves{

    RecyclerView list_request;
    LeavesListAdapter leavesListAdapter;
    List<DataLeave> leaveList;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public RequestFragment() {
        // Required empty public constructor
    }

    public static RequestFragment newInstance(String param1, String param2) {
        RequestFragment fragment = new RequestFragment();
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
        View view = inflater.inflate(R.layout.fragment_request, container, false);

        list_request = view.findViewById(R.id.leaves_list);
        leaveList = new ArrayList<>();
        leavesListAdapter= new LeavesListAdapter(getContext(),leaveList,this);

        loadLeavesMethod();
        
        return view;
    }

    private void loadLeavesMethod() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String URL1 = Constant.host +"/utb_api1/req_leaves.php";


        StringRequest request= new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray leaveArray = new JSONArray(response);

                    for (int l =0; l< leaveArray.length();l++){

                        JSONObject leaveObj = leaveArray.getJSONObject(l);

                        leaveList.add(new DataLeave(
                                leaveObj.getString("TypeId"),
                                leaveObj.getString("LeaveName"),
                                leaveObj.getString("LeaveDays")
                        ));
                    }

                    list_request.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    list_request.setAdapter(leavesListAdapter);
                    Toast.makeText(getContext(), "Hello1", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Constant constant = new Constant(getContext(),"Message");
                    constant.openDialog("Message"+e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Hello3", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(getContext()).add(request);
    }

    @Override
    public void selectLeaves(DataLeave dataLeave) {
        Intent i = new Intent(getContext(), ApplyActivity.class);
        startActivity(i);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("leaveSharedName", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LeaveID", dataLeave.getLid());
        editor.putString("LeaveTitle", dataLeave.getTitle());
        editor.putString("LeaveDay", dataLeave.getDays());
        editor.commit();

    }
}