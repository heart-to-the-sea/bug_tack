<!--
  项目详情页面
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="project-detail">
    <div class="page-header">
      <el-button link @click="$router.back()">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h1>项目详情</h1>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="项目ID">
              <span class="id-cell">#{{ project.id }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusType(project.status)" size="small">
                {{ statusLabels[project.status] }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="项目名称" :span="2">
              {{ project.name }}
            </el-descriptions-item>
            <el-descriptions-item label="项目描述" :span="2">
              {{ project.description }}
            </el-descriptions-item>
            <el-descriptions-item label="负责人">
              {{ project.owner }}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ project.created_at }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <el-card class="member-card">
          <template #header>
            <div class="card-header">
              <span>项目成员 ({{ project.members?.length || 0 }})</span>
              <el-button type="primary" link @click="manageMembers">管理成员</el-button>
            </div>
          </template>
          <el-table :data="project.members" stripe>
            <el-table-column prop="name" label="成员" />
            <el-table-column prop="role" label="角色" width="120">
              <template #default="{ row }">
                <el-tag :type="getMemberRoleType(row.role)" size="small">
                  {{ memberRoleLabels[row.role] }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="email" label="邮箱" />
          </el-table>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="stats-card">
          <template #header>
            <div class="card-header">
              <span>项目统计</span>
            </div>
          </template>
          <div class="stats-list">
            <div class="stat-item">
              <span class="stat-value">{{ project.members?.length || 0 }}</span>
              <span class="stat-label">成员数量</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ project.bugCount }}</span>
              <span class="stat-label">问题数量</span>
            </div>
            <div class="stat-item">
              <span class="stat-value">{{ project.requirementCount }}</span>
              <span class="stat-label">需求数量</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue' // V1.2.0
import { useRouter } from 'vue-router' // V1.2.0
import { ArrowLeft } from '@element-plus/icons-vue' // V1.2.0

interface Member {
  // V1.2.0
  name: string // V1.2.0
  role: string // V1.2.0
  email: string // V1.2.0
} // V1.2.0

interface Project {
  // V1.2.0
  id: number // V1.2.0
  name: string // V1.2.0
  description: string // V1.2.0
  owner: string // V1.2.0
  status: string // V1.2.0
  created_at: string // V1.2.0
  members?: Member[] // V1.2.0
  bugCount: number // V1.2.0
  requirementCount: number // V1.2.0
} // V1.2.0

export default defineComponent({
  name: 'ProjectDetailView',
  components: {
    ArrowLeft,
  },
  setup() {
    const router = useRouter() // V1.2.0

    const project = ref<Project>({
      id: 1,
      name: '电商平台v2.0',
      description: '新一代电商平台系统，支持多终端统一管理',
      owner: '张三',
      status: 'active',
      created_at: '2026-01-15',
      bugCount: 23,
      requirementCount: 8,
      members: [
        { name: '张三', role: 'leader', email: 'zhangsan@example.com' },
        { name: '李四', role: 'developer', email: 'lisi@example.com' },
        { name: '王五', role: 'developer', email: 'wangwu@example.com' },
        { name: '赵六', role: 'tester', email: 'zhaoliu@example.com' },
        { name: '钱七', role: 'deployer', email: 'qianqi@example.com' },
      ],
    })

    const statusLabels: Record<string, string> = {
      active: '进行中',
      completed: '已完成',
      archived: '已归档',
    }

    const memberRoleLabels: Record<string, string> = {
      leader: '项目负责人',
      developer: '开发',
      tester: '测试',
      deployer: '部署',
      viewer: '只读',
    }

    const getStatusType = (status: string) => {
      const types: Record<string, string> = {
        active: 'primary',
        completed: 'success',
        archived: 'info',
      }
      return types[status] || 'info'
    }

    const getMemberRoleType = (role: string) => {
      const types: Record<string, string> = {
        leader: 'danger',
        developer: 'primary',
        tester: 'success',
        deployer: 'warning',
        viewer: 'info',
      }
      return types[role] || 'info'
    }

    const handleEdit = () => {
      router.push({ path: `/projects` })
    }

    const manageMembers = () => {
      router.push({ path: `/projects` })
    }

    return {
      project,
      statusLabels,
      memberRoleLabels,
      getStatusType,
      getMemberRoleType,
      handleEdit,
      manageMembers,
    }
  },
})
</script>

<style lang="less" scoped>
.project-detail {
  width: 100%;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;

  h1 {
    font-size: 20px;
    font-weight: 600;
    color: #303133;
    margin: 0;
    flex: 1;
  }
}

.info-card,
.member-card,
.stats-card {
  margin-bottom: 20px;
  border-radius: 8px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.id-cell {
  color: #409eff;
  font-weight: 500;
}

.stats-list {
  .stat-item {
    text-align: center;
    padding: 16px;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .stat-value {
      display: block;
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 4px;
    }

    .stat-label {
      font-size: 13px;
      color: #909399;
    }
  }
}
</style>
