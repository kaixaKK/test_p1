<template>
  <div class="home">
    <div class="category-filter">
      <button
        v-for="cat in categories"
        :key="cat"
        :class="{ active: selectedCategory === cat }"
        @click="selectedCategory = cat"
      >
        {{ cat }}
      </button>
    </div>
    <div class="product-grid">
      <ProductCard v-for="product in filteredProducts" :key="product.id" :product="product" />
    </div>
    <div v-if="filteredProducts.length === 0" class="empty">暂无商品</div>
  </div>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import api from '../../api'
import ProductCard from '../../components/ProductCard.vue'

export default {
  name: 'Home',
  components: { ProductCard },
  setup() {
    const products = ref([])
    const selectedCategory = ref('全部')

    const categories = ['全部', '电子产品', '服装', '食品', '图书', '其他']

    const filteredProducts = computed(() => {
      if (selectedCategory.value === '全部') {
        return products.value
      }
      return products.value.filter(p => p.category === selectedCategory.value)
    })

    onMounted(async () => {
      try {
        const res = await api.getOnSaleProducts()
        products.value = res.data
      } catch (error) {
        console.error('Failed to load products:', error)
      }
    })

    return { products, selectedCategory, categories, filteredProducts }
  }
}
</script>

<style scoped>
.home {
  padding: 20px 0;
}
.category-filter {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}
.category-filter button {
  padding: 8px 16px;
  border: 1px solid #ddd;
  background: #fff;
  border-radius: 20px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}
.category-filter button.active {
  background: #ff6700;
  color: #fff;
  border-color: #ff6700;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
}
.empty {
  text-align: center;
  padding: 60px;
  color: #999;
  font-size: 16px;
}
</style>
