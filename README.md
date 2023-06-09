# Lite-BI



# 功能树

- [ ] 数据表
  - [ ] 分类目录
    - [ ] 数据源(标签页)
      - [ ] 数据源 增、删、改、查、刷新
      - [ ] 查看数据源下所有的表
      - [ ] 多种数据源接入
        - [ ] Mysql
        - [ ] PostgreSQL
        - [ ] Excel、CSV、TXT 等文件
        - [ ] Oracle
        - [ ] SQL Server
    - [ ] 自定义表(标签页)
      - [ ] 自定义的表清单
      - [ ] 创建、修改、删除自定义表
    - [ ] 标签视图(标签页)
      - [ ] 根据标签，显示表
    - [ ] 3 个标签页对表的通用操作
      - [ ] 搜索表
      - [ ] 打标签
      - [ ] 修改表的显示名
      - [ ] 如果是自定义表，在标签视图也能 修改、删除
  - [ ] 数据预览
    - [ ] 显示表的数据内容
    - [ ] 修改列显示名称
- [ ] 图表
  - [ ] 数据计算
    - [ ] 原始数据过滤
    - [ ] 基本的分组汇总
    - [ ] 分组后过滤
    - [ ] 二次计算
  - [ ] 通用功能
    - [ ] 发布图表（供其他应用嵌入）
    - [ ] 数据刷新周期
  - [ ] 图表类型
    - [ ] 分组表
    - [ ] 柱状图
    - [ ] 折线图
    - [ ] 饼图
- [ ] 仪表板
  - [ ] 画布
  - [ ] 过滤组件
  - [ ] Web 组件

# RoadMap

* [ ] 0.3 数据处理能力
  * [ ] 数据接入
    * [ ] 支持PostgreSQL 数据源
    * [ ] 支持本地缓存
  * [ ] 数据处理
    * [ ] 可视化定义视图
  * [x] 图表
  * [x] 仪表板
* [ ] 0.2 基本图表能力
  * [x] 数据接入
  * [x] 数据处理
  * [ ] 图表
    * [ ] 柱状图
    * [ ] 折线图
    * [ ] 饼图
  * [x] 仪表板
* [ ] 0.1 (MVP) 
  * [ ] 项目搭建
    * [x] 后端
    * [ ] 前端
  * [x] 数据接入
    * [x] 支持MySQL数据源
        * [x] Mysql 数据源字段定义

        * [x] 数据源增删改查

        * [ ] Controller 层单元测试

        * [x] 创建 fdw mysql 所有的表

        * [ ] fdw 结构更新

        * [ ] 数据预览
    * [x] 数据处理
  * [ ] 图表
    * [ ] 分组表基本功能
        * [ ] 原始数据过滤
        * [ ] 基本的分组汇总
        * [ ] 分组后过滤
        * [ ] 二次计算
  * [x] 仪表板
  * [ ] 打包
    * [ ] Windows
    * [ ] macOS
    * [ ] Linux

