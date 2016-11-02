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

@LayoutRes(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.button_1)
    Button mBtuuon_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectTool.inject(this);
        mBtuuon_1.setText("this button has change");
    }

    @OnClick(R.id.button_2)
    public void clickBtn_2(){
        Toast.makeText(this,"hellow world_2",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.button_3)
    public void clickBtn_3(){
        Toast.makeText(this,"hellow world_3",Toast.LENGTH_SHORT).show();
    }

}
