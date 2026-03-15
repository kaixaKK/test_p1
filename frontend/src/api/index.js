import axios from 'axios'

const api = axios.create({
  baseURL: '/api'
})

export default {
  // User API
  login(data) {
    return api.post('/users/login', data)
  },
  register(data) {
    return api.post('/users/register', data)
  },
  getUser(id) {
    return api.get(`/users/${id}`)
  },
  getAllUsers() {
    return api.get('/users')
  },
  getUsersByRole(role) {
    return api.get(`/users/role/${role}`)
  },
  updateUser(id, data) {
    return api.put(`/users/${id}`, data)
  },
  deleteUser(id) {
    return api.delete(`/users/${id}`)
  },

  // Product API
  getProducts() {
    return api.get('/products')
  },
  getOnSaleProducts() {
    return api.get('/products/onsale')
  },
  getProduct(id) {
    return api.get(`/products/${id}`)
  },
  getProductsByMerchant(merchantId) {
    return api.get(`/products/merchant/${merchantId}`)
  },
  getProductsByCategory(category) {
    return api.get(`/products/category/${category}`)
  },
  getSeckillProducts() {
    return api.get('/products/seckill')
  },
  createProduct(data) {
    return api.post('/products', data)
  },
  updateProduct(id, data) {
    return api.put(`/products/${id}`, data)
  },
  deleteProduct(id) {
    return api.delete(`/products/${id}`)
  },

  // Cart API
  getCart(userId) {
    return api.get(`/cart/${userId}`)
  },
  addToCart(data) {
    return api.post('/cart/add', data)
  },
  updateCartItem(cartItemId, quantity) {
    return api.put(`/cart/update/${cartItemId}`, { quantity })
  },
  deleteCartItem(cartItemId) {
    return api.delete(`/cart/${cartItemId}`)
  },
  clearCart(userId) {
    return api.delete(`/cart/clear/${userId}`)
  },

  // Order API
  getOrders(userId) {
    return api.get(`/orders/user/${userId}`)
  },
  getAllOrders() {
    return api.get('/orders')
  },
  getOrder(id) {
    return api.get(`/orders/${id}`)
  },
  createOrder(data) {
    return api.post('/orders/create', data)
  },
  payOrder(orderId) {
    return api.post(`/orders/pay/${orderId}`)
  },
  shipOrder(orderId) {
    return api.post(`/orders/ship/${orderId}`)
  },
  deliverOrder(orderId) {
    return api.post(`/orders/deliver/${orderId}`)
  },
  cancelOrder(orderId) {
    return api.post(`/orders/cancel/${orderId}`)
  },

  // Message API
  getMessagesByProduct(productId) {
    return api.get(`/messages/product/${productId}`)
  },
  getAllMessages() {
    return api.get('/messages')
  },
  getMessagesByMerchant(merchantId) {
    return api.get(`/messages/merchant/${merchantId}`)
  },
  createMessage(data) {
    return api.post('/messages', data)
  },
  replyMessage(messageId, reply) {
    return api.post(`/messages/reply/${messageId}`, { reply })
  },
  deleteMessage(id) {
    return api.delete(`/messages/${id}`)
  }
}
