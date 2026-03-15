<template>
  <div class="merchant-messages">
    <h2>留言管理</h2>
    <div class="message-list">
      <div v-for="msg in messages" :key="msg.id" class="message-item">
        <div class="message-header">
          <span class="username">{{ msg.username }}</span>
          <span class="time">{{ formatDate(msg.createdAt) }}</span>
        </div>
        <div class="message-content">{{ msg.content }}</div>
        <div class="message-reply" v-if="msg.reply">
          <strong>已回复:</strong> {{ msg.reply }}
        </div>
        <div class="reply-form" v-else>
          <input v-model="msg.replyContent" placeholder="回复内容..." />
          <button @click="replyMessage(msg.id, msg.replyContent)">回复</button>
        </div>
      </div>
    </div>
    <div v-if="messages.length === 0" class="empty">暂无留言</div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import api from '../../api'

export default {
  name: 'MerchantMessages',
  setup() {
    const userStore = useUserStore()
    const messages = ref([])

    const loadMessages = async () => {
      try {
        const res = await api.getMessagesByMerchant(userStore.user.id)
        // 为每条消息添加独立的回复内容字段
        messages.value = res.data.map(msg => ({
          ...msg,
          replyContent: ''
        }))
      } catch (error) {
        console.error('Failed to load messages:', error)
      }
    }

    const replyMessage = async (messageId, content) => {
      if (!content || !content.trim()) return
      try {
        await api.replyMessage(messageId, content)
        loadMessages()
      } catch (error) {
        alert('回复失败')
      }
    }

    const formatDate = (date) => {
      if (!date) return ''
      return new Date(date).toLocaleString('zh-CN')
    }

    onMounted(() => {
      loadMessages()
    })

    return { messages, replyMessage, formatDate }
  }
}
</script>

<style scoped>
.merchant-messages {
  padding: 20px 0;
}
.merchant-messages h2 {
  margin-bottom: 20px;
}
.message-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}
.message-item {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
}
.message-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
.username {
  font-weight: bold;
}
.time {
  color: #999;
  font-size: 12px;
}
.message-content {
  color: #666;
  margin-bottom: 10px;
  line-height: 1.5;
}
.message-reply {
  background: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  color: #4caf50;
}
.reply-form {
  display: flex;
  gap: 10px;
}
.reply-form input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}
.reply-form button {
  padding: 8px 20px;
  background: #4caf50;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.empty {
  text-align: center;
  padding: 60px;
  background: #fff;
  border-radius: 8px;
  color: #999;
}
</style>
