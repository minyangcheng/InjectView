package com.min.inject.compiler;

import com.min.inject.compiler.bind.ClassBind;
import com.min.inject.compiler.bind.FieldBind;
import com.min.inject.compiler.bind.MethodBind;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by minyangcheng on 2016/11/1.
 */
public class ProxyClassInfo {

    private static final String NEW_LINE="\n";
    private static final String TAB="    ";

    private static final String SUFFIX_NAME="$$Inject";

    private String mTargetPackageName;
    private String mTragetClassName;
    private String mProxyClassName;

    private Set<FieldBind> mFiledBindSet=new HashSet<>();
    private Set<MethodBind> mMethodBindSet=new HashSet<>();
    private ClassBind mClassBind;

    public ProxyClassInfo(String targetPackageName,String tragetClassName){
        this.mTargetPackageName=targetPackageName;
        this.mTragetClassName=tragetClassName;
        this.mProxyClassName=tragetClassName+SUFFIX_NAME;
    }

    public void add(FieldBind bind){
        mFiledBindSet.add(bind);
    }

    public void add(MethodBind bind){
        mMethodBindSet.add(bind);
    }

    public void setClassBind(ClassBind bind){
        mClassBind=bind;
    }

    public String getFullClassName(){
        return mTargetPackageName+"."+mProxyClassName;
    }

    public String generateCode(){
        StringBuilder sb=new StringBuilder();
        sb.append("//this is generate by inject view, do not modify"+NEW_LINE);
        sb.append("package "+mTargetPackageName+" ;" +NEW_LINE+NEW_LINE);
        sb.append("import com.min.inject.api.Inject;"+NEW_LINE);
        sb.append("import com.min.inject.api.Provider;"+NEW_LINE);
        sb.append("import android.view.View;"+NEW_LINE);
        sb.append(NEW_LINE);
        sb.append("public class "+mProxyClassName+" implements Inject<"+mTragetClassName+"> {"+NEW_LINE);
        sb.append(NEW_LINE);
        sb.append(TAB+"@Override"+NEW_LINE);
        sb.append(TAB+"public void bind(final "+mTragetClassName+" target, Object source, Provider provider) {"+NEW_LINE);

        insertClassCode(sb);
        insertFieldCode(sb);
        insertMethodCode(sb);

        sb.append(TAB+"}"+NEW_LINE);
        sb.append(NEW_LINE);
        sb.append("}");
        return sb.toString();
    }

    private void insertClassCode(StringBuilder sb) {
        if(mClassBind.resId>0){
            sb.append(TAB+TAB+"provider.setLayoutResId(target,"+mClassBind.resId+");"+NEW_LINE);
        }
    }

    private void insertMethodCode(StringBuilder sb) {
        sb.append(TAB+TAB+"View view= null;"+NEW_LINE);
        for(MethodBind bind : mMethodBindSet){
            sb.append(TAB+TAB+"view=provider.findView(source,"+bind.resId+");"+NEW_LINE);
            sb.append(TAB+TAB+"view.setOnClickListener(new View.OnClickListener() {"+NEW_LINE);
            sb.append(TAB+TAB+TAB+"@Override"+NEW_LINE);
            sb.append(TAB+TAB+TAB+"public void onClick(View v) {"+NEW_LINE);
            sb.append(TAB+TAB+TAB+TAB+"target."+bind.name+"();"+ NEW_LINE);
            sb.append(TAB+TAB+TAB+"}"+NEW_LINE);
            sb.append(TAB+TAB+"});"+NEW_LINE);

        }
    }

    private void insertFieldCode(StringBuilder sb) {
        for(FieldBind bind : mFiledBindSet){
            sb.append(TAB+TAB+"target."+bind.name
                    +"=("+bind.type+") provider.findView(source,"+bind.resId+");"+NEW_LINE);
        }
    }

}
