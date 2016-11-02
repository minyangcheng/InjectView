package com.min.inject.compiler;

import com.min.inject.annotation.BindView;

import java.util.HashSet;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;

/**
 * Created by minyangcheng on 2016/11/1.
 */
public class ProxyClassInfo {

    private static final String NEWLINE="\n";
    private static final String LINEEND=";";
    private static final String TAB="    ";

    private String mPackageName;
    private String mClassName;

    private Set<VariableElement> mVariableSet=new HashSet<>();

    public ProxyClassInfo(String packageName,String className){
        this.mPackageName=packageName;
        this.mClassName=className;
    }

    public void add(VariableElement element){
        mVariableSet.add(element);
    }

    public void add(TypeElement element){

    }

    public void add(ExecutableElement element){

    }

    public String getFullClassName(){
        return mPackageName+"."+mClassName+"$$Inject";
    }

    public String generateCode(){
        StringBuilder sb=new StringBuilder();
        sb.append("//this is generate by inject view, do not modify"+NEWLINE);
        sb.append("package "+mPackageName+ LINEEND +NEWLINE+NEWLINE);
        sb.append("import com.min.inject.api.Inject;"+NEWLINE);
        sb.append("import com.min.inject.api.Provider;"+NEWLINE);
        sb.append(NEWLINE);
        sb.append("public class "+mClassName+"$$Inject "+"implements Inject<"+mClassName+"> {"+NEWLINE);
        sb.append(NEWLINE);
        sb.append(TAB+"@Override"+NEWLINE);
        sb.append(TAB+"public void bind("+mClassName+" target, Object source, Provider provider) {"+NEWLINE);

        for(VariableElement element : mVariableSet){
            String type=element.asType().toString();
            String name=element.getSimpleName().toString();
            BindView bindView=element.getAnnotation(BindView.class);
            int resId=bindView.value();
            sb.append(TAB+TAB+"target."+name+"=("+type+") provider.findView(source,"+resId+")"+LINEEND+NEWLINE);
        }

        sb.append(TAB+"}"+NEWLINE);
        sb.append(NEWLINE);
        sb.append("}");
        return sb.toString();
    }

}
