package com.valiant.numbersfacts.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.valiant.numbersfacts.R;
import com.valiant.numbersfacts.classes.MyApplication;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private final String TAG = this.getClass().getName();
    private final String TRIVIA = "trivia";
    private final String MATH = "math";
    private final String RANDOM = "random";
    private final String TYPE = "type";
    private final String FOUND = "found";
    private View rootView;
    private AppCompatTextView txt_trivia, txt_math, txt_do_you_know;
    private AVLoadingIndicatorView avi;
    private AppCompatButton look_up_btn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AppCompatEditText input_number = view.findViewById(R.id.input_number);
        look_up_btn = view.findViewById(R.id.look_up_btn);
        txt_math = view.findViewById(R.id.txt_math);
        txt_do_you_know = view.findViewById(R.id.do_you_know_txt);
        txt_trivia = view.findViewById(R.id.txt_trivia);
        avi = view.findViewById(R.id.avi);

        loadData(RANDOM, TRIVIA);
        look_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(input_number.getText().toString())){
                    input_number.setError("Empty fields");
                } else {
                    loadData(input_number.getText().toString(), TRIVIA);
                    input_number.setText("");
                    input_number.clearFocus();
                }
            }
        });

    }

    private void loadData(final String number, final String type) {
        if (look_up_btn.getVisibility() == View.VISIBLE){
            look_up_btn.setVisibility(View.GONE);
            avi.smoothToShow();
        }

        String  tag_string_req = "number_request";

        String url = "http://numbersapi.com/" + number + "/" + type + "?json&notfound=floor";

        Log.e(TAG, "url = " + url);


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                response -> {
                    Log.e(TAG, response.toString());
                    if (type.equals(MATH)){
                        avi.smoothToHide();
                        look_up_btn.setVisibility(View.VISIBLE);
                    }

                    try {
                        String text = response.getString("text");
                        boolean found = response.getBoolean(FOUND);
                        String number1 = response.getString("number");
                        String type1 = response.getString(TYPE);

                        if (type1.equals(TRIVIA)){
                            if (number.equals(RANDOM)){
                                loadData(number1, MATH);
                            } else {
                                loadData(number, MATH);
                            }
                            txt_trivia.setText(text);
                            if (found){
                                if (number.equals(RANDOM)){
                                txt_do_you_know.setText("Do you know the facts about number " + number1);
                                } else {
                                    txt_do_you_know.setText("Here is the facts of your number " + number1);
                                }
                            } else {
                                txt_do_you_know.setTextColor(Color.RED);
                                txt_do_you_know.setText("We couldn't find anything for your number" +  number + ". Let's see the facts about nearest number " + number1);
                            }
                        } else {
                            txt_math.setText(text);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


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