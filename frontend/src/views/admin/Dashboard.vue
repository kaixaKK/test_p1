<template>
  <div class="admin-dashboard">
    <h2>管理后台</h2>
    <div class="dashboard-links">
      <router-link to="/admin/users" class="dash-card">
        <h3>用户管理</h3>
        <p>管理普通用户</p>
      </router-link>
      <router-link to="/admin/merchants" class="dash-card">
        <h3>商户管理</h3>
        <p>管理商户账号</p>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'AdminDashboard'
}
</script>

<style scoped>
.admin-dashboard {
  padding: 20px 0;
}
.admin-dashboard h2 {
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
  color: #f44336;
}
.dash-card p {
  color: #666;
  font-size: 14px;
}
</style>
