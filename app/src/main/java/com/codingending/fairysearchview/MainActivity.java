package com.codingending.fairysearchview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.codingending.library.FairySearchView;

/**
 * 示范demo
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView feedbackView;
    private FairySearchView fairySearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initToolbar();
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
    }

    private void initViews(){
        toolbar=findViewById(R.id.toolbar);
        feedbackView=findViewById(R.id.text_view_feedback);
        fairySearchView=findViewById(R.id.search_view);

        fairySearchView.setOnBackClickListener(new FairySearchView.OnBackClickListener() {
            @Override
            public void onClick() {
                feedbackView.setText("点击了返回按钮");
            }
        });
        fairySearchView.setOnCancelClickListener(new FairySearchView.OnCancelClickListener() {
            @Override
            public void onClick() {
                feedbackView.setText("点击了取消按钮");
            }
        });
        fairySearchView.setOnEditChangeListener(new FairySearchView.OnEditChangeListener() {
            @Override
            public void onEditChanged(String nowContent) {
                String text="当前内容："+fairySearchView.getSearchText();
                feedbackView.setText(text);
            }
        });
        fairySearchView.setOnEnterClickListener(new FairySearchView.OnEnterClickListener() {
            @Override
            public void onEnterClick(String content) {
                String text="点击了虚拟键盘中的搜索按钮"+"\n"+"搜索内容："+content;
                feedbackView.setText(text);
            }
        });
    }

}
