<template>
  <div class="orders">
    <h2>我的订单</h2>
    <div v-if="!userStore.isLoggedIn()" class="login-tip">
      请先 <router-link to="/login">登录</router-link>
    </div>
    <template v-else>
      <div v-if="orders.length > 0" class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-item">
          <div class="order-header">
            <span class="order-id">订单号: {{ order.id }}</span>
            <span class="order-status" :class="order.status">{{ order.status }}</span>
          </div>
          <div class="order-items">
            <div v-for="item in order.items" :key="item.id" class="order-product">
              <span>{{ item.productName }}</span>
              <span>¥{{ item.price }} × {{ item.quantity }}</span>
            </div>
          </div>
          <div class="order-footer">
            <span class="total-amount">总价: ¥{{ order.totalAmount }}</span>
            <div class="order-actions">
              <button v-if="order.status === 'PENDING'" @click="payOrder(order.id)" class="pay-btn">支付</button>
              <button v-if="order.status === 'PENDING'" @click="cancelOrder(order.id)" class="cancel-btn">取消</button>
              <button v-if="order.status === 'SHIPPED'" @click="confirmReceive(order.id)" class="receive-btn">确认收货</button>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="empty-orders">
        <p>暂无订单</p>
        <router-link to="/">去购物</router-link>
      </div>
    </template>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import api from '../../api'

export default {
  name: 'Orders',
  setup() {
    const userStore = useUserStore()
    const orders = ref([])

    const loadOrders = async () => {
      if (!userStore.user) return
      try {
        const res = await api.getOrders(userStore.user.id)
        orders.value = res.data
      } catch (error) {
        console.error('Failed to load orders:', error)
      }
    }

    const payOrder = async (orderId) => {
      try {
        await api.payOrder(orderId)
        loadOrders()
        alert('支付成功')
      } catch (error) {
        alert('支付失败')
      }
    }

    const cancelOrder = async (orderId) => {
      try {
        await api.cancelOrder(orderId)
        loadOrders()
      } catch (error) {
        alert('操作失败')
      }
    }

    const confirmReceive = async (orderId) => {
      try {
        await api.deliverOrder(orderId)
        loadOrders()
      } catch (error) {
        alert('操作失败')
      }
    }

    onMounted(() => {
      loadOrders()
    })

    return {
      userStore,
      orders,
      payOrder,
      cancelOrder,
      confirmReceive
    }
  }
}
</script>

<style scoped>
.orders {
  padding: 20px 0;
}
.orders h2 {
  margin-bottom: 20px;
}
.login-tip {
  text-align: center;
  padding: 60px;
  background: #fff;
  border-radius: 8px;
}
.login-tip a {
  color: #ff6700;
}
.order-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
.order-item {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #f5f5f5;
}
.order-id {
  color: #666;
  font-size: 14px;
}
.order-status {
  padding: 4px 12px;
  border-radius: 4px;
  font-size: 12px;
}
.order-status.PENDING { background: #fff3e0; color: #ff9800; }
.order-status.PAID { background: #e3f2fd; color: #2196f3; }
.order-status.SHIPPED { background: #e8f5e9; color: #4caf50; }
.order-status.DELIVERED { background: #e0e0e0; color: #666; }
.order-status.CANCELLED { background: #ffebee; color: #f44336; }
.order-items {
  padding: 15px 20px;
}
.order-product {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  color: #666;
}
.order-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  border-top: 1px solid #eee;
}
.total-amount {
  font-size: 18px;
  font-weight: bold;
  color: #f44336;
}
.order-actions {
  display: flex;
  gap: 10px;
}
.pay-btn {
  padding: 8px 20px;
  background: #ff6700;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.cancel-btn {
  padding: 8px 20px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
}
.receive-btn {
  padding: 8px 20px;
  background: #4caf50;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.empty-orders {
  text-align: center;
  padding: 60px;
  background: #fff;
  border-radius: 8px;
}
.empty-orders p {
  margin-bottom: 20px;
  color: #999;
}
.empty-orders a {
  color: #ff6700;
}
</style>
