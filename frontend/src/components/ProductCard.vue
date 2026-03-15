<template>
  <div class="product-card" @click="goToDetail">
    <div class="product-image">
      <img :src="product.imageUrl || 'https://via.placeholder.com/200'" :alt="product.name" />
      <span v-if="product.isSeckill" class="seckill-tag">秒杀</span>
    </div>
    <div class="product-info">
      <h3 class="product-name">{{ product.name }}</h3>
      <p class="product-desc">{{ product.description }}</p>
      <div class="product-price">
        <span class="current-price">¥{{ seckillPrice }}</span>
        <span v-if="product.isSeckill" class="original-price">¥{{ product.price }}</span>
      </div>
      <div class="product-stock">库存: {{ product.stock }}</div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

export default {
  name: 'ProductCard',
  props: {
    product: Object
  },
  setup(props) {
    const router = useRouter()

    const seckillPrice = computed(() => {
      if (props.product.isSeckill) {
        return props.product.seckillPrice
      }
      return props.product.price
    })

    const goToDetail = () => {
      router.push(`/product/${props.product.id}`)
    }

    return { seckillPrice, goToDetail }
  }
}
</script>

<style scoped>
.product-card {
  background: #fff;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}
.product-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
.product-image {
  position: relative;
  width: 100%;
  height: 200px;
  background: #f5f5f5;
}
.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
.seckill-tag {
  position: absolute;
  top: 10px;
  left: 10px;
  padding: 4px 8px;
  background: #f44336;
  color: #fff;
  font-size: 12px;
  border-radius: 4px;
}
.product-info {
  padding: 15px;
}
.product-name {
  font-size: 16px;
  font-weight: 500;
  color: #333;
  margin-bottom: 8px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.product-desc {
  font-size: 12px;
  color: #999;
  margin-bottom: 10px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.product-price {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 8px;
}
.current-price {
  font-size: 20px;
  font-weight: bold;
  color: #f44336;
}
.original-price {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
}
.product-stock {
  font-size: 12px;
  color: #666;
}
</style>
