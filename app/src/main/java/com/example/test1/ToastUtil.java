package com.example.test1;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
      private Context context;
      public ToastUtil(Context context) {
      this.context=context;
      }
    private static Toast toast;
    public static void showToast(Context context, int code, String content)
    {
        //code=1时Toast显示的时间长，code=0时显示的时间短。
        if (toast==null)
        {
            if (code ==0) {
                toast = Toast.makeText(context, content, Toast.LENGTH_SHORT);
                toast.setText(content);
            }
            if (code==1) {
                toast = Toast.makeText(context, content, Toast.LENGTH_LONG);
                toast.setText(content);
            }
        }
        else
        {
            toast.setText(content);
        }
        toast.show();
    }
}
