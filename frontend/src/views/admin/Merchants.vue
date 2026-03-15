<template>
  <div class="admin-merchants">
    <div class="header">
      <h2>商户管理</h2>
      <button @click="showAddForm = true" class="add-btn">添加商户</button>
    </div>

    <div v-if="showAddForm" class="merchant-form">
      <h3>添加商户</h3>
      <form @submit.prevent="addMerchant">
        <input v-model="form.username" placeholder="用户名" required />
        <input v-model="form.password" type="password" placeholder="密码" required />
        <input v-model="form.phone" placeholder="手机号" />
        <input v-model="form.email" placeholder="邮箱" />
        <div class="form-actions">
          <button type="submit" class="save-btn">保存</button>
          <button type="button" @click="showAddForm = false" class="cancel-btn">取消</button>
        </div>
      </form>
    </div>

    <div class="merchant-list">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>用户名</th>
            <th>角色</th>
            <th>手机</th>
            <th>邮箱</th>
            <th>创建时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in merchants" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.role }}</td>
            <td>{{ user.phone }}</td>
            <td>{{ user.email }}</td>
            <td>{{ formatDate(user.createdAt) }}</td>
            <td>
              <button @click="deleteMerchant(user.id)" class="delete-btn">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import api from '../../api'

export default {
  name: 'AdminMerchants',
  setup() {
    const merchants = ref([])
    const showAddForm = ref(false)
    const form = reactive({
      username: '',
      password: '',
      phone: '',
      email: ''
    })

    const loadMerchants = async () => {
      try {
        const res = await api.getUsersByRole('MERCHANT')
        merchants.value = res.data
      } catch (error) {
        console.error('Failed to load merchants:', error)
      }
    }

    const addMerchant = async () => {
      try {
        await api.register({ ...form, role: 'MERCHANT' })
        showAddForm.value = false
        form.username = ''
        form.password = ''
        form.phone = ''
        form.email = ''
        loadMerchants()
      } catch (error) {
        alert('添加失败')
      }
    }

    const deleteMerchant = async (id) => {
      if (!confirm('确定删除?')) return
      try {
        await api.deleteUser(id)
        loadMerchants()
      } catch (error) {
        alert('删除失败')
      }
    }

    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    onMounted(() => {
      loadMerchants()
    })

    return { merchants, showAddForm, form, addMerchant, deleteMerchant, formatDate }
  }
}
</script>

<style scoped>
.admin-merchants {
  padding: 20px 0;
}
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.add-btn {
  padding: 10px 20px;
  background: #f44336;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.merchant-form {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}
.merchant-form h3 {
  margin-bottom: 15px;
}
.merchant-form input {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.form-actions {
  display: flex;
  gap: 10px;
}
.save-btn {
  padding: 10px 20px;
  background: #f44336;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.cancel-btn {
  padding: 10px 20px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
}
.merchant-list {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}
table {
  width: 100%;
  border-collapse: collapse;
}
th, td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #eee;
}
th {
  background: #f5f5f5;
  font-weight: 500;
}
td button {
  padding: 5px 10px;
  border: 1px solid #f44336;
  background: #fff;
  color: #f44336;
  border-radius: 4px;
  cursor: pointer;
}
td button:hover {
  background: #f44336;
  color: #fff;
}
</style>
