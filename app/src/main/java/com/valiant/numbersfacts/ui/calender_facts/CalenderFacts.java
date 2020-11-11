package com.valiant.numbersfacts.ui.calender_facts;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.valiant.numbersfacts.R;
import com.valiant.numbersfacts.classes.Functions;
import com.valiant.numbersfacts.classes.MyApplication;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;

import java.lang.reflect.Array;

public class CalenderFacts extends Fragment {

    private final String TAG = this.getClass().getName();
    private final String DATE = "date";
    private AppCompatButton look_up_btn;
    private AVLoadingIndicatorView avi;
    private DatePicker datePicker;


    private View rootView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_calender_facts, container, false);
        return rootView;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatEditText input_date = view.findViewById(R.id.input_date);
        AppCompatEditText input_month = view.findViewById(R.id.input_month);
        avi = view.findViewById(R.id.avi);
        look_up_btn = view.findViewById(R.id.look_up_btn);

        datePicker = view.findViewById(R.id.date_picker);




        input_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String date = input_date.getText().toString().trim();

                if (date.length() == 2) {
                    if (!TextUtils.isEmpty(date) && Integer.parseInt(date) > 31) {
                        input_date.setError("Invalid");
                    } else {
                        input_month.requestFocus();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        input_month.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String month = input_month.getText().toString().trim();
                if (!TextUtils.isEmpty(month) && Integer.parseInt(month) > 12){
                    input_month.setError("Invalid");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        look_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String day = input_date.getText().toString().trim();
                String month = input_month.getText().toString().trim();

                String date = month + "/" + day;
                if (Functions.isDateValid(date, "mm/dd")){
                    loadData(date, DATE);
                } else {
                    input_month.setError("Invalid Date");
                }
            }
        });



    }

    private void loadData(final String date, final String type) {
        if (look_up_btn.getVisibility() == View.VISIBLE){
            look_up_btn.setVisibility(View.GONE);
            avi.smoothToShow();
        }

        String  tag_string_req = "number_request";

        String url = "http://numbersapi.com/" + date + "/" + type + "?json&notfound=floor";

        Log.e(TAG, "url = " + url);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                response -> {
                    Log.e(TAG, response.toString());
                    look_up_btn.setVisibility(View.VISIBLE);
                    avi.smoothToHide();



                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                avi.smoothToHide();
                look_up_btn.setVisibility(View.VISIBLE);
                Log.e(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
            }
        });

// Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(jsonObjReq, tag_string_req);
    }
}