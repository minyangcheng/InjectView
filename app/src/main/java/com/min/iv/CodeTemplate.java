package com.min.iv;

import android.widget.Button;

import com.min.inject.api.Inject;
import com.min.inject.api.Provider;

/**
 * Created by minyangcheng on 2016/11/1.
 */
public class CodeTemplate implements Inject<MainActivity> {

    @Override
    public void bind(MainActivity target, Object source, Provider provider) {
        target.mBtuuon_1= (Button) provider.findView(source,1);
    }

}
