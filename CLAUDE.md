# 开发日志记录器

一款用来记录工作日志的软件，支持按日期筛选和浏览、标签分类、简单的统计面板（本周写了几条、常用标签）。

## 技术栈
- 后端：Java 17, Spring Boot 4.x, Spring Data JPA
- 前端：TypeScript, Vue 3, Vite
- 数据库：MySQL 8
- 构建工具：Maven (后端), pnpm (前端)

## 项目结构
- backend/   — Spring Boot 后端
- frontend/  — Vue 3 SPA

## 常用命令
- 后端构建：`cd backend && mvn clean package -DskipTests`
- 后端测试：`cd backend && mvn test`
- 前端启动：`cd frontend && pnpm dev`
- 前端构建：`cd frontend && pnpm build`

## 代码规范
- 后端遵循阿里巴巴 Java 编码规范
- 前端使用 Composition API + <script setup> 语法
- 变量命名：后端 camelCase，数据库字段 snake_case
- 所有 API 返回统一的 Result<T> 包装类

## 工作流程
- 修改代码后先跑对应模块的测试再提交
- 每次只改一个功能点，不要混合多个变更
- 每完成一个子步骤就 git commit，commit message 写清楚做了什么，并更新 progress.md
- 复杂任务中途如果有重要的设计决策，写入 progress.md