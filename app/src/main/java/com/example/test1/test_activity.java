package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class test_activity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private DetectAdapter mDetectAdapter;
    private List<String> mList;
    private Button clear_button;
    public static int MODE = MODE_PRIVATE;
    public static final String PREFERENCE_NAME = "the_detect_data";
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_test);
        clear_button = this.findViewById(R.id.clear_infomation);
        mRecyclerView = this.findViewById(R.id.recyclerView);
        // 设置布局管理器
        preferences=getSharedPreferences( PREFERENCE_NAME,MODE);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        // 设置 item 增加和删除时的动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mList = getList();
        mDetectAdapter = new DetectAdapter(this, mList);
        mRecyclerView.setAdapter(mDetectAdapter);
        clear_button.setOnClickListener(v -> {
            preferences.edit().clear().commit();
            ToastUtil.showToast(test_activity.this,0,"清空数据成功");
            reStartActivity();
        });
    }
    private List<String> getList() {
        List<String> list = new ArrayList<>();
        preferences=getSharedPreferences( PREFERENCE_NAME,MODE);
        Map<String,String> key_Value=(Map<String, String>)preferences.getAll(); //获取所有保存在对应标识下的数据，并以Map形式返回
        for(Map.Entry<String, String> entry : key_Value.entrySet()){
            String mapKey = entry.getKey();
            String mapValue = entry.getValue();
            list.add(mapKey + ":"+mapValue);
        }
        return list;
    }
    private void reStartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}