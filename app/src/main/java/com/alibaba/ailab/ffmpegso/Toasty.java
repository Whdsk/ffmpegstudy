package com.alibaba.ailab.ffmpegso;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Author gaohangbo
 * @Date 2021 12/23/21 5:03 下午
 * @Describe
 */
public class Toasty {

    public static void showToast(String str){
        View view = LayoutInflater.from(FFmpegApplication.getInstance().getApplicationContext()).inflate(R.layout.toast_layout,null);
        TextView tv_msg = view.findViewById(R.id.toast_text);
        tv_msg.setText(str);
        Toast toast = new Toast(FFmpegApplication.getInstance().getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }


}
