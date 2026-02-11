<template>
  <div class="todo-list-page">
    <div class="page-header">
      <h1>我的待办</h1>
      <div class="header-actions">
        <select class="filter-select" v-model="filterPriority">
          <option value="">全部优先级</option>
          <option value="high">高</option>
          <option value="medium">中</option>
          <option value="low">低</option>
        </select>
        <select class="filter-select" v-model="filterStatus">
          <option value="">全部状态</option>
          <option value="pending">未开始</option>
          <option value="processing">处理中</option>
          <option value="completed">已完成</option>
        </select>
      </div>
    </div>

    <div class="todo-stats">
      <div class="stat-item">
        <span class="stat-value">{{ pendingCount }}</span>
        <span class="stat-label">未开始</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ processingCount }}</span>
        <span class="stat-label">处理中</span>
      </div>
      <div class="stat-item">
        <span class="stat-value">{{ completedCount }}</span>
        <span class="stat-label">已完成</span>
      </div>
      <div class="stat-item highlight">
        <span class="stat-value">{{ completionRate }}%</span>
        <span class="stat-label">完成率</span>
      </div>
    </div>

    <div class="todo-items">
      <div
        class="todo-item"
        v-for="todo in filteredTodos"
        :key="todo.id"
        :class="{ completed: todo.status === 'completed' }"
      >
        <div class="todo-checkbox">
          <input
            type="checkbox"
            :checked="todo.status === 'completed'"
            @change="toggleTodo(todo)"
          />
        </div>
        <div class="todo-content">
          <div class="todo-header">
            <span class="todo-bug-id">#{{ todo.bugId }}</span>
            <span class="todo-title">{{ todo.title }}</span>
            <span class="priority-tag" :class="'priority-' + todo.priority">
              {{ priorityLabels[todo.priority] }}
            </span>
          </div>
          <div class="todo-meta">
            <span class="meta-item"> 📁 {{ todo.project }} </span>
            <span
              class="meta-item"
              :class="{ overdue: isOverdue(todo.deadline) }"
            >
              ⏰ {{ formatDate(todo.deadline) }}
            </span>
            <span class="meta-item"> 👤 {{ todo.assignee }} </span>
          </div>
        </div>
        <div class="todo-actions">
          <select
            class="status-select"
            :value="todo.status"
            @change="updateStatus(todo, $event)"
          >
            <option value="pending">未开始</option>
            <option value="processing">处理中</option>
            <option value="completed">已完成</option>
          </select>
          <button class="action-btn" @click="viewDetail(todo)">详情</button>
        </div>
      </div>
    </div>

    <div class="empty-state" v-if="filteredTodos.length === 0">
      <div class="empty-icon">📋</div>
      <div class="empty-text">暂无待办事项</div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from "vue";

interface Todo {
  id: number;
  bugId: number;
  title: string;
  project: string;
  priority: string;
  status: string;
  deadline: string;
  assignee: string;
}

