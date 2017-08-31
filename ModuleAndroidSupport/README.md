# 模块说明
> 该模块负责引用AndroidSupport相关包，同时对于Support相关功能需要二次封装，
也请在该模块中封装。


> 模块中相关Version，全部在最外层build.gradle中定义，方便版本管理，确保使用一致的版本。


## 2017-03-29
### 增加MultiDex处理方案关联的资料

> 1.配置方法数超过 64K 的应用: https://developer.android.com/studio/build/multidex.html

> 2.Android拆分与加载Dex的多种方案对比: http://mp.weixin.qq.com/s?__biz=MzAwNDY1ODY2OQ==&mid=207151651&idx=1&sn=9eab282711f4eb2b4daf2fbae5a5ca9a&3rd=MzA3MDU4NTYzMw==&scene=6#rd

> 3.预防Android Dex 64k方法大小限制: https://ingramchen.io/blog/2014/09/prevention-of-android-dex-64k-method-size-limit.html
