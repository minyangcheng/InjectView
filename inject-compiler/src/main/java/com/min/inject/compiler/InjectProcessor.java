package com.min.inject.compiler;

import com.google.auto.service.AutoService;
import com.min.inject.annotation.BindView;
import com.min.inject.annotation.LayoutRes;
import com.min.inject.annotation.OnClick;
import com.min.inject.compiler.bind.ClassBind;
import com.min.inject.compiler.bind.FieldBind;
import com.min.inject.compiler.bind.MethodBind;
import com.min.inject.compiler.util.GsonUtil;
import com.min.inject.compiler.util.LogUtil;

import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * Created by minyangcheng on 2016/11/1.
 */
@AutoService(Processor.class)
public class InjectProcessor extends AbstractProcessor {

    private Elements mElementUtils;
    private Messager mMessager;
    private Filer mFiler;

    private Map<String,ProxyClassInfo> mProxyMap=new HashMap<>();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mElementUtils=processingEnv.getElementUtils();
        mMessager=processingEnv.getMessager();
        mFiler= processingEnv.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mProxyMap.clear();
        for(Element element : roundEnv.getElementsAnnotatedWith(BindView.class)){
            if(element.getKind()==ElementKind.FIELD && element instanceof VariableElement){
                FieldBind bind=new FieldBind((VariableElement) element,mElementUtils);
                String jsonStr= GsonUtil.toJson(bind);
                LogUtil.d(jsonStr);
            }
//            ProxyClassInfo proxyInfo=mProxyMap.get(fullName);
//            if(proxyInfo==null){
//                proxyInfo=new ProxyClassInfo(packageName,className);
//                proxyInfo.add(variableElement);
//                mProxyMap.put(fullName, proxyInfo);
//            }
        }

        for(Element element : roundEnv.getElementsAnnotatedWith(OnClick.class)){
            if(element.getKind()==ElementKind.METHOD && element instanceof ExecutableElement){
                MethodBind bind=new MethodBind((ExecutableElement) element,mElementUtils);
                String jsonStr= GsonUtil.toJson(bind);
                LogUtil.d(jsonStr);
            }
//            ProxyClassInfo proxyInfo=mProxyMap.get(fullName);
//            if(proxyInfo==null){
//                proxyInfo=new ProxyClassInfo(packageName,className);
//                proxyInfo.add(variableElement);
//                mProxyMap.put(fullName, proxyInfo);
//            }
        }

        for(Element element : roundEnv.getElementsAnnotatedWith(LayoutRes.class)){
            if(element.getKind()==ElementKind.CLASS && element instanceof TypeElement){
                ClassBind bind=new ClassBind((TypeElement) element,mElementUtils);
                String jsonStr= GsonUtil.toJson(bind);
                LogUtil.d(jsonStr);
            }
//            ProxyClassInfo proxyInfo=mProxyMap.get(fullName);
//            if(proxyInfo==null){
//                proxyInfo=new ProxyClassInfo(packageName,className);
//                proxyInfo.add(variableElement);
//                mProxyMap.put(fullName, proxyInfo);
//            }
        }

        try {
            int a=0;
            int b=1/a;
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(Map.Entry<String,ProxyClassInfo> entry : mProxyMap.entrySet()){
            try {
                ProxyClassInfo proxyInfo=entry.getValue();
                JavaFileObject jfo=mFiler.createSourceFile(proxyInfo.getFullClassName());
                Writer writer = jfo.openWriter();
                writer.write(proxyInfo.generateCode());
                writer.flush();
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return true;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(BindView.class.getCanonicalName());
        types.add(OnClick.class.getCanonicalName());
        types.add(LayoutRes.class.getCanonicalName());
        return types;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    private void debug(String msg, Object... args) {
        if(args.length>0){
            msg=String.format(msg, args);
        }
        msg="compiling annotation debug : "+msg;
        mMessager.printMessage(Diagnostic.Kind.NOTE, msg);
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
