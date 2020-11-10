package com.valiant.numbersfacts.ui.calender_facts;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.valiant.numbersfacts.R;

import java.lang.reflect.Array;

public class CalenderFacts extends Fragment {

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
    }
}