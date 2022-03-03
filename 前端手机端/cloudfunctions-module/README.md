# 云函数

### 云函数打包命令

```
npm install

<!-- 打包全部云函数 -->
node build-api.js

<!-- 打包指定云函数 -->
node build-api.js 函数名1 函数名2 ...
```

### 目录说明

`src/api`为存放云函数位置

云函数目录为`api/functionName/functionName.js`，打包云函数后在`cloudfunctions-aliyun`下对应目录生成云函数`index.js`

### 其他说明

- 代码需统一风格，目前是使用`cjs`风格