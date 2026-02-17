<!--
  待办列表页面
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="todo-list-page">
    <div class="page-header">
      <h1>我的待办</h1>
      <div class="header-actions">
        <el-select v-model="filterPriority" placeholder="全部优先级" clearable style="width: 140px">
          <el-option label="全部优先级" value="" />
          <el-option label="高" value="high" />
          <el-option label="中" value="medium" />
          <el-option label="低" value="low" />
        </el-select>
        <el-select v-model="filterStatus" placeholder="全部状态" clearable style="width: 140px">
          <el-option label="全部状态" value="" />
          <el-option label="未开始" value="pending" />
          <el-option label="处理中" value="processing" />
          <el-option label="已完成" value="completed" />
        </el-select>
      </div>
    </div>

    <el-row :gutter="20" class="todo-stats">
      <el-col :span="6">
        <el-card shadow="never" class="stat-item">
          <div class="stat-value">{{ pendingCount }}</div>
          <div class="stat-label">未开始</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-item">
          <div class="stat-value">{{ processingCount }}</div>
          <div class="stat-label">处理中</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-item">
          <div class="stat-value">{{ completedCount }}</div>
          <div class="stat-label">已完成</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="never" class="stat-item highlight">
          <div class="stat-value">{{ completionRate }}%</div>
          <div class="stat-label">完成率</div>
        </el-card>
      </el-col>
    </el-row>

    <el-table :data="filteredTodos" border stripe :height="tableHeight">
      <el-table-column label="状态" width="60" align="center">
        <template #default="{ row }">
          <el-checkbox :model-value="row.status === 'completed'" @change="toggleTodo(row)" />
        </template>
      </el-table-column>
      <el-table-column label="问题ID" width="80">
        <template #default="{ row }">
          <span class="todo-bug-id">#{{ row.bugId }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
      <el-table-column prop="project" label="项目" width="120" />
      <el-table-column prop="priority" label="优先级" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="getPriorityType(row.priority)" size="small">
            {{ priorityLabels[row.priority] }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-select
            :model-value="row.status"
            size="small"
            style="width: 90px"
            @change="(val: string) => updateStatus(row, val)"
          >
            <el-option label="未开始" value="pending" />
            <el-option label="处理中" value="processing" />
            <el-option label="已完成" value="completed" />
          </el-select>
        </template>
      </el-table-column>
      <el-table-column label="截止日期" width="100">
        <template #default="{ row }">
          <span :class="{ overdue: isOverdue(row.deadline) }">
            {{ formatDate(row.deadline) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column prop="assignee" label="负责人" width="80" />
      <el-table-column label="操作" width="80" align="center">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="viewDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-empty v-if="filteredTodos.length === 0" description="暂无待办事项" />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue' // V1.2.0

interface Todo {
  // V1.2.0
  id: number // V1.2.0
  bugId: number // V1.2.0
  title: string // V1.2.0
  project: string // V1.2.0
  priority: string // V1.2.0
  status: string // V1.2.0
  deadline: string // V1.2.0
  assignee: string // V1.2.0
} // V1.2.0

export default defineComponent({
  name: 'TodoList',
  setup() {
    const filterPriority = ref('') // V1.2.0
    const filterStatus = ref('') // V1.2.0
    const tableHeight = 'calc(100vh - 320px)' // V1.2.0

    const priorityLabels: Record<string, string> = {
      // V1.2.0
      high: '高', // V1.2.0
      medium: '中', // V1.2.0
      low: '低', // V1.2.0
    }

    const todos = ref<Todo[]>([
      // V1.2.0
      {
        id: 1,
        bugId: 156,
        title: '修复用户登录接口500错误',
        project: '电商平台v2.0',
        priority: 'high',
        status: 'processing',
        deadline: '2026-02-12',
        assignee: '张三',
      },
      {
        id: 2,
        bugId: 155,
        title: '优化订单列表查询SQL',
        project: '电商平台v2.0',
        priority: 'medium',
        status: 'pending',
        deadline: '2026-02-13',
        assignee: '张三',
      },
      {
        id: 3,
        bugId: 154,
        title: '更新支付回调配置',
        project: '支付系统升级',
        priority: 'low',
        status: 'pending',
        deadline: '2026-02-15',
        assignee: '张三',
      },
      {
        id: 4,
        bugId: 152,
        title: '添加单元测试用例',
        project: '电商平台v2.0',
        priority: 'medium',
        status: 'completed',
        deadline: '2026-02-10',
        assignee: '张三',
      },
      {
        id: 5,
        bugId: 151,
        title: '用户注册短信验证码发送失败',
        project: '电商平台v2.0',
        priority: 'high',
        status: 'pending',
        deadline: '2026-02-11',
        assignee: '张三',
      },
    ]) // V1.2.0

    const filteredTodos = computed(() => {
      return todos.value.filter((todo) => {
        const matchPriority = !filterPriority.value || todo.priority === filterPriority.value
        const matchStatus = !filterStatus.value || todo.status === filterStatus.value
        return matchPriority && matchStatus
      })
    })

    const pendingCount = computed(() => todos.value.filter((t) => t.status === 'pending').length)
    const processingCount = computed(
      () => todos.value.filter((t) => t.status === 'processing').length
    )
    const completedCount = computed(
      () => todos.value.filter((t) => t.status === 'completed').length
    )
    const completionRate = computed(() => {
      return todos.value.length > 0
        ? Math.round((completedCount.value / todos.value.length) * 100)
        : 0
    })

    const formatDate = (dateStr: string) => {
      const date = new Date(dateStr)
      return `${date.getMonth() + 1}/${date.getDate()}`
    }

    const isOverdue = (dateStr: string) => {
      return new Date(dateStr) < new Date()
    }

    const getPriorityType = (priority: string) => {
      const types: Record<string, string> = {
        high: 'danger',
        medium: 'warning',
        low: 'success',
      }
      return types[priority] || 'info'
    }

    const toggleTodo = (todo: Todo) => {
      todo.status = todo.status === 'completed' ? 'pending' : 'completed'
    }

    const updateStatus = (todo: Todo, status: string) => {
      todo.status = status
    }

    const viewDetail = (todo: Todo) => {
      console.log('查看详情:', todo.id)
    }

    return {
      filterPriority,
      filterStatus,
      tableHeight,
      priorityLabels,
      todos,
      filteredTodos,
      pendingCount,
      processingCount,
      completedCount,
      completionRate,
      formatDate,
      isOverdue,
      getPriorityType,
      toggleTodo,
      updateStatus,
      viewDetail,
    }
  },
})
</script>

<style lang="less" scoped>
.todo-list-page {
  width: 100%;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  h1 {
    font-size: 20px;
    font-weight: 600;
    color: #333;
  }

  .header-actions {
    display: flex;
    gap: 12px;
  }
}

.todo-stats {
  margin-bottom: 24px;

  .stat-item {
    text-align: center;

    .stat-value {
      font-size: 24px;
      font-weight: 600;
      color: #333;
      margin-bottom: 4px;
    }

    .stat-label {
      font-size: 13px;
      color: #999;
    }

    &.highlight {
      :deep(.el-card__body) {
        background-color: #00c4c4;
      }

      .stat-value,
      .stat-label {
        color: #fff;
      }
    }
  }
}

.todo-bug-id {
  color: #00c4c4;
  font-weight: 500;
}

.overdue {
  color: #ff4d4f;
}
</style>
