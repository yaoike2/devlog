## 功能点 1 - 后端
- Claude Code 做对了什么: 实现功能点 1 ,成功创建了该功能点 1 所需的数据库表.验证通过,编译通过.每一次修改功能点都会自动进行 git commit
- Claude Code 做错了什么 / 需要人工干预的地方：Plan Mode 下列出的计划整体没问题,但计划中没留意：pom.xml 中还没有 spring-boot-starter-validation 依赖，请在实现前先添加这个依赖。
- CLAUDE.md 需要改什么：MySQL 8 改成 MySQL 5.7,这一点是我个人的问题,我的docker 安装了 MySQL 8, MySQL 5.7 两个镜像,我当前启动的 Container 是基于 MySQL 5.7 镜像的. 