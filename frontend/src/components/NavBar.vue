<template>
  <nav class="navbar">
    <div class="nav-container">
      <div class="nav-left">
        <router-link to="/" class="logo">简易淘宝</router-link>
        <router-link to="/" class="nav-link">首页</router-link>
        <router-link to="/seckill" class="nav-link">秒杀专区</router-link>
        <router-link to="/cart" class="nav-link">购物车</router-link>
        <router-link to="/orders" class="nav-link">我的订单</router-link>
      </div>
      <div class="nav-right">
        <template v-if="userStore.isLoggedIn()">
          <span class="username">{{ userStore.user.username }}</span>
          <span class="role-badge" :class="userStore.user.role">{{ userStore.user.role }}</span>
          <template v-if="userStore.isMerchant() || userStore.isAdmin()">
            <router-link to="/merchant" class="nav-link merchant-link">商户中心</router-link>
          </template>
          <template v-if="userStore.isAdmin()">
            <router-link to="/admin" class="nav-link admin-link">管理后台</router-link>
          </template>
          <button @click="logout" class="logout-btn">退出</button>
        </template>
        <template v-else>
          <router-link to="/login" class="nav-link">登录</router-link>
        </template>
      </div>
    </div>
  </nav>
</template>

<script>
import { useUserStore } from '../stores/user'

export default {
  name: 'NavBar',
  setup() {
    const userStore = useUserStore()

    const logout = () => {
      userStore.logout()
      window.location.href = '/'
    }

    return { userStore, logout }
  }
}
</script>

<style scoped>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: #fff;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  z-index: 1000;
}
.nav-container {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
}
.nav-left {
  display: flex;
  align-items: center;
  gap: 20px;
}
.logo {
  font-size: 20px;
  font-weight: bold;
  color: #ff6700;
  text-decoration: none;
}
.nav-link {
  text-decoration: none;
  color: #333;
  font-size: 14px;
}
.nav-link:hover {
  color: #ff6700;
}
.nav-right {
  display: flex;
  align-items: center;
  gap: 15px;
}
.username {
  font-size: 14px;
  color: #333;
}
.role-badge {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
}
.role-badge.USER { background: #e0e0e0; color: #333; }
.role-badge.MERCHANT { background: #4caf50; color: #fff; }
.role-badge.ADMIN { background: #f44336; color: #fff; }
.logout-btn {
  padding: 5px 15px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}
.logout-btn:hover {
  background: #f5f5f5;
}
.merchant-link { color: #4caf50 !important; }
.admin-link { color: #f44336 !important; }
</style>
