package com.min.iv;

import android.view.View;
import android.widget.Button;

import com.min.inject.api.Inject;
import com.min.inject.api.Provider;

/**
 * Created by minyangcheng on 2016/11/1.
 */
public class CodeTemplate implements Inject<MainActivity> {

    @Override
    public void bind(final MainActivity target, Object source, Provider provider) {
//        provider.setLayoutResId(target,id);
        target.mBtuuon_1= (Button) provider.findView(source,1);

        View view= null;
        view=provider.findView(source,1);;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                target.onBackPressed();
            }
        });
    }

}
