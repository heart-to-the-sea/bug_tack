<template>
  <div class="dashboard">
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon" style="background-color: rgba(0, 196, 196, 0.1); color: #00c4c4">
          📝
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.total }}</span>
          <span class="stat-label">全部问题</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background-color: rgba(255, 153, 0, 0.1); color: #ff9900">
          ⏳
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.pending }}</span>
          <span class="stat-label">待分配</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background-color: rgba(24, 144, 255, 0.1); color: #1890ff">
          🔧
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.processing }}</span>
          <span class="stat-label">处理中</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon" style="background-color: rgba(82, 196, 26, 0.1); color: #52c41a">
          ✅
        </div>
        <div class="stat-content">
          <span class="stat-value">{{ stats.completed }}</span>
          <span class="stat-label">已修复</span>
        </div>
      </div>
    </div>

    <div class="dashboard-content">
      <div class="section-card">
        <div class="section-header">
          <h2>待处理问题</h2>
          <router-link to="/bugs" class="view-all">查看全部 →</router-link>
        </div>
        <div class="issue-list">
          <div class="issue-item" v-for="issue in recentIssues" :key="issue.id">
            <div class="issue-info">
              <span class="issue-id">#{{ issue.id }}</span>
              <span class="issue-title">{{ issue.title }}</span>
            </div>
            <div class="issue-meta">
              <span class="issue-project">{{ issue.project }}</span>
              <span class="issue-priority" :class="'priority-' + issue.priority">
                {{ priorityLabels[issue.priority] }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div class="section-card">
        <div class="section-header">
          <h2>我的待办</h2>
          <router-link to="/todo" class="view-all">查看全部 →</router-link>
        </div>
        <div class="todo-list">
          <div class="todo-item" v-for="todo in myTodos" :key="todo.id">
            <input type="checkbox" class="todo-checkbox" />
            <span class="todo-title">{{ todo.title }}</span>
            <span class="todo-deadline" :class="{ overdue: isOverdue(todo.deadline) }">
              {{ formatDate(todo.deadline) }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="section-card full-width">
      <div class="section-header">
        <h2>快速操作</h2>
      </div>
      <div class="quick-actions">
        <button class="action-btn" @click="showCreateBug = true">
          <span class="action-icon">➕</span>
          <span class="action-text">新建问题</span>
        </button>
        <button class="action-btn">
          <span class="action-icon">📊</span>
          <span class="action-text">报表统计</span>
        </button>
        <button class="action-btn">
          <span class="action-icon">👥</span>
          <span class="action-text">人员管理</span>
        </button>
        <button class="action-btn">
          <span class="action-icon">🔄</span>
          <span class="action-text">问题交接</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'

interface Issue {
  id: number
  title: string
  project: string
  priority: string
}

interface Todo {
  id: number
  title: string
  deadline: string
}

export default defineComponent({
  name: 'DashboardView',
  setup() {
    const stats = ref({
      total: 156,
      pending: 23,
      processing: 45,
      completed: 88,
    })

    const priorityLabels: Record<string, string> = {
      high: '高',
      medium: '中',
      low: '低',
    }

    const recentIssues = ref<Issue[]>([
      {
        id: 156,
        title: '用户登录接口返回500错误',
        project: '电商平台v2.0',
        priority: 'high',
      },
      {
        id: 155,
        title: '订单列表分页加载缓慢',
        project: '电商平台v2.0',
        priority: 'medium',
      },
      {
        id: 154,
        title: '购物车数量更新异常',
        project: '小程序优化',
        priority: 'high',
      },
      {
        id: 153,
        title: '支付回调地址配置错误',
        project: '支付系统升级',
        priority: 'low',
      },
      {
        id: 152,
        title: '商品详情页图片加载失败',
        project: '电商平台v2.0',
        priority: 'medium',
      },
    ])

    const myTodos = ref<Todo[]>([
      { id: 1, title: '修复用户登录接口500错误', deadline: '2026-02-12' },
      { id: 2, title: '优化订单列表查询SQL', deadline: '2026-02-13' },
      { id: 3, title: '更新支付回调配置', deadline: '2026-02-15' },
      { id: 4, title: '添加单元测试用例', deadline: '2026-02-10' },
    ])

    const formatDate = (dateStr: string) => {
      const date = new Date(dateStr)
      return `${date.getMonth() + 1}/${date.getDate()}`
    }

    const isOverdue = (dateStr: string) => {
      return new Date(dateStr) < new Date()
    }

    const showCreateBug = ref(false)

    return {
      stats,
      priorityLabels,
      recentIssues,
      myTodos,
      formatDate,
      isOverdue,
      showCreateBug,
    }
  },
})
</script>

<style lang="less" scoped>
.dashboard {
  width: 100%;
  margin: 0 auto;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  .stat-icon {
    width: 48px;
    height: 48px;
    border-radius: 12px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 24px;
  }

  .stat-content {
    display: flex;
    flex-direction: column;

    .stat-value {
      font-size: 28px;
      font-weight: 600;
      color: #333;
    }

    .stat-label {
      font-size: 14px;
      color: #999;
      margin-top: 4px;
    }
  }
}

.dashboard-content {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.section-card {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  &.full-width {
    grid-column: 1 / -1;
  }

  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;
    padding-bottom: 12px;
    border-bottom: 1px solid #f0f0f0;

    h2 {
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }

    .view-all {
      font-size: 13px;
      color: #00c4c4;
      cursor: pointer;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}

.issue-list {
  .issue-item {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .issue-info {
      display: flex;
      align-items: center;
      gap: 10px;

      .issue-id {
        color: #999;
        font-size: 13px;
      }

      .issue-title {
        font-size: 14px;
        color: #333;
      }
    }

    .issue-meta {
      display: flex;
      align-items: center;
      gap: 12px;

      .issue-project {
        font-size: 12px;
        color: #999;
      }

      .issue-priority {
        padding: 2px 8px;
        border-radius: 4px;
        font-size: 12px;

        &.priority-high {
          background-color: #fff2f0;
          color: #ff4d4f;
        }

        &.priority-medium {
          background-color: #fff7e6;
          color: #d46b08;
        }

        &.priority-low {
          background-color: #f6ffed;
          color: #52c41a;
        }
      }
    }
  }
}

.todo-list {
  .todo-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .todo-checkbox {
      width: 18px;
      height: 18px;
      cursor: pointer;
    }

    .todo-title {
      flex: 1;
      font-size: 14px;
      color: #333;
    }

    .todo-deadline {
      font-size: 12px;
      color: #999;

      &.overdue {
        color: #ff4d4f;
      }
    }
  }
}

.quick-actions {
  display: flex;
  gap: 16px;

  .action-btn {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 8px;
    padding: 20px;
    background-color: #fafafa;
    border: 1px solid #f0f0f0;
    border-radius: 8px;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      background-color: #f0f9ff;
      border-color: #00c4c4;
    }

    .action-icon {
      font-size: 24px;
    }

    .action-text {
      font-size: 14px;
      color: #333;
    }
  }
}
</style>
