<template>
  <div class="project-list">
    <div class="page-header">
      <h1>项目管理</h1>
      <button class="btn-primary" @click="showCreateModal = true">
        ➕ 新建项目
      </button>
    </div>

    <div class="project-grid">
      <div class="project-card" v-for="project in projects" :key="project.id">
        <div class="project-header">
          <h3>{{ project.name }}</h3>
          <span class="project-status" :class="'status-' + project.status">
            {{ statusLabels[project.status] }}
          </span>
        </div>
        <p class="project-desc">{{ project.description }}</p>
        <div class="project-meta">
          <div class="meta-item">
            <span class="meta-label">负责人</span>
            <span class="meta-value">{{ project.owner }}</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">成员数</span>
            <span class="meta-value">{{ project.members }}人</span>
          </div>
          <div class="meta-item">
            <span class="meta-label">问题数</span>
            <span class="meta-value">{{ project.bugCount }}个</span>
          </div>
        </div>
        <div class="project-actions">
          <button class="action-btn" @click="viewProject(project)">
            查看详情
          </button>
          <button class="action-btn" @click="editProject(project)">编辑</button>
        </div>
      </div>
    </div>

    <div
      class="modal-overlay"
      v-if="showCreateModal"
      @click.self="showCreateModal = false"
    >
      <div class="modal-content">
        <div class="modal-header">
          <h2>新建项目</h2>
          <button class="modal-close" @click="showCreateModal = false">
            ×
          </button>
        </div>
        <div class="modal-body">
          <div class="form-item">
            <label>项目名称 <span class="required">*</span></label>
            <input
              type="text"
              class="form-input"
              v-model="newProject.name"
              placeholder="请输入项目名称"
            />
          </div>
          <div class="form-item">
            <label>项目描述</label>
            <textarea
              class="form-textarea"
              v-model="newProject.description"
              placeholder="请输入项目描述"
            ></textarea>
          </div>
          <div class="form-item">
            <label>项目负责人</label>
            <select class="form-select" v-model="newProject.owner">
              <option value="">请选择负责人</option>
              <option value="张三">张三</option>
              <option value="李四">李四</option>
              <option value="王五">王五</option>
            </select>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showCreateModal = false">
            取消
          </button>
          <button class="btn-primary" @click="createProject">创建</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";

interface Project {
  id: number;
  name: string;
  description: string;
  owner: string;
  members: number;
  bugCount: number;
  status: string;
}

export default defineComponent({
  name: "ProjectList",
  setup() {
    const showCreateModal = ref(false);
    const statusLabels: Record<string, string> = {
      active: "进行中",
      completed: "已完成",
      archived: "已归档",
    };

    const projects = ref<Project[]>([
      {
        id: 1,
        name: "电商平台v2.0",
        description: "全新升级的电商平台，优化用户体验和性能",
        owner: "张三",
        members: 8,
        bugCount: 45,
        status: "active",
      },
      {
        id: 2,
        name: "小程序优化",
        description: "微信小程序性能优化和新功能开发",
        owner: "李四",
        members: 4,
        bugCount: 23,
        status: "active",
      },
      {
        id: 3,
        name: "支付系统升级",
        description: "支付系统安全性升级和多渠道支付支持",
        owner: "王五",
        members: 3,
        bugCount: 18,
        status: "active",
      },
      {
        id: 4,
        name: "后台管理系统",
        description: "企业内部管理系统重构",
        owner: "张三",
        members: 5,
        bugCount: 12,
        status: "completed",
      },
    ]);

    const newProject = ref({
      name: "",
      description: "",
      owner: "",
    });

    const viewProject = (project: Project) => {
      console.log("查看项目:", project.id);
    };

    const editProject = (project: Project) => {
      console.log("编辑项目:", project.id);
    };

    const createProject = () => {
      if (!newProject.value.name) {
        alert("请填写项目名称");
        return;
      }
      console.log("创建项目:", newProject.value);
      showCreateModal.value = false;
      newProject.value = { name: "", description: "", owner: "" };
    };

    return {
      showCreateModal,
      statusLabels,
      projects,
      newProject,
      viewProject,
      editProject,
      createProject,
    };
  },
});
</script>

<style lang="less" scoped>
.project-list {
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
}

.btn-primary {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background-color: #00c4c4;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;

  &:hover {
    background-color: #00b0b0;
  }
}

.project-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
  gap: 20px;
}

.project-card {
  background-color: #fff;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  transition: box-shadow 0.2s;

  &:hover {
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  }

  .project-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 12px;

    h3 {
      font-size: 16px;
      font-weight: 600;
      color: #333;
    }

    .project-status {
      padding: 4px 10px;
      border-radius: 4px;
      font-size: 12px;

      &.status-active {
        background-color: #e6f7ff;
        color: #1890ff;
      }

      &.status-completed {
        background-color: #f6ffed;
        color: #52c41a;
      }

      &.status-archived {
        background-color: #f5f5f5;
        color: #999;
      }
    }
  }

  .project-desc {
    font-size: 14px;
    color: #666;
    margin-bottom: 16px;
    line-height: 1.5;
  }

  .project-meta {
    display: flex;
    gap: 24px;
    padding: 12px 0;
    border-top: 1px solid #f0f0f0;
    border-bottom: 1px solid #f0f0f0;
    margin-bottom: 16px;

    .meta-item {
      display: flex;
      flex-direction: column;

      .meta-label {
        font-size: 12px;
        color: #999;
        margin-bottom: 4px;
      }

      .meta-value {
        font-size: 14px;
        color: #333;
        font-weight: 500;
      }
    }
  }

  .project-actions {
    display: flex;
    gap: 12px;

    .action-btn {
      flex: 1;
      padding: 8px 16px;
      border: 1px solid #e8e8e8;
      background-color: #fff;
      border-radius: 6px;
      font-size: 14px;
      color: #666;
      cursor: pointer;
      transition: all 0.2s;

      &:hover {
        border-color: #00c4c4;
        color: #00c4c4;
      }
    }
  }
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  width: 90%;
  max-width: 500px;
  background-color: #fff;
  border-radius: 12px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;

  h2 {
    font-size: 18px;
    font-weight: 600;
  }

  .modal-close {
    width: 32px;
    height: 32px;
    border: none;
    background: none;
    font-size: 24px;
    cursor: pointer;
    color: #999;

    &:hover {
      color: #333;
    }
  }
}

.modal-body {
  padding: 24px;
}

.form-item {
  margin-bottom: 20px;

  label {
    display: block;
    margin-bottom: 8px;
    font-weight: 500;
    color: #333;

    .required {
      color: #ff4d4f;
    }
  }

  .form-input,
  .form-select,
  .form-textarea {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #e8e8e8;
    border-radius: 6px;
    font-size: 14px;
    outline: none;

    &:focus {
      border-color: #00c4c4;
    }
  }

  .form-textarea {
    min-height: 80px;
    resize: vertical;
  }
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px 24px;
  border-top: 1px solid #f0f0f0;
}

.btn-secondary {
  padding: 10px 20px;
  background-color: #fff;
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;

  &:hover {
    border-color: #00c4c4;
    color: #00c4c4;
  }
}
</style>
