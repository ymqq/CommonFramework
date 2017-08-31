

# 2017-04-19 重新调整模块结构

> 1、为了减少模块，提高Gradle编译效率，将一些分散的模块合并，组成Core核心模块
> 2、Core模块的考虑点：App模块使必须会使用的到所有内容，组成一个Core模块
> 3、其他非必须的可单独创建模块


# 当前模块包含内容

1. Rx响应式编程相关的所有内容：
	1) RxJava, RxAndroid
	2) RxLifeCycle, RxLifeCycle-Android
	3) RxPermissions
	4) RxBinding, RxBinding-AppCompat-V7, RxBinding-Design, RxBinding-RecyclerView-V7

2. 性能优化：LeakCanary

3. 日志：Logger

4. Supports兼容支持库：
	1) AppCompat：
	2) RecyclerView：
		1) ItemTouchBuilder
		2) RefreshRecyclerView
		3) RefreshWrapAdapter
		4) DividerGridItemDecoration
		5) DividerItemDecoration
	3) Multidex：
		1) DexCount插件使用
		2) 多包处理方案实现
	4) CardView（目前未使用）
	5) Design（目前未使用）
	
5. 基类：
	1) BaseApplication:
		1) 异常奔溃日志捕获： CustomCrashHandler
		2) 多包处理方案实现
		3) 内存缓存Map方案
	2) BaseAppCompatActivity
	3) BaseFragment
	4) BaseMvpView
	5) BaseMvpPresenter

6. Github资源：
	1) [RxJava](https://github.com/ReactiveX/RxJava)
	2) [RxAndroid](https://github.com/ReactiveX/RxAndroid)
	3) [RxLifeCycle 相关](https://github.com/trello/RxLifecycle)
	4) [RxBinding 相关](https://github.com/JakeWharton/RxBinding)
	5) [LeakCanary](https://github.com/square/leakcanary)
	6) [Logger](https://github.com/orhanobut/logger)
	7) [AndroidSwipeLayout](https://github.com/daimajia/AndroidSwipeLayout)
	8) [DexCount](https://github.com/KeepSafe/dexcount-gradle-plugin)

7. Supports支持库引用：
	1) AppCompat: compile "com.android.support:appcompat-v7:25.0.1"
	2) RecyclerView: "com.android.support:recyclerview-v7:25.0.1"
	3) Multidex: "com.android.support:multidex:25.0.1"
	4) CardView: compile "com.android.support:cardview-v7:25.0.1"
	5) Design: "com.android.support:design:25.0.1"


6. 参考资料：
	1) [AndroidStudio提升打包的效率](http://blog.csdn.net/caihongdao123/article/details/52086059)
	2) [15 个 Android 通用流行框架大全](http://www.oschina.net/news/73836/15-android-general-popular-frameworks)
	3) [一套完整的Android通用框架](https://zhuanlan.zhihu.com/p/22367031?refer=WuXiaolong)
	3) [Glide全套教程](https://mrfu.me/2016/02/27/Glide_Getting_Started/)
	4) [Android检测apk及dex方法数](http://www.jianshu.com/p/366b3ae72be6)
	5) [美团Android DEX自动拆包及动态加载简介](http://tech.meituan.com/mt-android-auto-split-dex.html)
	6) [Android拆分与加载Dex的多种方案对比](http://mp.weixin.qq.com/s?__biz=MzAwNDY1ODY2OQ==&mid=207151651&idx=1&sn=9eab282711f4eb2b4daf2fbae5a5ca9a&3rd=MzA3MDU4NTYzMw==&scene=6#rd)
	7) [MultiDex终极方案](http://blog.zongwu233.com/the-touble-of-multidex)