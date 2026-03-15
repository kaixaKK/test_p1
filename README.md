# 简易淘宝 (SimpleTaobao) - 项目文档

## 项目简介

简易淘宝是一个全栈电商平台，采用**微服务架构**，前端使用 Vue 3 + Vite，后端使用 Spring Cloud 微服务 + MySQL。

## 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3, Vite, Vue Router, Pinia, Axios |
| 网关 | Spring Cloud Gateway |
| 微服务框架 | Spring Cloud |
| 服务注册与配置 | Nacos |
| 熔断限流 | Sentinel |
| 消息队列 | RocketMQ |
| 缓存 | Redis |
| 安全框架 | Spring Security + JWT |
| 数据库 | MySQL |
| ORM | MyBatis |
| 构建工具 | Maven (后端), npm (前端) |

---

## 微服务架构

```
                              ┌─────────────────┐
                              │   Gateway       │
                              │  (Spring Cloud  │
                              │   Gateway)      │
                              │   端口: 8090    │
                              └────────┬────────┘
                                       │
         ┌───────────┬───────┬───────┬─┴───────┬────────┐
         │           │       │       │         │        │
         ▼           ▼       ▼       ▼         ▼        ▼
┌────────────┐ ┌──────────┐ ┌──────┐ ┌─────────┐ ┌───────────┐
│user-service│ │product-  │ │ cart │ │  order  │ │  message  │
│            │ │service   │ │service│ │ service │ │  service  │
│ 端口: 8081 │ │端口:8082 │ │8083  │ │  8084   │ │   8085    │
└────────────┘ └──────────┘ └──────┘ └─────────┘ └───────────┘
         │           │       │       │         │
         └───────────┴───────┴───────┴─────────┘
                                       │
                              ┌────────┴────────┐
                              │     MySQL      │
                              │  端口: 3306    │
                              │ 数据库: simple_taobao
                              └─────────────────┘

┌────────────┐
│  Frontend  │
│  Vue 3     │
│ 端口: 5173 │
└────────────┘
```

---

## 服务端口一览

| 服务 | 端口 | 说明 |
|------|------|------|
| gateway | 8090 | API 网关，统一入口 |
| user-service | 8081 | 用户服务 |
| product-service | 8082 | 商品服务 |
| cart-service | 8083 | 购物车服务 |
| order-service | 8084 | 订单服务 |
| message-service | 8085 | 留言服务 |
| frontend | 5173 | Vue 3 前端 |
| Nacos | 8848 | 服务注册与配置中心 |
| Sentinel | 8080 | 熔断限流 dashboard |
| RocketMQ | 9876/10911 | 消息队列 |
| Redis | 6380 | 缓存数据库 |
| MySQL | 3307 | 数据库 |

---

## 项目结构

