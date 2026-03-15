<template>
  <div class="cart">
    <h2>购物车</h2>
    <div v-if="!userStore.isLoggedIn()" class="login-tip">
      请先 <router-link to="/login">登录</router-link>
    </div>
    <template v-else>
      <div v-if="cartProducts.length > 0" class="cart-content">
        <div class="cart-list">
          <div v-for="item in cartProducts" :key="item.cartId" class="cart-item">
            <div class="item-image">
              <img :src="item.imageUrl || 'https://via.placeholder.com/100'" :alt="item.name" />
            </div>
            <div class="item-info">
              <h3>{{ item.name }}</h3>
              <p class="price">¥{{ item.price }}</p>
            </div>
            <div class="item-quantity">
              <button @click="updateQuantity(item.cartId, item.cartQuantity - 1)">-</button>
              <span>{{ item.cartQuantity }}</span>
              <button @click="updateQuantity(item.cartId, item.cartQuantity + 1)">+</button>
            </div>
            <div class="item-subtotal">
              ¥{{ (item.price * item.cartQuantity).toFixed(2) }}
            </div>
            <button class="delete-btn" @click="removeItem(item.cartId)">删除</button>
          </div>
        </div>
        <div class="cart-summary">
          <div class="total">
            总计: <span>¥{{ total.toFixed(2) }}</span>
          </div>
          <button class="checkout-btn" @click="checkout">结算</button>
        </div>
      </div>
      <div v-else class="empty-cart">
        <p>购物车为空</p>
        <router-link to="/">去逛逛</router-link>
      </div>
    </template>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../stores/user'
import api from '../../api'

export default {
  name: 'Cart',
  setup() {
    const router = useRouter()
    const userStore = useUserStore()
    const cartProducts = ref([])

    const total = computed(() => {
      return cartProducts.value.reduce((sum, item) => {
        return sum + item.price * item.cartQuantity
      }, 0)
    })

    const loadCart = async () => {
      if (!userStore.user) return
      try {
        const res = await api.getCart(userStore.user.id)
        cartProducts.value = res.data
      } catch (error) {
        console.error('Failed to load cart:', error)
      }
    }

    const updateQuantity = async (cartItemId, quantity) => {
      if (quantity < 1) return
      try {
        await api.updateCartItem(cartItemId, quantity)
        loadCart()
      } catch (error) {
        alert('更新失败')
      }
    }

    const removeItem = async (cartItemId) => {
      try {
        await api.deleteCartItem(cartItemId)
        loadCart()
      } catch (error) {
        alert('删除失败')
      }
    }

    const checkout = async () => {
      const cartItemIds = cartProducts.value.map(item => item.cartId)
      try {
        const res = await api.createOrder({
          userId: userStore.user.id,
          cartItemIds
        })
        if (res.data.success) {
          alert('订单创建成功')
          loadCart()
          router.push('/orders')
        } else {
          alert(res.data.message || '创建订单失败')
        }
      } catch (error) {
        alert('创建订单失败')
      }
    }

    onMounted(() => {
      loadCart()
    })

    return {
      userStore,
      cartProducts,
      total,
      updateQuantity,
      removeItem,
      checkout
    }
  }
}
</script>

<style scoped>
.cart {
  padding: 20px 0;
}
.cart h2 {
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
.cart-content {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
}
.cart-list {
  padding: 20px;
}
.cart-item {
  display: flex;
  align-items: center;
  padding: 15px 0;
  border-bottom: 1px solid #eee;
}
.item-image {
  width: 80px;
  height: 80px;
  background: #f5f5f5;
  border-radius: 4px;
  overflow: hidden;
  margin-right: 15px;
}
.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.item-info {
  flex: 1;
}
.item-info h3 {
  font-size: 14px;
  margin-bottom: 5px;
}
.price {
  color: #f44336;
}
.item-quantity {
  display: flex;
  align-items: center;
  margin: 0 20px;
}
.item-quantity button {
  width: 28px;
  height: 28px;
  border: 1px solid #ddd;
  background: #fff;
  cursor: pointer;
}
.item-quantity span {
  width: 40px;
  text-align: center;
}
.item-subtotal {
  font-size: 18px;
  font-weight: bold;
  color: #f44336;
  width: 100px;
  text-align: right;
  margin-right: 15px;
}
.delete-btn {
  padding: 5px 10px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
  color: #999;
}
.delete-btn:hover {
  background: #f44336;
  color: #fff;
  border-color: #f44336;
}
.cart-summary {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background: #f9f9f9;
}
.total {
  font-size: 16px;
}
.total span {
  font-size: 24px;
  font-weight: bold;
  color: #f44336;
}
.checkout-btn {
  padding: 12px 40px;
  background: #ff6700;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
}
.checkout-btn:hover {
  background: #e65c00;
}
.empty-cart {
  text-align: center;
  padding: 60px;
  background: #fff;
  border-radius: 8px;
}
.empty-cart p {
  margin-bottom: 20px;
  color: #999;
}
.empty-cart a {
  color: #ff6700;
}
</style>
