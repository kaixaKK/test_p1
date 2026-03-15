<template>
  <div class="merchant-dashboard">
    <h2>商户中心</h2>
    <div class="dashboard-links">
      <router-link to="/merchant/products" class="dash-card">
        <h3>商品管理</h3>
        <p>管理您的商品</p>
      </router-link>
      <router-link to="/merchant/orders" class="dash-card">
        <h3>订单管理</h3>
        <p>查看和处理订单</p>
      </router-link>
      <router-link to="/merchant/messages" class="dash-card">
        <h3>留言管理</h3>
        <p>回复用户留言</p>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MerchantDashboard'
}
</script>

<style scoped>
.merchant-dashboard {
  padding: 20px 0;
}
.merchant-dashboard h2 {
  margin-bottom: 20px;
}
.dashboard-links {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}
.dash-card {
  background: #fff;
  padding: 30px;
  border-radius: 8px;
  text-decoration: none;
  color: #333;
  transition: transform 0.2s, box-shadow 0.2s;
}
.dash-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
.dash-card h3 {
  margin-bottom: 10px;
  color: #4caf50;
}
.dash-card p {
  color: #666;
  font-size: 14px;
}
</style>
