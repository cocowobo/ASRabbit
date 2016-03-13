# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Program Files\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/zmingchun/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#####################记录生成的日志数据,gradle build时在本项目根目录输出################
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
#####################记录生成的日志数据，gradle build时 在本项目根目录输出-end################

##---------- deal for global -------------##
############################################
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
 #优化 不优化输入的类文件
-dontoptimize
 #预校验
-dontpreverify
 #混淆时是否记录日志
-verbose
 #混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
 #忽略警告
#-ignorewarning

 #保护注解
-keepattributes *Annotation*
 #保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
 #v4或者v7包的Fragment不参与混淆
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v7.app.Fragment
 #引用了v4或者v7包的，不处理警告
-dontwarn android.support.**

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
#保持自定义控件类不被混淆
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#保持枚举 enum 类不被混淆 如果混淆报错，建议直接使用上面的 -keepclassmembers class * implements java.io.Serializable即可
#-keepclassmembers enum * {
#  public static **[] values();
#  public static ** valueOf(java.lang.String);
#}

-keepclassmembers class * {
    public void *ButtonClicked(android.view.View);
}

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}

#避免混淆泛型 如果混淆报错建议关掉
-keepattributes Signature

#屏蔽内部类方法暴露范围的属性
-keepattributes Exceptions,InnerClasses,Deprecated,SourceFile,LineNumberTable,EnclosingMethod

############混淆保护自己项目的部分代码以及引用的第三方jar包library-start##################
##---------- deal for common -------------##
-keep class com.htmm.owner.model.** { *; } #实体类不参与混淆
-keep class com.htmm.owner.view.** { *; } #自定义控件不参与混淆
-keep public class com.htmm.owner..R$*{
    public static final int *;
}
-keep class  com.htmm.owner.base.**{*;} #基类不参与混淆
-keep class  com.htmm.owner.app.**{*;} #常量类不参与混淆
-keep class  com.htmm.owner.receiver.**{*;} #接收类不参与混淆

## webview + js ##
-keepattributes *JavascriptInterface*
# keep 使用 webview 的类
#-keepclassmembers class  cn.xingkoo.chaoaiyan.view.activity.GuideActivity {public *;}

# keep 使用 webview 的类的所有的内部类
#-keepclassmembers class  cn.xingkoo.chaoaiyan.view.activity.GuideActivity$* {*;}

## deal for retrolambda ##
-dontwarn java.lang.invoke.*

## deal for okhttp ##
-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn okio.**

## deal for gson  and hawk##
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.** {*;}
-keep class com.google.gson.stream.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.** {
    <fields>;
    <methods>;
}
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
-dontwarn com.google.gson.**

## deal for pulltorefresh ##
-dontwarn com.handmark.pulltorefresh.library.**
-keep class com.handmark.pulltorefresh.library.** { *;}
-dontwarn com.handmark.pulltorefresh.library.extras.**
-keep class com.handmark.pulltorefresh.library.extras.** { *;}
-dontwarn com.handmark.pulltorefresh.library.internal.**
-keep class com.handmark.pulltorefresh.library.internal.** { *;}

## deal for rxjava ##
-dontwarn rx.internal.**

## deal for dagger ##
-dontwarn dagger.**
-keep class dagger.** { *;}

## deal for EventBus ##
-keepclassmembers class ** {
    public void onEvent*(**);
    void onEvent*(**);
}

## deal for butterknife ##
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}



## deal for OrmLite ##
-keep class com.j256.**
-keepclassmembers class com.j256.** { *; }
-keep enum com.j256.**
-keepclassmembers enum com.j256.** { *; }
-keep interface com.j256.**
-keepclassmembers interface com.j256.** { *; }
-keepclassmembers class * {
@com.j256.ormlite.field.DatabaseField *;
}

############混淆保护自己项目的部分代码以及引用的第三方jar包、library-end##################
## deal for ant-1.5.jar ##
-dontwarn org.apache.tools.**
-keep class org.apache.tools.** { *;}

## deal for ant-contrib-1.0b3.jar ##
-dontwarn net.sf.antcontrib.**
-keep class net.sf.antcontrib.** { *;}

## deal for circleimageview-1.3.0.jar ##
-dontwarn de.hdodenhof.circleimageview.**
-keep class de.hdodenhof.circleimageview.** { *;}

## deal for core-3.2.1.jar ##
-dontwarn com.google.zxing.**
-keep class com.google.zxing.** { *;}

