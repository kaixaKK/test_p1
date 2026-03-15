<template>
  <div class="merchant-orders">
    <h2>订单管理</h2>
    <div class="order-list">
      <table>
        <thead>
          <tr>
            <th>订单号</th>
            <th>用户ID</th>
            <th>商品</th>
            <th>总价</th>
            <th>状态</th>
            <th>时间</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id">
            <td>{{ order.id }}</td>
            <td>{{ order.userId }}</td>
            <td>
              <div v-for="item in order.items" :key="item.id">
                {{ item.productName }} × {{ item.quantity }}
              </div>
            </td>
            <td>¥{{ order.totalAmount }}</td>
            <td>{{ order.status }}</td>
            <td>{{ formatDate(order.createdAt) }}</td>
            <td>
              <button v-if="order.status === 'PAID'" @click="shipOrder(order.id)">发货</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <div v-if="orders.length === 0" class="empty">暂无订单</div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import api from '../../api'

export default {
  name: 'MerchantOrders',
  setup() {
    const orders = ref([])

    const loadOrders = async () => {
      try {
        const res = await api.getAllOrders()
        orders.value = res.data
      } catch (error) {
        console.error('Failed to load orders:', error)
      }
    }

    const shipOrder = async (orderId) => {
      try {
        await api.shipOrder(orderId)
        loadOrders()
      } catch (error) {
        alert('操作失败')
      }
    }

    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    onMounted(() => {
      loadOrders()
    })

    return { orders, shipOrder, formatDate }
  }
}
</script>

<style scoped>
.merchant-orders {
  padding: 20px 0;
}
.merchant-orders h2 {
  margin-bottom: 20px;
}
.order-list {
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
  border: 1px solid #4caf50;
  background: #fff;
  color: #4caf50;
  border-radius: 4px;
  cursor: pointer;
}
td button:hover {
  background: #4caf50;
  color: #fff;
}
.empty {
  text-align: center;
  padding: 60px;
  background: #fff;
  border-radius: 8px;
  color: #999;
}
</style>
