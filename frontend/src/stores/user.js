import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUserStore = defineStore('user', () => {
  const user = ref(JSON.parse(localStorage.getItem('user') || 'null'))

  function setUser(newUser) {
    user.value = newUser
    if (newUser) {
      localStorage.setItem('user', JSON.stringify(newUser))
    } else {
      localStorage.removeItem('user')
    }
  }

  function logout() {
    user.value = null
    localStorage.removeItem('user')
  }

  function isLoggedIn() {
    return user.value !== null
  }

  function isMerchant() {
    return user.value?.role === 'MERCHANT'
  }

  function isAdmin() {
    return user.value?.role === 'ADMIN'
  }

  return { user, setUser, logout, isLoggedIn, isMerchant, isAdmin }
})
