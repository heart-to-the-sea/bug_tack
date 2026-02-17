<!--
  需求详情页面
  V1.2 新增：需求管理功能，包含需求修改轨迹
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="requirement-detail">
    <div class="page-header">
      <el-button link @click="$router.back()">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h1>需求详情</h1>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="info-card">
          <template #header>
            <div class="card-header">
              <span>需求信息</span>
            </div>
          </template>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="需求ID">
              <span class="id-cell">#{{ requirement.id }}</span>
            </el-descriptions-item>
            <el-descriptions-item label="状态">
              <el-tag :type="getStatusType(requirement.status)" size="small">
                {{ statusLabels[requirement.status] }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="需求标题" :span="2">
              {{ requirement.title }}
            </el-descriptions-item>
            <el-descriptions-item label="所属项目">
              {{ requirement.project }}
            </el-descriptions-item>
            <el-descriptions-item label="优先级">
              <el-tag :type="getPriorityType(requirement.priority)" size="small">
                {{ priorityLabels[requirement.priority] }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="负责人">
              {{ requirement.assignee }}
            </el-descriptions-item>
            <el-descriptions-item label="创建人">
              {{ requirement.creator }}
            </el-descriptions-item>
            <el-descriptions-item label="创建时间">
              {{ requirement.created_at }}
            </el-descriptions-item>
            <el-descriptions-item label="更新时间" :span="2">
              {{ requirement.updated_at }}
            </el-descriptions-item>
            <el-descriptions-item label="需求描述" :span="2">
              {{ requirement.description }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>

        <el-card class="bug-card">
          <template #header>
            <div class="card-header">
              <span>关联的BUG</span>
              <el-button type="primary" link @click="linkBug"> 关联BUG </el-button>
            </div>
          </template>
          <el-table :data="requirement.related_bugs" stripe>
            <el-table-column prop="id" label="ID" width="80" align="center">
              <template #default="{ row }">
                <span class="id-cell">#{{ row.id }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
            <el-table-column prop="status" label="状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="getBugStatusType(row.status)" size="small">{{
                  row.status_text
                }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="80" align="center">
              <template #default="{ row }">
                <el-button type="primary" link @click="viewBug(row.id)"> 查看 </el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-empty
            v-if="requirement.related_bugs.length === 0"
            description="暂无关联BUG"
            :image-size="60"
          />
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="timeline-card">
          <template #header>
            <div class="card-header">
              <span>修改轨迹</span>
            </div>
          </template>
          <el-timeline>
            <el-timeline-item
              v-for="(item, index) in requirement.timeline"
              :key="index"
              :timestamp="item.timestamp"
              :type="item.type"
              placement="top"
            >
              <div class="timeline-content">
                <div class="timeline-title">{{ item.title }}</div>
                <div class="timeline-desc">{{ item.description }}</div>
                <div class="timeline-operator">操作人：{{ item.operator }}</div>
              </div>
            </el-timeline-item>
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue' // V1.2.0
import { useRouter } from 'vue-router' // V1.2.0
import { ArrowLeft } from '@element-plus/icons-vue' // V1.2.0

interface TimelineItem {
  // V1.2.0
  timestamp: string // V1.2.0
  type: string // V1.2.0
  title: string // V1.2.0
  description: string // V1.2.0
  operator: string // V1.2.0
} // V1.2.0

interface RelatedBug {
  // V1.2.0
  id: number // V1.2.0
  title: string // V1.2.0
  status: string // V1.2.0
  status_text: string // V1.2.0
} // V1.2.0

interface Requirement {
  // V1.2.0
  id: number // V1.2.0
  title: string // V1.2.0
  project: string // V1.2.0
  priority: string // V1.2.0
  status: string // V1.2.0
  description: string // V1.2.0
  assignee: string // V1.2.0
  creator: string // V1.2.0
  created_at: string // V1.2.0
  updated_at: string // V1.2.0
  related_bugs: RelatedBug[] // V1.2.0
  timeline: TimelineItem[] // V1.2.0
} // V1.2.0

export default defineComponent({
  name: 'RequirementDetailView',
  components: {
    ArrowLeft,
  },
  setup() {
    const router = useRouter() // V1.2.0

    const requirement = ref<Requirement>({
      id: 1,
      title: '用户登录功能优化',
      project: '电商平台v2.0',
      priority: 'high',
      status: 'in_progress',
      description: '优化用户登录流程，增加扫码登录功能，支持第三方账号绑定',
      assignee: '张三',
      creator: '李四',
      created_at: '2026-02-10 10:00:00',
      updated_at: '2026-02-12 15:30:00',
      related_bugs: [
        {
          id: 156,
          title: '用户登录接口返回500错误',
          status: 'processing',
          status_text: '处理中',
        },
        {
          id: 155,
          title: '订单列表分页加载缓慢',
          status: 'pending',
          status_text: '待分配',
        },
        {
          id: 151,
          title: '用户注册短信验证码发送失败',
          status: 'processing',
          status_text: '处理中',
        },
      ],
      timeline: [
        {
          timestamp: '2026-02-12 15:30:00',
          type: 'primary',
          title: '需求更新',
          description: '修改了需求描述，增加了第三方账号绑定功能',
          operator: '张三',
        },
        {
          timestamp: '2026-02-11 14:00:00',
          type: 'success',
          title: '开始开发',
          description: '项目经理安排张三负责开发',
          operator: '李四',
        },
        {
          timestamp: '2026-02-10 10:00:00',
          type: 'primary',
          title: '需求创建',
          description: '创建了用户登录功能优化需求',
          operator: '李四',
        },
      ],
    })

    const priorityLabels: Record<string, string> = {
      // V1.2.0
      high: '高',
      medium: '中',
      low: '低',
    }

    const statusLabels: Record<string, string> = {
      // V1.2.0
      pending: '待处理',
      in_progress: '进行中',
      completed: '已完成',
      cancelled: '已取消',
    }

    const getPriorityType = (priority: string) => {
      const types: Record<string, string> = {
        high: 'danger',
        medium: 'warning',
        low: 'success',
      }
      return types[priority] || 'info'
    }

    const getStatusType = (status: string) => {
      const types: Record<string, string> = {
        pending: 'warning',
        in_progress: 'primary',
        completed: 'success',
        cancelled: 'info',
      }
      return types[status] || 'info'
    }

    const getBugStatusType = (status: string) => {
      const types: Record<string, string> = {
        pending: 'warning',
        processing: 'primary',
        testing: 'info',
        completed: 'success',
        rejected: 'danger',
      }
      return types[status] || 'info'
    }

    const handleEdit = () => {
      router.push({ path: `/requirements/${requirement.value.id}/edit` })
    }

    const linkBug = () => {
      router.push({
        path: '/bugs',
        query: { requirementId: String(requirement.value.id) },
      })
    }

    const viewBug = (bugId: number) => {
      router.push({ path: `/bugs/${bugId}` })
    }

    return {
      requirement,
      priorityLabels,
      statusLabels,
      getPriorityType,
      getStatusType,
      getBugStatusType,
      handleEdit,
      linkBug,
      viewBug,
    }
  },
})
</script>

<style lang="less" scoped>
.requirement-detail {
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
.bug-card,
.timeline-card {
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

.timeline-content {
  .timeline-title {
    font-weight: 500;
    color: #303133;
    margin-bottom: 4px;
  }

  .timeline-desc {
    color: #606266;
    font-size: 14px;
    margin-bottom: 4px;
  }

  .timeline-operator {
    color: #909399;
    font-size: 12px;
  }
}
</style>
