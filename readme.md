# I Bill  
2021年11月22日- 2021年12月3日 Android基础实训任务

### 预览
![preview](/preview.gif)

### 开发环境
- Java JDK 16.0.2
- Android Studio 2020.3.1
- Gradle 5.4.1
- Android Gradle Plugin 3.5.2

### 程序逻辑处理结构
```
main 
  |- assets
  |- java
    |- (...package name)
      |- model
      |- server
      |- util
      |- view
      MainActivity.java
  |- res 
    |- drawable
    |- layout 
    |- menu
    |- mipmap
    |- values

```

### 启动动画
  - 原理：使用 ``WebView``视图控件，再给这个试图控件设置加载的``html``文件资源

  - 在 ``activiey_wecome.xml`` 使用的是 ``WebView`` 视图控件，然后给试图控件设置加载的URL，在``wecome.java`` 中可以看到。

  - 相应的资源： ``activiey_wecome.xml`` -> ``/main/res/layout/*``
    ``wecom.java`` -> ``/main/java/com/example/mybill/view/*``
    ``index.html`` -> ``/main/assets/*``