export default defineComponent({
  name: "TodoList",
  setup() {
    const filterPriority = ref("");
    const filterStatus = ref("");

    const priorityLabels: Record<string, string> = {
      high: "高",
      medium: "中",
      low: "低",
    };

    const todos = ref<Todo[]>([
      {
        id: 1,
        bugId: 156,
        title: "修复用户登录接口500错误",
        project: "电商平台v2.0",
        priority: "high",
        status: "processing",
        deadline: "2026-02-12",
        assignee: "张三",
      },
      {
        id: 2,
        bugId: 155,
        title: "优化订单列表查询SQL",
        project: "电商平台v2.0",
        priority: "medium",
        status: "pending",
        deadline: "2026-02-13",
        assignee: "张三",
      },
      {
        id: 3,
        bugId: 154,
        title: "更新支付回调配置",
        project: "支付系统升级",
        priority: "low",
        status: "pending",
        deadline: "2026-02-15",
        assignee: "张三",
      },
      {
        id: 4,
        bugId: 152,
        title: "添加单元测试用例",
        project: "电商平台v2.0",
        priority: "medium",
        status: "completed",
        deadline: "2026-02-10",
        assignee: "张三",
      },
      {
        id: 5,
        bugId: 151,
        title: "用户注册短信验证码发送失败",
        project: "电商平台v2.0",
        priority: "high",
        status: "pending",
        deadline: "2026-02-11",
        assignee: "张三",
      },
    ]);

    const filteredTodos = computed(() => {
      return todos.value.filter((todo) => {
        const matchPriority =
          !filterPriority.value || todo.priority === filterPriority.value;
        const matchStatus =
          !filterStatus.value || todo.status === filterStatus.value;
        return matchPriority && matchStatus;
      });
    });

    const pendingCount = computed(
      () => todos.value.filter((t) => t.status === "pending").length
    );
    const processingCount = computed(
      () => todos.value.filter((t) => t.status === "processing").length
    );
    const completedCount = computed(
      () => todos.value.filter((t) => t.status === "completed").length
    );
    const completionRate = computed(() => {
      return Math.round((completedCount.value / todos.value.length) * 100);
    });

    const formatDate = (dateStr: string) => {
      const date = new Date(dateStr);
      return `${date.getMonth() + 1}/${date.getDate()}`;
    };

    const isOverdue = (dateStr: string) => {
      return new Date(dateStr) < new Date();
    };

    const toggleTodo = (todo: Todo) => {
      todo.status = todo.status === "completed" ? "pending" : "completed";
    };

    const updateStatus = (todo: Todo, event: Event) => {
      const target = event.target as HTMLSelectElement;
      todo.status = target.value;
    };

    const viewDetail = (todo: Todo) => {
      console.log("查看详情:", todo.id);
    };

    return {
      filterPriority,
      filterStatus,
      priorityLabels,
      todos,
      filteredTodos,
      pendingCount,
      processingCount,
      completedCount,
      completionRate,
      formatDate,
      isOverdue,
      toggleTodo,
      updateStatus,
      viewDetail,
    };
  },
});
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

    .filter-select {
      padding: 8px 12px;
      border: 1px solid #e8e8e8;
      border-radius: 6px;
      font-size: 14px;
      background-color: #fff;
      cursor: pointer;
    }
  }
}

.todo-stats {
  display: flex;
  gap: 20px;
  margin-bottom: 24px;

  .stat-item {
    flex: 1;
    background-color: #fff;
    border-radius: 8px;
    padding: 16px 20px;
    text-align: center;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

    .stat-value {
      display: block;
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
      background-color: #00c4c4;

      .stat-value {
        color: #fff;
      }

      .stat-label {
        color: rgba(255, 255, 255, 0.8);
      }
    }
  }
}

.todo-items {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.todo-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
  transition: background-color 0.2s;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background-color: #fafafa;
  }

  &.completed {
    opacity: 0.6;

    .todo-title {
      text-decoration: line-through;
    }
  }

  .todo-checkbox {
    padding-top: 4px;

    input[type="checkbox"] {
      width: 18px;
      height: 18px;
      cursor: pointer;
    }
  }

  .todo-content {
    flex: 1;

    .todo-header {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 8px;

      .todo-bug-id {
        color: #00c4c4;
        font-weight: 500;
      }

      .todo-title {
        font-size: 14px;
        color: #333;
      }
    }

    .todo-meta {
      display: flex;
      gap: 16px;

      .meta-item {
        font-size: 12px;
        color: #999;

        &.overdue {
          color: #ff4d4f;
        }
      }
    }
  }

  .todo-actions {
    display: flex;
    align-items: center;
    gap: 12px;

    .status-select {
      padding: 6px 10px;
      border: 1px solid #e8e8e8;
      border-radius: 4px;
      font-size: 12px;
      background-color: #fff;
      cursor: pointer;
    }

    .action-btn {
      padding: 6px 12px;
      border: 1px solid #e8e8e8;
      background-color: #fff;
      border-radius: 4px;
      font-size: 12px;
      cursor: pointer;

      &:hover {
        border-color: #00c4c4;
        color: #00c4c4;
      }
    }
  }
}

.priority-tag {
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

.empty-state {
  text-align: center;
  padding: 60px 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  .empty-icon {
    font-size: 48px;
    margin-bottom: 16px;
  }

  .empty-text {
    font-size: 14px;
    color: #999;
  }
}
</style>
