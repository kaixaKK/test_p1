<template>
  <div class="seckill">
    <h2>秒杀专区</h2>
    <div class="product-grid">
      <ProductCard v-for="product in seckillProducts" :key="product.id" :product="product" />
    </div>
    <div v-if="seckillProducts.length === 0" class="empty">
      暂无秒杀商品
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import api from '../../api'
import ProductCard from '../../components/ProductCard.vue'

export default {
  name: 'Seckill',
  components: { ProductCard },
  setup() {
    const seckillProducts = ref([])

    const loadSeckillProducts = async () => {
      try {
        const res = await api.getSeckillProducts()
        seckillProducts.value = res.data
      } catch (error) {
        console.error('Failed to load seckill products:', error)
      }
    }

    onMounted(() => {
      loadSeckillProducts()
    })

    return { seckillProducts }
  }
}
</script>

<style scoped>
.seckill {
  padding: 20px 0;
}
.seckill h2 {
  margin-bottom: 20px;
}
.product-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
  gap: 20px;
}
.empty {
  text-align: center;
  padding: 60px;
  background: #fff;
  border-radius: 8px;
  color: #999;
  font-size: 16px;
}
</style>
