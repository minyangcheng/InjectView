# Android中如何运用编译时注解

java注解分为源码注解（SOURCE）、编译时注解（CLASS）、运行时注解（RUNTIME）。源码注解和运行时注解比较常见了。

1. 源码注解：Override、Deprecated、Suppresswarnings等注解对象
2. 编译时注解：butterknife、Dagger等框架
3. 运行时注解：Retrofit、litepal等框架。

### 编译时注解有何作用？

在代码编译的时刻，即可获取到注解的元素，然后分析元素的各个属性，就可以拿到你所需要的信息，然后你就可以做任何操作了。

### 在android上利用编译时注解书写框架

本框架完成的功能主要是模仿butterknife，目前完成的功能：
* 可以通过注解BindView设置View变量
* 通过OnClick设置view的点击时间
* 在Activity子类上设置LayoutRes注解可以动态的为Activity设置布局。

1. 新建android工程，如下图:
![](/images/project.jpg)

 * app为运行代码的demo。
 * inject-api工程，在约定的位置找到代理类，并用反射生成代理类对象，然后调用代理类方法。
 * inject-annotation工程，定义需要用的注解。
 * inject-complier工程，在约定的位置根据注解元素信息，生成代理类。

2. inject-api主要代码：
```java
private static Map<String,Inject> injectMap=new HashMap<>();

    public static void inject(Activity activity){
        inject(activity,activity,Provider.ACTIVITY);
    }

    public static void inject(View view){
        inject(view,view,Provider.View);
    }

    public static void inject(Object target , Object source,Provider provider){
        Inject inject=findInject(target);
        if(inject!=null){
            inject.bind(target,source,provider);
        }
    }

    private static Inject findInject(Object target) {
        String key=target.getClass().getCanonicalName();
        Inject inject=injectMap.get(key);
        if(inject==null){
            String proxyClassName=key+Constants.CLASS_NAME_SUFFIX;
            try {
                Class clazz=Class.forName(proxyClassName);
                inject= (Inject) clazz.newInstance();
                injectMap.put(key,inject);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return inject;
    }
```

3. inject-complier主要代码：
```java
 @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mProxyMap.clear();
		//获取注解元素信息，并封装到ProxyClassInfo对象中
        initProxyMap(roundEnv);
        writeSourceCode();
        return true;
    }

    private void writeSourceCode() {
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
    }
```
4. app工程在gradle中配置:
```bash
//在主工程中配置
dependencies {
        classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
    }
//app module中配置
apply plugin: 'com.neenbedankt.android-apt'
dependencies {
    compile project(':inject-annotation')
    compile project(':inject-api')
    apt project(':inject-compiler')
}
```

具体代码请查看：<https://github.com/minyangcheng/InjectView>

### 编译时注解可以有哪些用途

通过编译时解析代码上的注解来生成一些辅助代理类，理论上讲可以做很多有趣的事情。如是否可以用此方式来做一个框架来动态的切换程序的主题。


#### 参考
1. <https://lizhaoxuan.github.io/2016/07/17/apt-run_project/#more>