```
simple-taobao/
├── gateway/                          # API 网关
│   ├── src/main/java/com/taobao/gateway/
│   │   ├── GatewayApplication.java   # 启动类
│   │   └── config/
│   │       └── CorsConfig.java        # 跨域配置
│   ├── src/main/resources/
│   │   └── application.yml            # 网关配置
│   └── pom.xml
│
├── user-service/                     # 用户服务
│   ├── src/main/java/com/taobao/
│   │   ├── UserServiceApplication.java
│   │   ├── controller/
│   │   │   └── UserController.java
│   │   ├── service/
│   │   │   └── UserService.java
│   │   ├── mapper/
│   │   │   └── UserMapper.java
│   │   └── entity/
│   │       └── User.java
│   └── src/main/resources/
│       └── application.yml
│
├── product-service/                  # 商品服务
│   ├── src/main/java/com/taobao/
│   │   ├── ProductServiceApplication.java
│   │   ├── controller/
│   │   │   └── ProductController.java
│   │   ├── service/
│   │   │   └── ProductService.java
│   │   ├── mapper/
│   │   │   └── ProductMapper.java
│   │   └── entity/
│   │       └── Product.java
│   └── src/main/resources/
│       └── application.yml
│
├── cart-service/                     # 购物车服务
│   ├── src/main/java/com/taobao/
│   │   ├── CartServiceApplication.java
│   │   ├── controller/
│   │   │   └── CartController.java
│   │   ├── service/
│   │   │   └── CartService.java
│   │   ├── mapper/
│   │   │   └── CartMapper.java
│   │   └── entity/
│   │       └── Cart.java
│   └── src/main/resources/
│       └── application.yml
│
├── order-service/                    # 订单服务
│   ├── src/main/java/com/taobao/
│   │   ├── OrderServiceApplication.java
│   │   ├── controller/
│   │   │   └── OrderController.java
│   │   ├── service/
│   │   │   └── OrderService.java
│   │   ├── mapper/
│   │   │   ├── OrderMapper.java
│   │   │   └── OrderItemMapper.java
│   │   └── entity/
│   │       ├── Order.java
│   │       └── OrderItem.java
│   └── src/main/resources/
│       └── application.yml
│
├── message-service/                  # 留言服务
│   ├── src/main/java/com/taobao/
│   │   ├── MessageServiceApplication.java
│   │   ├── controller/
│   │   │   └── MessageController.java
│   │   ├── service/
│   │   │   └── MessageService.java
│   │   ├── mapper/
│   │   │   └── MessageMapper.java
│   │   └── entity/
│   │       └── Message.java
│   └── src/main/resources/
│       └── application.yml
│
├── backend/                          # 原单体应用（保留）
│   └── ...
│
└── frontend/                         # Vue 3 前端
    ├── src/
    │   ├── views/
    │   │   ├── user/                  # 用户端页面
    │   │   │   ├── Home.vue           # 商城首页
    │   │   │   ├── ProductDetail.vue  # 商品详情
    │   │   │   ├── Cart.vue           # 购物车
    │   │   │   ├── Orders.vue         # 我的订单
    │   │   │   └── Seckill.vue        # 秒杀专区
    │   │   ├── merchant/              # 商户端页面
    │   │   │   ├── Dashboard.vue      # 商户首页
    │   │   │   ├── Products.vue       # 商品管理
    │   │   │   ├── Orders.vue         # 订单管理
    │   │   │   └── Messages.vue       # 留言管理
    │   │   └── admin/                 # 管理端页面
    │   │       ├── Dashboard.vue      # 管理首页
    │   │       ├── Users.vue          # 用户管理
    │   │       └── Merchants.vue      # 商户管理
    │   ├── components/                # 公共组件
    │   │   ├── NavBar.vue            # 导航栏
    │   │   ├── Login.vue              # 登录组件
    │   │   └── ProductCard.vue       # 商品卡片
    │   ├── api/
    │   │   └── index.js              # API 封装
    │   ├── stores/
    │   │   └── user.js               # 用户状态管理
    │   ├── router/
    │   │   └── index.js
    │   ├── App.vue
    │   └── main.js
    ├── index.html
    ├── vite.config.js
    └── package.json
```

---

## 认证与授权

### Spring Security + JWT

项目采用 **Spring Security + JWT** 实现无状态认证：

#### 核心组件

| 组件 | 位置 | 说明 |
|------|------|------|
| SecurityConfig | backend/config/ | Spring Security 配置 |
| JwtUtil | backend/config/ | JWT 工具类 |
| JwtAuthenticationFilter | backend/config/ | JWT 认证过滤器 |

#### 安全配置说明

```java
// SecurityConfig.java 主要配置
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    // 1. 密码加密：BCrypt
    // 2. JWT 过滤器
    // 3. 跨域配置
    // 4. 角色权限控制
}
```

#### 权限规则

| 路径 | 权限 |
|------|------|
| `/api/users/register` | 公开 |
| `/api/users/login` | 公开 |
| `/api/products/**` | 公开 |
| `/api/admin/**` | 需 ADMIN 角色 |
| `/api/merchant/**` | 需 MERCHANT 或 ADMIN 角色 |
| 其他 | 需要登录认证 |

#### JWT Token

- **生成方式**: 用户登录成功后生成 Token
- **有效期**: 24 小时
- **包含信息**: userId, username, role
- **请求头格式**: `Authorization: Bearer <token>`

#### 密码加密

使用 BCryptPasswordEncoder 对密码进行加密存储。

---

## 数据库设计

### 1. 用户表 (users)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| username | VARCHAR(50) | 用户名（唯一） |
| password | VARCHAR(100) | 密码（BCrypt 加密） |
| role | VARCHAR(20) | 角色：USER/MERCHANT/ADMIN |
| phone | VARCHAR(20) | 手机号 |
| email | VARCHAR(100) | 邮箱 |
| created_at | DATETIME | 创建时间 |

### 2. 商品表 (products)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| merchant_id | BIGINT | 商户ID（外键） |
| name | VARCHAR(100) | 商品名称 |
| description | TEXT | 商品描述 |
| price | DECIMAL(10,2) | 价格 |
| stock | INT | 库存数量 |
| image_url | VARCHAR(255) | 图片URL |
| category | VARCHAR(50) | 分类 |
| status | VARCHAR(20) | 状态：ON_SALE/OFF_SALE |
| is_seckill | BOOLEAN | 是否秒杀商品 |
| seckill_price | DECIMAL(10,2) | 秒杀价格 |
| seckill_start_time | DATETIME | 秒杀开始时间 |
| seckill_end_time | DATETIME | 秒杀结束时间 |
| created_at | DATETIME | 创建时间 |

### 3. 购物车表 (cart_items)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID（外键） |
| product_id | BIGINT | 商品ID（外键） |
| quantity | INT | 数量 |

