package com.example.malik.foodstore.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.malik.foodstore.R;
import com.example.malik.foodstore.adapters.OrderTrackAdapter;
import com.example.malik.foodstore.model.OrderList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by malik on 7/4/2017.
 */

public class OrdersFragment extends Fragment {
    EditText et_number;
    Button bt_trackOrder;
    private static String url_final;
    private static final String TAG = OrdersFragment.class.getSimpleName();
    private String url = "http://rjtmobile.com/ansari/fos/fosapp/order_recent.php?&user_phone=";
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<OrderList> orderList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       View view = inflater.inflate(R.layout.orders_fragment,container,false);
        et_number = (EditText)view.findViewById(R.id.et_number);
        bt_trackOrder = (Button) view.findViewById(R.id.bt_trackOrder);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerViewOrders);
        recyclerView.setHasFixedSize(true);
        //custom layout for recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        orderList = new ArrayList<>();
        bt_trackOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num = et_number.getText().toString();
                url_final = url+num;
                myloadRecylerView();
            }
        });

        return view;
    }

    private void myloadRecylerView() {
        Log.i(TAG,"myloadRecyclerView");
        StringRequest sr = new StringRequest(Request.Method.GET, url_final, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {
                // no hardcoding of strings
                Log.i(TAG,response.toString());

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray categories = jsonObject.getJSONArray("Order Detail");
                    for (int i = 0; i < categories.length(); i++) {
                        JSONObject item = categories.getJSONObject(i);
                        OrderList ls = new OrderList(item.getString("OrderId"), item.getString("OrderName"),
                                item.getString("OrderQuantity"),item.getString("TotalOrder"),item.getString("OrderStatus") );
                        orderList.add(ls);
                    }

                    adapter = new OrderTrackAdapter(orderList,getActivity().getApplicationContext());
                    recyclerView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        requestQueue.add(sr);

    }
}
