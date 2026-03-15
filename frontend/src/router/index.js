import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

// User views
import Home from '../views/user/Home.vue'
import ProductDetail from '../views/user/ProductDetail.vue'
import Cart from '../views/user/Cart.vue'
import Orders from '../views/user/Orders.vue'
import Seckill from '../views/user/Seckill.vue'

// Merchant views
import MerchantDashboard from '../views/merchant/Dashboard.vue'
import MerchantProducts from '../views/merchant/Products.vue'
import MerchantOrders from '../views/merchant/Orders.vue'
import MerchantMessages from '../views/merchant/Messages.vue'

// Admin views
import AdminDashboard from '../views/admin/Dashboard.vue'
import AdminUsers from '../views/admin/Users.vue'
import AdminMerchants from '../views/admin/Merchants.vue'

// Login
import Login from '../components/Login.vue'

const routes = [
  { path: '/', name: 'Home', component: Home },
  { path: '/login', name: 'Login', component: Login },
  { path: '/product/:id', name: 'ProductDetail', component: ProductDetail },
  { path: '/cart', name: 'Cart', component: Cart },
  { path: '/orders', name: 'Orders', component: Orders },
  { path: '/seckill', name: 'Seckill', component: Seckill },

  { path: '/merchant', name: 'MerchantDashboard', component: MerchantDashboard },
  { path: '/merchant/products', name: 'MerchantProducts', component: MerchantProducts },
  { path: '/merchant/orders', name: 'MerchantOrders', component: MerchantOrders },
  { path: '/merchant/messages', name: 'MerchantMessages', component: MerchantMessages },

  { path: '/admin', name: 'AdminDashboard', component: AdminDashboard },
  { path: '/admin/users', name: 'AdminUsers', component: AdminUsers },
  { path: '/admin/merchants', name: 'AdminMerchants', component: AdminMerchants }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.path.startsWith('/merchant') && !userStore.isMerchant() && !userStore.isAdmin()) {
    next('/login')
  } else if (to.path.startsWith('/admin') && !userStore.isAdmin()) {
    next('/login')
  } else {
    next()
  }
})

export default router
