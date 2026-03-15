<template>
  <div class="login-container">
    <div class="login-box">
      <h2>{{ isRegister ? '用户注册' : '用户登录' }}</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <input v-model="form.username" type="text" placeholder="用户名" required />
        </div>
        <div class="form-group">
          <input v-model="form.password" type="password" placeholder="密码" required />
        </div>
        <div class="form-group" v-if="isRegister">
          <input v-model="form.phone" type="text" placeholder="手机号" />
        </div>
        <div class="form-group" v-if="isRegister">
          <input v-model="form.email" type="email" placeholder="邮箱" />
        </div>
        <button type="submit" class="submit-btn">{{ isRegister ? '注册' : '登录' }}</button>
      </form>
      <div class="toggle-link">
        <a href="#" @click.prevent="isRegister = !isRegister">
          {{ isRegister ? '已有账号？去登录' : '没有账号？去注册' }}
        </a>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import api from '../api'

export default {
  name: 'Login',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const isRegister = ref(false)
    const form = reactive({
      username: '',
      password: '',
      phone: '',
      email: ''
    })

    const handleSubmit = async () => {
      try {
        if (isRegister.value) {
          const res = await api.register(form)
          if (res.data.success) {
            alert('注册成功，请登录')
            isRegister.value = false
          } else {
            alert(res.data.message || '注册失败')
          }
        } else {
          const res = await api.login(form)
          if (res.data.success) {
            userStore.setUser(res.data.user)
            window.location.href = '/'
          } else {
            alert(res.data.message || '登录失败')
          }
        }
      } catch (error) {
        alert('操作失败，请重试')
      }
    }

    return { isRegister, form, handleSubmit }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: calc(100vh - 60px);
  padding: 40px 20px;
}
.login-box {
  width: 360px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.login-box h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #333;
}
.form-group {
  margin-bottom: 20px;
}
.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}
.submit-btn {
  width: 100%;
  padding: 12px;
  background: #ff6700;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}
.submit-btn:hover {
  background: #e65c00;
}
.toggle-link {
  text-align: center;
  margin-top: 20px;
}
.toggle-link a {
  color: #ff6700;
  text-decoration: none;
  font-size: 14px;
}
</style>
