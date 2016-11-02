package com.min.iv;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.min.inject.annotation.BindView;
import com.min.inject.annotation.LayoutRes;
import com.min.inject.annotation.OnClick;
import com.min.inject.api.InjectTool;
import com.min.inject.api.Provider;

import java.util.List;

@LayoutRes(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_1)
    Button mBtuuon_1;

    private int mNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        InjectTool.inject(MainActivity.this, MainActivity.this, Provider.ACTIVITY);
        findViewById(R.id.btn_actual).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNum++;
                mBtuuon_1.setText(String.valueOf(mNum));
            }
        });
    }

    @OnClick(R.id.button_2)
    public void clickBtn_1(String s , int num ,List<String> list){
        Toast.makeText(this,"hellow world?",Toast.LENGTH_SHORT).show();
    }

}