### 4. 订单表 (orders)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| user_id | BIGINT | 用户ID（外键） |
| total_amount | DECIMAL(10,2) | 总金额 |
| status | VARCHAR(20) | 状态：PENDING/PAID/SHIPPED/DELIVERED/CANCELLED |
| created_at | DATETIME | 创建时间 |
| pay_time | DATETIME | 支付时间 |
| ship_time | DATETIME | 发货时间 |

### 5. 订单详情表 (order_items)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| order_id | BIGINT | 订单ID（外键） |
| product_id | BIGINT | 商品ID（外键） |
| product_name | VARCHAR(100) | 商品名称（快照） |
| price | DECIMAL(10,2) | 购买价格 |
| quantity | INT | 数量 |

### 6. 留言表 (messages)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键 |
| product_id | BIGINT | 商品ID（外键） |
| user_id | BIGINT | 用户ID（外键） |
| content | TEXT | 留言内容 |
| reply | TEXT | 商户回复 |
| created_at | DATETIME | 创建时间 |

---

## 功能模块

### 用户端功能

1. **商城首页** (`/`)
   - 商品列表展示
   - 分类筛选（电子产品、服装、食品、图书、其他）
   - 显示商品图片、名称、价格、库存

2. **秒杀专区** (`/seckill`)
   - 展示限时秒杀商品
   - 显示秒杀价格和原价对比

3. **商品详情** (`/product/:id`)
   - 查看商品详细信息
   - 选择数量加入购物车
   - 留言功能

4. **购物车** (`/cart`)
   - 查看购物车商品
   - 修改商品数量
   - 删除商品
   - 计算总价并下单

5. **我的订单** (`/orders`)
   - 查看订单列表
   - 订单状态：待支付 → 已支付 → 已发货 → 已收货
   - 支付、取消、确认收货操作

### 商户端功能

1. **商户中心** (`/merchant`)
   - 商户仪表盘

2. **商品管理** (`/merchant/products`)
   - 添加新商品
   - 编辑商品信息
   - 上架/下架商品
   - 删除商品
   - 设置秒杀商品（秒杀价、秒杀时间）

3. **订单管理** (`/merchant/orders`)
   - 查看所有订单
   - 发货操作

4. **留言管理** (`/merchant/messages`)
   - 查看商品留言
   - 回复用户留言

### 管理端功能

1. **管理后台** (`/admin`)
   - 管理员仪表盘

2. **用户管理** (`/admin/users`)
   - 查看普通用户
   - 添加/删除用户

3. **商户管理** (`/admin/merchants`)
   - 查看商户
   - 添加/删除商户

---

## 订单超时取消功能

### 实现原理

使用 **Redis ZSet** + **RocketMQ** 实现订单超时自动取消：

```
创建订单 ──┬──> Redis ZSet (score=过期时间戳, value=订单ID)
           │
           └──> RocketMQ 延迟消息

定时任务 ──> 每分钟扫描 Redis ZSet ──> 发现超时订单 ──> 取消订单
```

### Redis 数据结构

```
Key: order:timeout:zset
├── score: 过期时间戳 (毫秒)
└── value: 订单ID
```

### 核心组件

| 组件 | 路径 | 说明 |
|------|------|------|
| OrderTimeoutConfig | order-service/config/ | 超时处理核心逻辑 |
| OrderTimeoutService | order-service/config/ | 订单取消服务 |

### 工作流程

1. **创建订单**：将订单ID和过期时间存入 Redis ZSet，发送 RocketMQ 延迟消息
2. **支付成功**：从 Redis ZSet 中移除该订单
3. **取消订单**：从 Redis ZSet 中移除该订单
4. **定时检查**：每分钟扫描 ZSet，自动取消超时的待支付订单

### 配置说明

订单超时时间可在 `OrderService.java` 中配置：

```java
private static final long ORDER_TIMEOUT = 30 * 60 * 1000; // 30分钟
```

---

## API 接口说明

所有请求通过网关统一路由，网关端口：**8090**

### 用户接口

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | /api/users/login | 用户登录 |
| POST | /api/users/register | 用户注册 |
| GET | /api/users/{id} | 获取用户信息 |
| GET | /api/users | 获取所有用户 |
| GET | /api/users/role/{role} | 按角色获取用户 |
| PUT | /api/users/{id} | 更新用户 |
| DELETE | /api/users/{id} | 删除用户 |

### 商品接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/products | 获取所有商品 |
| GET | /api/products/onsale | 获取在售商品 |
| GET | /api/products/{id} | 获取商品详情 |
| GET | /api/products/merchant/{id} | 获取商户商品 |
| GET | /api/products/category/{cat} | 按分类获取 |
| GET | /api/products/seckill | 获取秒杀商品 |
| POST | /api/products | 创建商品（需商户权限） |
| PUT | /api/products/{id} | 更新商品（需商户权限） |
| DELETE | /api/products/{id} | 删除商品（需商户权限） |

