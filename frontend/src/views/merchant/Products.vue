<template>
  <div class="merchant-products">
    <div class="header">
      <h2>商品管理</h2>
      <button @click="showAddForm = true" class="add-btn">添加商品</button>
    </div>

    <div v-if="showAddForm || editingProduct" class="product-form">
      <h3>{{ editingProduct ? '编辑商品' : '添加商品' }}</h3>
      <form @submit.prevent="saveProduct">
        <input v-model="form.name" placeholder="商品名称" required />
        <textarea v-model="form.description" placeholder="商品描述"></textarea>
        <input v-model="form.price" type="number" step="0.01" placeholder="价格" required />
        <input v-model="form.stock" type="number" placeholder="库存" required />
        <input v-model="form.imageUrl" placeholder="图片URL" />
        <select v-model="form.category">
          <option value="">选择分类</option>
          <option value="电子产品">电子产品</option>
          <option value="服装">服装</option>
          <option value="食品">食品</option>
          <option value="图书">图书</option>
          <option value="其他">其他</option>
        </select>
        <label class="checkbox-label">
          <input v-model="form.isSeckill" type="checkbox" />
          秒杀商品
        </label>
        <template v-if="form.isSeckill">
          <input v-model="form.seckillPrice" type="number" step="0.01" placeholder="秒杀价格" />
          <input v-model="form.seckillStartTime" type="datetime-local" placeholder="秒杀开始时间" />
          <input v-model="form.seckillEndTime" type="datetime-local" placeholder="秒杀结束时间" />
        </template>
        <div class="form-actions">
          <button type="submit" class="save-btn">保存</button>
          <button type="button" @click="cancelForm" class="cancel-btn">取消</button>
        </div>
      </form>
    </div>

    <div class="product-list">
      <table>
        <thead>
          <tr>
            <th>ID</th>
            <th>名称</th>
            <th>价格</th>
            <th>库存</th>
            <th>状态</th>
            <th>秒杀</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="product in products" :key="product.id">
            <td>{{ product.id }}</td>
            <td>{{ product.name }}</td>
            <td>¥{{ product.price }}</td>
            <td>{{ product.stock }}</td>
            <td>{{ product.status }}</td>
            <td>{{ product.isSeckill ? '是' : '否' }}</td>
            <td>
              <button @click="editProduct(product)">编辑</button>
              <button @click="toggleStatus(product)">{{ product.status === 'ON_SALE' ? '下架' : '上架' }}</button>
              <button @click="deleteProduct(product.id)" class="delete-btn">删除</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import api from '../../api'

export default {
  name: 'MerchantProducts',
  setup() {
    const userStore = useUserStore()
    const products = ref([])
    const showAddForm = ref(false)
    const editingProduct = ref(null)
    const form = reactive({
      name: '',
      description: '',
      price: '',
      stock: '',
      imageUrl: '',
      category: '',
      isSeckill: false,
      seckillPrice: '',
      seckillStartTime: '',
      seckillEndTime: ''
    })

    const loadProducts = async () => {
      try {
        const res = await api.getProductsByMerchant(userStore.user.id)
        products.value = res.data
      } catch (error) {
        console.error('Failed to load products:', error)
      }
    }

    const resetForm = () => {
      form.name = ''
      form.description = ''
      form.price = ''
      form.stock = ''
      form.imageUrl = ''
      form.category = ''
      form.isSeckill = false
      form.seckillPrice = ''
      form.seckillStartTime = ''
      form.seckillEndTime = ''
    }

    const saveProduct = async () => {
      try {
        const data = {
          ...form,
          merchantId: userStore.user.id,
          seckillStartTime: form.seckillStartTime ? new Date(form.seckillStartTime).toISOString() : null,
          seckillEndTime: form.seckillEndTime ? new Date(form.seckillEndTime).toISOString() : null
        }
        if (editingProduct.value) {
          await api.updateProduct(editingProduct.value.id, data)
        } else {
          await api.createProduct(data)
        }
        showAddForm.value = false
        editingProduct.value = null
        resetForm()
        loadProducts()
      } catch (error) {
        alert('保存失败')
      }
    }

    const editProduct = (product) => {
      editingProduct.value = product
      form.name = product.name
      form.description = product.description
      form.price = product.price
      form.stock = product.stock
      form.imageUrl = product.imageUrl
      form.category = product.category
      form.isSeckill = product.isSeckill
      form.seckillPrice = product.seckillPrice
      form.seckillStartTime = product.seckillStartTime ? product.seckillStartTime.slice(0, 16) : ''
      form.seckillEndTime = product.seckillEndTime ? product.seckillEndTime.slice(0, 16) : ''
    }

    const cancelForm = () => {
      showAddForm.value = false
      editingProduct.value = null
      resetForm()
    }

    const toggleStatus = async (product) => {
      const newStatus = product.status === 'ON_SALE' ? 'OFF_SALE' : 'ON_SALE'
      try {
        await api.updateProduct(product.id, { ...product, status: newStatus })
        loadProducts()
      } catch (error) {
        alert('操作失败')
      }
    }

    const deleteProduct = async (id) => {
      if (!confirm('确定删除?')) return
      try {
        await api.deleteProduct(id)
        loadProducts()
      } catch (error) {
        alert('删除失败')
      }
    }

    onMounted(() => {
      loadProducts()
    })

    return {
      products,
      showAddForm,
      editingProduct,
      form,
      saveProduct,
      editProduct,
      cancelForm,
      toggleStatus,
      deleteProduct
    }
  }
}
</script>

<style scoped>
.merchant-products {
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
  background: #4caf50;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.product-form {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  margin-bottom: 20px;
}
.product-form h3 {
  margin-bottom: 15px;
}
.product-form input,
.product-form textarea,
.product-form select {
  width: 100%;
  padding: 10px;
  margin-bottom: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.product-form textarea {
  height: 80px;
  resize: none;
}
.checkbox-label {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
}
.form-actions {
  display: flex;
  gap: 10px;
}
.save-btn {
  padding: 10px 20px;
  background: #4caf50;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.form-actions .cancel-btn {
  padding: 10px 20px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
}
.product-list {
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
  margin-right: 5px;
  padding: 5px 10px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 4px;
  cursor: pointer;
}
td .delete-btn {
  color: #f44336;
  border-color: #f44336;
}
</style>
