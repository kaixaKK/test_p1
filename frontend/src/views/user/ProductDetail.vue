<template>
  <div class="product-detail" v-if="product">
    <div class="detail-container">
      <div class="detail-image">
        <img :src="product.imageUrl || 'https://via.placeholder.com/400'" :alt="product.name" />
      </div>
      <div class="detail-info">
        <h1>{{ product.name }}</h1>
        <p class="description">{{ product.description }}</p>
        <div class="price-section">
          <span class="current-price">¥{{ seckillPrice }}</span>
          <span v-if="product.isSeckill" class="original-price">¥{{ product.price }}</span>
        </div>
        <div class="stock-info">
          <span>库存: {{ product.stock }}</span>
          <span>分类: {{ product.category }}</span>
        </div>
        <div class="seckill-info" v-if="product.isSeckill">
          <p>秒杀价: ¥{{ product.seckillPrice }}</p>
          <p>秒杀时间: {{ formatTime(product.seckillStartTime) }} - {{ formatTime(product.seckillEndTime) }}</p>
        </div>
        <div class="action-buttons">
          <div class="quantity-input">
            <button @click="quantity > 1 && quantity--">-</button>
            <input type="number" v-model="quantity" min="1" :max="product.stock" />
            <button @click="quantity < product.stock && quantity++">+</button>
          </div>
          <button class="add-cart-btn" @click="addToCart">加入购物车</button>
        </div>
      </div>
    </div>

    <div class="message-section">
      <h3>留言</h3>
      <div v-if="userStore.isLoggedIn()" class="message-form">
        <textarea v-model="messageContent" placeholder="写下您的留言..."></textarea>
        <button @click="submitMessage">提交留言</button>
      </div>
      <div v-else class="login-tip">登录后可留言</div>
      <div class="message-list">
        <div v-for="msg in messages" :key="msg.id" class="message-item">
          <div class="message-header">
            <span class="username">{{ msg.username }}</span>
            <span class="time">{{ formatDate(msg.createdAt) }}</span>
          </div>
          <div class="message-content">{{ msg.content }}</div>
          <div v-if="msg.reply" class="message-reply">
            <strong>商家回复:</strong> {{ msg.reply }}
          </div>
        </div>
        <div v-if="messages.length === 0" class="empty-messages">暂无留言</div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useUserStore } from '../../stores/user'
import api from '../../api'

export default {
  name: 'ProductDetail',
  setup() {
    const route = useRoute()
    const userStore = useUserStore()
    const product = ref(null)
    const messages = ref([])
    const quantity = ref(1)
    const messageContent = ref('')

    const seckillPrice = computed(() => {
      if (product.value?.isSeckill) {
        return product.value.seckillPrice
      }
      return product.value?.price
    })

    const loadProduct = async () => {
      try {
        const res = await api.getProduct(route.params.id)
        product.value = res.data
      } catch (error) {
        console.error('Failed to load product:', error)
      }
    }

    const loadMessages = async () => {
      try {
        const res = await api.getMessagesByProduct(route.params.id)
        messages.value = res.data
      } catch (error) {
        console.error('Failed to load messages:', error)
      }
    }

    const addToCart = async () => {
      if (!userStore.isLoggedIn()) {
        alert('请先登录')
        return
      }
      try {
        await api.addToCart({
          userId: userStore.user.id,
          productId: product.value.id,
          quantity: quantity.value
        })
        alert('添加成功')
      } catch (error) {
        alert('添加失败')
      }
    }

    const submitMessage = async () => {
      if (!messageContent.value.trim()) return
      try {
        await api.createMessage({
          productId: product.value.id,
          userId: userStore.user.id,
          content: messageContent.value
        })
        messageContent.value = ''
        loadMessages()
      } catch (error) {
        alert('提交失败')
      }
    }

    const formatTime = (time) => {
      if (!time) return ''
      return new Date(time).toLocaleString('zh-CN')
    }

    const formatDate = (time) => {
      if (!time) return ''
      return new Date(time).toLocaleString('zh-CN')
    }

    onMounted(() => {
      loadProduct()
      loadMessages()
    })

    return {
      product,
      messages,
      quantity,
      messageContent,
      userStore,
      seckillPrice,
      addToCart,
      submitMessage,
      formatTime,
      formatDate
    }
  }
}
</script>

<style scoped>
.product-detail {
  padding: 20px 0;
}
.detail-container {
  display: flex;
  gap: 40px;
  background: #fff;
  padding: 30px;
  border-radius: 8px;
}
.detail-image {
  width: 400px;
  height: 400px;
  background: #f5f5f5;
  border-radius: 8px;
  overflow: hidden;
}
.detail-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.detail-info {
  flex: 1;
}
.detail-info h1 {
  font-size: 24px;
  margin-bottom: 15px;
}
.description {
  color: #666;
  margin-bottom: 20px;
  line-height: 1.6;
}
.price-section {
  margin-bottom: 20px;
}
.current-price {
  font-size: 32px;
  font-weight: bold;
  color: #f44336;
}
.original-price {
  font-size: 16px;
  color: #999;
  text-decoration: line-through;
  margin-left: 10px;
}
.stock-info {
  display: flex;
  gap: 20px;
  color: #666;
  margin-bottom: 15px;
}
.seckill-info {
  background: #ffebee;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}
.seckill-info p {
  margin: 5px 0;
  color: #f44336;
}
.action-buttons {
  display: flex;
  gap: 15px;
  align-items: center;
}
.quantity-input {
  display: flex;
  align-items: center;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.quantity-input button {
  width: 36px;
  height: 36px;
  border: none;
  background: #f5f5f5;
  cursor: pointer;
}
.quantity-input input {
  width: 60px;
  height: 36px;
  text-align: center;
  border: none;
  border-left: 1px solid #ddd;
  border-right: 1px solid #ddd;
}
.add-cart-btn {
  padding: 10px 30px;
  background: #ff6700;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}
.add-cart-btn:hover {
  background: #e65c00;
}
.message-section {
  margin-top: 30px;
  background: #fff;
  padding: 30px;
  border-radius: 8px;
}
.message-section h3 {
  margin-bottom: 20px;
}
.message-form {
  margin-bottom: 20px;
}
.message-form textarea {
  width: 100%;
  height: 80px;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  margin-bottom: 10px;
  resize: none;
}
.message-form button {
  padding: 8px 20px;
  background: #ff6700;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.login-tip {
  padding: 20px;
  text-align: center;
  color: #999;
  background: #f5f5f5;
  border-radius: 4px;
  margin-bottom: 20px;
}
.message-list {
  max-height: 400px;
  overflow-y: auto;
}
.message-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
}
.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
.username {
  font-weight: bold;
  color: #333;
}
.time {
  color: #999;
  font-size: 12px;
}
.message-content {
  color: #666;
  line-height: 1.5;
}
.message-reply {
  margin-top: 10px;
  padding: 10px;
  background: #f5f5f5;
  border-radius: 4px;
  color: #666;
}
.empty-messages {
  text-align: center;
  padding: 40px;
  color: #999;
}
</style>
