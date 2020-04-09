package com.example.testweatherproject.classes;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testweatherproject.R;

public class CustomToast {
    public void toast(Context context, String message){

        LayoutInflater inflater = LayoutInflater.from(context);

        View layout = inflater.inflate(R.layout.toast_layout,
                (ViewGroup) ((Activity) context).findViewById(R.id.toast_layout_root));
        // set a message
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(message);

        // Toast...
        Toast toast = new Toast(context);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
