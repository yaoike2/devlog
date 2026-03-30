<script setup lang="ts">
import { ref } from 'vue'
import axios from 'axios'

interface LogEntryResponse {
  id: number
  content: string
  tags: string
  createdAt: string
  updatedAt: string
}

interface Result<T> {
  code: number
  message: string
  data: T
}

const content = ref('')
const tags = ref('')
const loading = ref(false)
const error = ref('')
const createdLog = ref<LogEntryResponse | null>(null)

async function handleSubmit() {
  error.value = ''
  loading.value = true
  try {
    const res = await axios.post<Result<LogEntryResponse>>('/api/v1/logs', {
      content: content.value,
      tags: tags.value,
    })
    createdLog.value = res.data.data
    content.value = ''
    tags.value = ''
  } catch (e: any) {
    error.value = e.response?.data?.message ?? '请求失败，请检查后端是否启动'
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <h1>DevLog</h1>

  <form @submit.prevent="handleSubmit">
    <div class="field">
      <label for="content">内容</label>
      <textarea
        id="content"
        v-model="content"
        rows="5"
        placeholder="记录今天做了什么..."
        required
      />
    </div>

    <div class="field">
      <label for="tags">标签</label>
      <input
        id="tags"
        v-model="tags"
        type="text"
        placeholder="逗号分隔，如：工作,会议"
      />
    </div>

    <p v-if="error" class="error">{{ error }}</p>

    <button type="submit" :disabled="loading">
      {{ loading ? '提交中...' : '创建日志' }}
    </button>
  </form>

  <div v-if="createdLog" class="result">
    <h2>创建成功</h2>
    <table>
      <tbody>
        <tr>
          <th>ID</th>
          <td>{{ createdLog.id }}</td>
        </tr>
        <tr>
          <th>内容</th>
          <td>{{ createdLog.content }}</td>
        </tr>
        <tr>
          <th>标签</th>
          <td>{{ createdLog.tags || '（无）' }}</td>
        </tr>
        <tr>
          <th>创建时间</th>
          <td>{{ createdLog.createdAt }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<style scoped>
h1 {
  margin-bottom: 24px;
}

form {
  max-width: 480px;
}

.field {
  display: flex;
  flex-direction: column;
  margin-bottom: 16px;
}

label {
  font-weight: bold;
  margin-bottom: 4px;
}

textarea,
input[type='text'] {
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-family: inherit;
}

textarea {
  resize: vertical;
}

button {
  padding: 8px 20px;
  font-size: 14px;
  cursor: pointer;
}

button:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.error {
  color: #c00;
  margin-bottom: 8px;
}

.result {
  margin-top: 32px;
  max-width: 480px;
}

.result h2 {
  color: #090;
  margin-bottom: 12px;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th,
td {
  text-align: left;
  padding: 8px;
  border: 1px solid #ddd;
}

th {
  width: 100px;
  background: #f5f5f5;
}
</style>
