# LLApp
android实用案例Demo

//作者：liulei
1.libs文件夹中的utils.arr为我集成的一个开发所用到的几乎所有的工具类，但必须要在application中进行初始化，即LiuleiUtils.init(this);
2.本app依赖core库，该库封装了baseActivity、baseFragment、baseAdapter、以及沉浸式状态栏，6.0权限动态授权功能，网络加载框架等等
3.app中添加了视频在线播放的功能 可以边下边播,可以滑动播放等各种功能
4.app中添加了聊天的功能  根据io.socket.client这个库集成的
5.app中添加了视频聊天功能（也可直播使用）//目前屏蔽暂时屏蔽，公司源码不可外漏
6.photoview包功能：查看图片，图片根据手势进行缩放等功能
7.qrView包功能：二维码扫描
8.添加了下载并安装apk文件
9.添加了右滑关闭页面  也可上下左右都行
10.添加了个人中心   可以设置头像   支持从图库选择  系统相机拍摄  未上传服务器
11.添加了切换应用主题颜色  全局  但未保存
12.添加了自己写的jni  c文件  以及aidl实现重力感应的接口    简单demo
13.添加了我的博客地址，可以跳转至我的博客(。。。哈哈)
14.可切换夜间模式,摇一摇，添加自定义悬浮窗
15.添加了自定义相机拍摄视频和图片，并支持查看图片或播放视频
16.添加了清除app缓存图片功能以及添加了图案锁屏