### 购物车接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/cart/{userId} | 获取购物车 |
| POST | /api/cart/add | 添加到购物车 |
| PUT | /api/cart/update/{id} | 更新数量 |
| DELETE | /api/cart/{id} | 删除商品 |
| DELETE | /api/cart/clear/{userId} | 清空购物车 |

### 订单接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/orders/{id} | 获取订单详情 |
| GET | /api/orders/user/{userId} | 用户订单列表 |
| GET | /api/orders | 所有订单（需商户/管理员） |
| POST | /api/orders/create | 创建订单 |
| POST | /api/orders/pay/{id} | 支付订单 |
| POST | /api/orders/ship/{id} | 发货（需商户权限） |
| POST | /api/orders/deliver/{id} | 确认收货 |
| POST | /api/orders/cancel/{id} | 取消订单 |

### 留言接口

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/messages/product/{id} | 商品留言 |
| GET | /api/messages | 所有留言（需商户权限） |
| GET | /api/messages/merchant/{id} | 商户留言 |
| POST | /api/messages | 提交留言 |
| POST | /api/messages/reply/{id} | 回复留言（需商户权限） |
| DELETE | /api/messages/{id} | 删除留言 |

---

## 快速开始

### 1. 环境要求

- JDK 17+
- Maven 3.8+
- Node.js 18+
- MySQL 8.0+
- Nacos 2.x (服务注册与配置中心)

### 2. 启动 Nacos

下载并启动 Nacos Server：
```bash
# 单机模式启动
startup.cmd -m standalone
```

Nacos 控制台：http://localhost:8848/nacos
默认账号：nacos / nacos

### 3. 配置数据库

各服务配置文件（`application.yml`）中数据库密码已配置为 `123456`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/simple_taobao?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false
    username: root
    password: 123456
```

> 如需修改，请同步修改各服务的 `application.yml`

### 4. 初始化数据库

```bash
mysql -u root -p123456 < backend/src/main/resources/schema.sql
```

> **注意**：数据库初始化脚本不包含商品数据。商品需要商户登录后在"商品管理"页面添加。

#### 测试账号

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | ADMIN |
| merchant1 | merchant123 | MERCHANT |
| user1 | user123 | USER |

### 5. 启动后端微服务

按顺序启动以下服务：

```bash
# 1. 启动用户服务
cd user-service
mvn spring-boot:run

# 2. 启动商品服务
cd product-service
mvn spring-boot:run

# 3. 启动购物车服务
cd cart-service
mvn spring-boot:run

# 4. 启动订单服务
cd order-service
mvn spring-boot:run

# 5. 启动留言服务
cd message-service
mvn spring-boot:run

# 6. 启动网关
cd gateway
mvn spring-boot:run
```

或者使用 Maven 命令一次性启动所有服务（需要在父 pom 目录下）：

```bash
mvn spring-boot:run -pl user-service,product-service,cart-service,order-service,message-service,gateway
```

### 6. 启动前端

```bash
cd frontend
npm install
npm run dev
```

前端访问地址：http://localhost:5173

---

## 页面路由

| 路径 | 页面 | 角色 |
|------|------|------|
| / | 商城首页 | 所有人 |
| /login | 登录 | 所有人 |
| /product/:id | 商品详情 | 所有人 |
| /cart | 购物车 | 用户 |
| /orders | 我的订单 | 用户 |
| /seckill | 秒杀专区 | 所有人 |
| /merchant | 商户中心 | 商户/管理员 |
| /merchant/products | 商品管理 | 商户/管理员 |
| /merchant/orders | 订单管理 | 商户/管理员 |
| /merchant/messages | 留言管理 | 商户/管理员 |
| /admin | 管理后台 | 管理员 |
| /admin/users | 用户管理 | 管理员 |
| /admin/merchants | 商户管理 | 管理员 |

---

## 订单状态流程

```
PENDING (待支付)
    ↓ 支付
PAID (已支付)
    ↓ 商户发货
SHIPPED (已发货)
    ↓ 确认收货
DELIVERED (已收货) / CANCELLED (已取消)
```

---

## 角色权限

| 功能 | 普通用户 | 商户 | 管理员 |
|------|----------|------|--------|
| 浏览商品 | ✓ | ✓ | ✓ |
| 购物车/下单 | ✓ | ✓ | ✓ |
| 发布商品 | - | ✓ | ✓ |
| 管理自己的商品 | - | ✓ | ✓ |
| 回复留言 | - | ✓ | ✓ |
| 管理所有订单 | - | ✓ | ✓ |
| 管理用户 | - | - | ✓ |
| 管理商户 | - | - | ✓ |