## deal for dexmaker-1.2.jar ##
-dontwarn com.google.dexmaker.**
-keep class com.google.dexmaker.** { *;}

## deal for ht-android-baselib-1.0.0.jar ##
-dontwarn com.ht.baselib.**
-keep class com.ht.baselib.** { *;}
-dontwarn com.libcore.io.**
-keep class com.libcore.io.** { *;}
-dontwarn net.bither.util.**
-keep class net.bither.util.** { *;}

## deal for httpcore-4.2.4.jar ##
-dontwarn org.apache.http.**
-keep class org.apache.http.** { *;}

## deal for javawriter-2.1.1.jar ##
-dontwarn com.squareup.javawriter.**
-keep class com.squareup.javawriter.** { *;}

## deal for javaxannotation-api-1.2.jar ##
-dontwarn javax.annotation.**
-keep class javax.annotation.** { *;}

## deal for javax.inject-1.jar ##
-dontwarn javax.inject.**
-keep class javax.inject.** { *;}


## deal for multidex.jar ##
-dontwarn android.support.multidex.**
-keep class android.support.multidex.** { *;}

## deal for objenesis.jar ##
-dontwarn org.objenesis.**
-keep class org.objenesis.** { *;}

## deal for slf4j.jar ##
-dontwarn org.slf4j.**
-keep class org.slf4j.** { *;}

## deal for universal-image-loader-1.9.4.jar ##
-dontwarn com.nostra13.universalimageloader.**
-keep class com.nostra13.universalimageloader.** { *;}

## deal for com.taobao.dp ##
-dontwarn com.taobao.dp.**
-keep class com.taobao.dp.** { *;}

## deal for org.apache.bcel.generic.EmptyVisitor ##
-dontwarn org.apache.bcel.**
-keep class org.apache.bcel.** { *;}

## deal for org.apache.commons.httpclient.params.HttpClientParams ##
-dontwarn org.apache.commons.httpclient.**
-keep class org.apache.commons.httpclient.** { *;}

## deal for org.apache.xml.serialize.XMLSerializer ##
-dontwarn org.apache.xml.**
-keep class org.apache.xml.** { *;}

## deal for avax.servlet.http ##
-dontwarn avax.servlet.http.**
-keep class avax.servlet.http.** { *;}

## deal for avax.servlet.http ##
-dontwarn javax.swing.**
-keep class javax.swing.** { *;}

## deal for java.awt ##
-dontwarn java.awt.**
-keep class java.awt** { *;}

## deal for javax.servlet ##
-dontwarn javax.servlet.**
-keep class javax.servlet.** { *;}

## deal for org.apache.http.conn.ssl. ##
-dontwarn org.apache.http.conn.ssl.**
-keep class org.apache.http.conn.ssl.** { *;}

## deal for org.apache.http.auth. ##
-dontwarn org.apache.http.auth.**
-keep class org.apache.http.auth.** { *;}

## deal for org.apache.tools. ##
-dontwarn org.apache.tools.**
-keep class org.apache.tools.** { *;}

## deal for fr.jayasoft.ivy. ##
-dontwarn fr.jayasoft.ivy.**
-keep class fr.jayasoft.ivy.** { *;}

## deal for net.sf.antcontrib.net. ##
-dontwarn net.sf.antcontrib.net.**
-keep class net.sf.antcontrib.net.** { *;}

## deal for android.net. ##
-dontwarn android.net.**
-keep class android.net.** { *;}

## deal for com.alipay.android.phone.mrpc.core. ##
-dontwarn com.alipay.android.phone.mrpc.core.**
-keep class com.alipay.android.phone.mrpc.core.** { *;}

## deal for  javax.security.sasl. ##
-dontwarn javax.security.sasl.**
-keep class javax.security.sasl.** { *;}

## deal for apache.mina ##
-dontwarn org.apache.mina.**
-keep class org.apache.mina.** { *;}

## deal for jdk15on ##
-dontwarn org.bouncycastle.**
-keep class org.bouncycastle.** { *;}

## deal for 友盟自动更新 ##
-dontwarn com.umeng.update.**
-keep class com.umeng.update.** { *;}

## deal for 友盟社会化分享 ##
-dontwarn com.tencent.**
-keep class com.tencent.** { *;}

-dontwarn com.umeng.**
-keep class com.umeng.** { *;}

## deal for Bugly ##
-keep public class com.tencent.bugly.**{*;}


## deal for 友盟统计和在线参数##
-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}
