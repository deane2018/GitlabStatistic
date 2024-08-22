# GItLab APi 代码统计
 >  可直接享用的GitLab代码统计项目

演示网站：[http://leyunone.com:7878/codex/#/](http://leyunone.com:7878/codex/#/)

## 功能

- 指定人员时间范围内的代码提交情况
- 部门时间范围内的代码提交情况
- 条件多元化查询的代码提交记录列表
- 基本数据管理，包括：真实人员-Git账号、部门-真实人员、真实项目-Git项目
- 如果导入bug系统的内容接入，你将得到代码质量报告：维度有人员、部门、项目
- 如果接入`xxl-job` 系统，你将得到监控警报机器人，条件有：周期内bug数量、代码提交频率、缺陷率、代码行数...
- 可视化折线图、柱状图展示数据，并且提交各类数据的导出功能
- ....待补充

## 使用步骤

### 1、数据库文件

见`codex.sql`

### 2、部署项目

代码环境：

|    JDK8    |
| :--------: |
| lombok插件 |

执行部署后，访问 <u>ip:7878/codex/</u>

可直接进入登录页面，**但是** 并没有做用户管理模块，登录只是一个形式（写死的）

### 3、同步gitlab

请自行使用任何办法定时同步gitlab：本项目只提供了一个基于`xxl-job`的每日凌晨执行一次的GitLab全量数据同步的方法`CodeXSummaryService`

### 4、接入bug系统（可选）

见数据库表`x_bug` ，请将你司的bug数据无论什么方式同步到这张表上；

`x_bug`数据表到位了 = 接入了bug系统

### 5、维护真实项目（可选）

见数据库表`x_real_project` ，请将你司的真实项目无论什么方式同步到这张表上；

之后可在页面的`基本数据` -> `项目管理` 中，关联这个真实项目下涉及的前端Git项目地址，后台Git项目地址....

### 6、接入`xxl-job` （可选）

如果你想使用监控报警机器人则必须接入`xxl-job` 因为是基于`cron` 表达式监控时间；

报警可选企微机器人、邮箱；并且可根据报警条件自定义报警内容和数值

## 后言

因为是随手帮公司做的东西😓，所以项目的代码、包、规范注释什么的都比较乱比较杂；

不过可见演示网站，已经是一个最低只需要配置数据库就可以直接享用的GitLab代码统计项目了；

而什么场景、什么人会需要这个项目，我们比谁都情况<(￣︶￣)↗，所以大概大伙想要二次开发的话也只需要改一改里面的一些很简单的方法即可；

前端页面代码由于不想这么麻烦，直接前后端不分离，将原本的vue项目打包成静态化文件通过模板引擎使后台可以直接访问；

如果有需要前端页面代码的可以`Call me` 一下，就不在弄一个项目了

最后欢迎大伙的测试鞭策，因为这并不是一个“完整”开发的项目：）
