# 项目笔记

***

## Model设计
Banner：id, url, type

Scene: id, name, iconUrl, type, visiablity

Order: id, type, time, status, desc, earning, meid, scene, phone







## MVP设计
1.主页功能：Banner展示，场景类目动态展示，场景跳转，是否从服务端获取

2.查询页功能：查询今日订单统计，查询近一周订单统计，查询指定日期订单统计，打开有效登记详情列表页面，打开无效登记详情列表页面，打开预登记详情列表页面

3.收益页功能：查询统计一个月内收益金额，查询所有日期收益情况，打开近一个月收益详情列表页面，打开指定日期收益详情列表页面




## 相关资料
### MVP
> 1.浅谈 MVP in Android:http://blog.csdn.net/lmj623565791/article/details/46596109

### RxJava/RxAndroid
> 1.给 Android 开发者的 RxJava 详解:http://gank.io/post/560e15be2dca930e00da1083

> 2.RxJava专题:http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2015/0430/2815.html

> 3.打造属于自己的RxBus:http://www.jianshu.com/p/adfc72da6056

> 4.RxJava 和 RxAndroid 一(基础):http://www.cnblogs.com/zhaoyanjun/p/5175502.html

> 5.RxJava 和 RxAndroid 二(操作符的使用):http://www.cnblogs.com/zhaoyanjun/p/5502804.html

> 6.RxJava 和 RxAndroid 三(生命周期控制和内存优化):http://www.cnblogs.com/zhaoyanjun/p/5523454.html

> 7.RxJava 和 RxAndroid 四(RxBinding的使用):http://www.cnblogs.com/zhaoyanjun/p/5535651.html

> 8.RxJava 和 RxAndroid 五(线程调度):http://www.cnblogs.com/zhaoyanjun/p/5624395.html

> 9.使用RxBinding响应控件的异步事件:http://www.jianshu.com/p/c2c7c46e6b97/comments/1338430

> 10.GreenDao3.0简单使用:http://www.jianshu.com/p/4986100eff90

> 11.GreenDao3 使用说明:http://www.jianshu.com/p/4e6d72e7f57a

### Retrofit2 + OkHttp3 + RxJava
> 1.Retrofit2+OkHttp3+RxJava搭建网络框架: http://blog.csdn.net/wlt111111/article/details/51455524

> 2.RxJava学习笔记2：基于RxJava+okHttp的Rest Cas登录实现:http://blog.csdn.net/wangkai0681080/article/details/50295043

> 3.Retrofit2 完全解析 探索与okhttp之间的关系:http://blog.csdn.net/lmj623565791/article/details/51304204

> 4.RxJava的封装和研究:http://blog.csdn.net/u014752325/article/details/53380258

> 5.RxJava+Retrofit+OkHttp深入浅出-终极封装二（网络请求）:http://blog.csdn.net/wzgiceman/article/details/51939574

> 6.高仿知乎日报 （Material Design + MVP + RxJava + Retrofit）:http://www.jianshu.com/p/61efdc826c01