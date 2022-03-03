package com.example.test1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String VIDEO_NAME = "welcome_video.mp4";//要播放的视频name

    private VideoView mVideoView;//视频view对象

    private InputType inputType = InputType.NONE;

    private Button buttonLeft, buttonRight;//左右两个按钮

    private FormView formView;//表单view

    private ViewGroup contianer;

    private TextView appName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            window.setFlags(
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
//                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();//隐藏顶部

        findView();

        initView();

        File videoFile = getFileStreamPath(VIDEO_NAME);
        if (!videoFile.exists()) {
            videoFile = copyVideoFile();
        }

        playVideo(videoFile);

        playAnim();
    }

    private void findView() {
        mVideoView = (VideoView) findViewById(R.id.videoView);
        buttonLeft = (Button) findViewById(R.id.buttonLeft);
        buttonRight = (Button) findViewById(R.id.buttonRight);
        contianer = (ViewGroup) findViewById(R.id.container);
        formView = (FormView) findViewById(R.id.formView);
        appName = (TextView) findViewById(R.id.appName);
        formView.post(new Runnable() {
            @Override
            public void run() {
//                int delta = formView.getTop()+formView.getHeight();//本来的位置
//               formView.setTranslationY(-1 * delta);//隐藏掉
            }
        });
    }

    private void initView() {

        buttonRight.setOnClickListener(this);
        buttonLeft.setOnClickListener(this);
    }

    private void playVideo(File videoFile) {
        mVideoView.setVideoPath(videoFile.getPath());
        mVideoView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
            }
        });
    }
    private void playAnim() {
        ObjectAnimator anim = ObjectAnimator.ofFloat(appName, "alpha", 0,1);
        anim.setDuration(4000);
        anim.setRepeatCount(1);
        anim.setRepeatMode(ObjectAnimator.REVERSE);
        anim.start();
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                appName.setVisibility(View.INVISIBLE);
            }
        });
    }

    @NonNull
    private File copyVideoFile() {
        File videoFile;
        try {
            FileOutputStream fos = openFileOutput(VIDEO_NAME, MODE_PRIVATE);
            InputStream in = getResources().openRawResource(R.raw.welcome_video);
            byte[] buff = new byte[1024];
            int len = 0;
            while ((len = in.read(buff)) != -1) {
                fos.write(buff, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        videoFile = getFileStreamPath(VIDEO_NAME);
        if (!videoFile.exists())
            throw new RuntimeException("video file has problem, are you sure you have welcome_video.mp4 in res/raw folder?");
        return videoFile;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }

    @Override
    public void onClick(View view) {//view的变化
        int delta = formView.getTop()+formView.getHeight();//每次点击都会获取当前控件在竖直方向位置
        switch (inputType) {
            case NONE:

                if (view == buttonLeft) {
                    Intent intent =(Intent) new Intent(this,ClassifierActivity.class);
                    formView.animate().translationY(0).alpha(1).setDuration(500).start();//alpha是透明度
                    inputType = InputType.LOGIN;
                    buttonLeft.setText("确认登录");//确认登录 原来是R.string.button_confirm_login
                    buttonRight.setText("取消登录");//取消登录 原来是R.string.button_cancel_login
                    startActivity(intent);//进入系统

                } else if (view == buttonRight) {
                    formView.animate().translationY(0).alpha(1).setDuration(500).start();//alpha是透明度
                    inputType = InputType.LOGIN;
                    buttonLeft.setText("确认登录");//确认登录 原来是R.string.button_confirm_login
                    buttonRight.setText("取消登录");//取消登录 原来是R.string.button_cancel_login
                }

                break;
            case LOGIN:

                if (view == buttonLeft) {
                    //确认登录之后的操作
                    String usern=formView.edit1.getText().toString();//用户名  我这里设置初始管理员用户名和密码是 admin 123456
                    String passw=formView.edit2.getText().toString();//密码
                    if(usern.equals("admin") && passw.equals("123456"))//登录成功
                    {
//                        Toast ts = Toast.makeText(this,"登录成功！",Toast.LENGTH_SHORT);
//                        ts.setText("登录成功！");
//                        ts.show();
                        ToastUtil.showToast(this,0,"登录成功！");
                        formView.edit1.setText("");
                        formView.edit2.setText("");
                        Intent intent_admin=new Intent(this,test_activity.class);
                        startActivity(intent_admin);
                    }
                    else{
//                        Toast ts = Toast.makeText(this,"登录失败！",Toast.LENGTH_SHORT);
//                        ts.setText("登录失败！");
//                        ts.show();
                        ToastUtil.showToast(this,0,"登录失败！");
                        formView.edit1.setText("");
                        formView.edit2.setText("");
                    }
                } else if (view == buttonRight) {
                    //取消登录的操作
                    formView.animate().translationY(-1 * delta).alpha(1).setDuration(500).start();//隐藏
                    inputType = InputType.NONE;
                    buttonLeft.setText("进入系统");//原来是R.string.button_login
                    buttonRight.setText("管理员登录");//原来是R.string.button_signup

                }
                break;
//            case SIGN_UP:
//
//                formView.animate().translationY(-1 * delta).alpha(0).setDuration(100).start();
//                if (view == buttonLeft) {
//
//                } else if (view == buttonRight) {
//
//                }
//                inputType = InputType.NONE;
//                buttonLeft.setText(R.string.button_login);
//                buttonRight.setText(R.string.button_signup);
//                break;
        }
    }

    enum InputType {
        NONE,LOGIN;
    }
}
