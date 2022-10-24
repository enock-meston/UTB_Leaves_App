package com.nigoote.utb_leave_app.fragments;

import static com.nigoote.utb_leave_app.LoginActivity.SHARED_PREFERENCES_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nigoote.utb_leave_app.ActionListAdapter;
import com.nigoote.utb_leave_app.Constant;
import com.nigoote.utb_leave_app.DataActionStatus;
import com.nigoote.utb_leave_app.DataLeave;
import com.nigoote.utb_leave_app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyLeavesFragment extends Fragment {

    RecyclerView Recycler_action_status;
    ActionListAdapter adapter;
    List<DataActionStatus> ActionList;
    SharedPreferences sharedPreferences;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MyLeavesFragment() {
        // Required empty public constructor
    }

    public static MyLeavesFragment newInstance(String param1, String param2) {
        MyLeavesFragment fragment = new MyLeavesFragment();
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
        View view=  inflater.inflate(R.layout.fragment_my_leaves, container, false);

        Recycler_action_status = (RecyclerView) view.findViewById(R.id.action_status_list);
        ActionList = new ArrayList<>();
        adapter = new ActionListAdapter(getContext(),ActionList);
        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE);
        String str_id = sharedPreferences.getString("id", "");
        loadLeavesMethod(str_id);

        return view;
    }
    private void loadLeavesMethod(String str_id) {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        String URL1 = Constant.host +"/UTB_API1/ActionStatus.php";
        StringRequest request= new StringRequest(Request.Method.POST, URL1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray ActionArray = new JSONArray(response);
                    for (int a =0; a< ActionArray.length();a++){
                        JSONObject ActionObj = ActionArray.getJSONObject(a);
                        ActionList.add(new DataActionStatus(
                                ActionObj.getString("LeaveName"),
                                ActionObj.getString("StatusSupervisor"),
                                ActionObj.getString("StatusHR"),
                                ActionObj.getString("StatusDVCPAF"),
                                ActionObj.getString("StatusDVCA"),
                                ActionObj.getString("StatusVC")
                        ));
                    }
                    Recycler_action_status.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                    Recycler_action_status.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> param = new HashMap<>();
                param.put("LeaveEmployeeID",str_id);
                return param;
            }
        };
        Volley.newRequestQueue(getContext()).add(request);
    }
}