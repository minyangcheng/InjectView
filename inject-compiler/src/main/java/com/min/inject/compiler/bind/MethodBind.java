package com.min.inject.compiler.bind;

import com.min.inject.annotation.OnClick;

import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;

/**
 * Created by minyangcheng on 2016/11/2.
 */
public class MethodBind {

    public String packageName;
    public String className;
    public String fullClassName;

    public String type;
    public String name;
    public List<String> paramsList;

    public int resId;

    public MethodBind(ExecutableElement element , Elements elementUtils){
        TypeElement typeElement= (TypeElement) element.getEnclosingElement();
        packageName=elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        className=typeElement.getSimpleName().toString();
        fullClassName=typeElement.getQualifiedName().toString();

        type=element.asType().toString();
        name=element.getSimpleName().toString();
        paramsList=getMethodParameterTypes(element);

        OnClick onClick=element.getAnnotation(OnClick.class);
        resId=onClick.value();
    }

    private List<String> getMethodParameterTypes(ExecutableElement executableElement) {
        List<? extends VariableElement> methodParameters = executableElement.getParameters();
        if (methodParameters.size()==0){
            return null;
        }
        List<String> types = new ArrayList<>();
        for (VariableElement variableElement : methodParameters) {
            TypeMirror methodParameterType = variableElement.asType();
            if (methodParameterType instanceof TypeVariable) {
                TypeVariable typeVariable = (TypeVariable) methodParameterType;
                methodParameterType = typeVariable.getUpperBound();
            }
            types.add(methodParameterType.toString());
        }
        return types;
    }

}
