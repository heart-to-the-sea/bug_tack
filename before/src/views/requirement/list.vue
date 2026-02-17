<!--
  需求列表页面
  V1.2 新增：需求管理功能
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="requirement-list">
    <div class="page-header">
      <h1>需求管理</h1>
      <el-button type="primary" @click="$router.push('/requirements/edit')">
        <el-icon><Plus /></el-icon>
        新建需求
      </el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索需求标题..."
        clearable
        style="width: 240px"
        @keyup.enter="handleSearch"
      />
      <el-select
        v-model="filterProject"
        placeholder="全部项目"
        clearable
        style="width: 160px"
        @change="handleSearch"
      >
        <el-option v-for="project in projects" :key="project" :label="project" :value="project" />
      </el-select>
      <el-select
        v-model="filterStatus"
        placeholder="全部状态"
        clearable
        style="width: 140px"
        @change="handleSearch"
      >
        <el-option label="待处理" value="pending" />
        <el-option label="进行中" value="in_progress" />
        <el-option label="已完成" value="completed" />
        <el-option label="已取消" value="cancelled" />
      </el-select>
      <el-select
        v-model="filterPriority"
        placeholder="全部优先级"
        clearable
        style="width: 140px"
        @change="handleSearch"
      >
        <el-option label="高" value="high" />
        <el-option label="中" value="medium" />
        <el-option label="低" value="low" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <el-table
      :data="filteredRequirements"
      stripe
      border
      style="width: 100%"
      row-key="id"
      :height="tableHeight"
      @row-click="handleRowClick"
    >
      <el-table-column prop="id" label="ID" width="80" align="center">
        <template #default="{ row }">
          <span class="id-cell">#{{ row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="title" label="需求标题" min-width="200" show-overflow-tooltip>
        <template #default="{ row }">
          <el-link type="primary" :underline="false" @click.stop="goToDetail(row.id)">
            {{ row.title }}
          </el-link>
        </template>
      </el-table-column>

      <el-table-column prop="project" label="项目" width="120" />
      <el-table-column prop="priority" label="优先级" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getPriorityType(row.priority)" size="small">
            {{ priorityLabels[row.priority] }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)" size="small">
            {{ statusLabels[row.status] }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="assignee" label="负责人" width="100" />
      <el-table-column prop="bug_count" label="关联BUG" width="100" align="center">
        <template #default="{ row }">
          <el-link type="primary" @click.stop="goToBugs(row.id)">
            {{ row.bug_count || 0 }}个
          </el-link>
        </template>
      </el-table-column>
      <el-table-column prop="created_at" label="创建时间" width="100" />

      <el-table-column label="操作" width="120" fixed="right" align="center">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click.stop="goToDetail(row.id)">
            查看
          </el-button>
          <el-button type="primary" link size="small" @click.stop="goToEdit(row.id)">
            编辑
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <span class="pagination-info">共 {{ filteredRequirements.length }} 条记录</span>
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="10"
        :total="filteredRequirements.length"
        layout="prev, pager, next"
        background
        @current-change="handlePageChange"
      />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue' // V1.2.0
import { useRouter } from 'vue-router' // V1.2.0
import { Plus } from '@element-plus/icons-vue' // V1.2.0

interface Requirement {
  // V1.2.0
  id: number // V1.2.0
  title: string // V1.2.0
  project: string // V1.2.0
  priority: string // V1.2.0
  status: string // V1.2.0
  assignee: string // V1.2.0
  bug_count: number // V1.2.0
  created_at: string // V1.2.0
  description?: string // V1.2.0
} // V1.2.0

export default defineComponent({
  name: 'RequirementListView',
  components: {
    Plus,
  },
  setup() {
    const router = useRouter() // V1.2.0
    const loading = ref(false) // V1.2.0
    const searchKeyword = ref('') // V1.2.0
    const filterProject = ref('') // V1.2.0
    const filterStatus = ref('') // V1.2.0
    const filterPriority = ref('') // V1.2.0
    const currentPage = ref(1) // V1.2.0
    const tableHeight = 'calc(100vh - 280px)' // V1.2.0

    const projects = ref([
      // V1.2.0
      '电商平台v2.0',
      '小程序优化',
      '支付系统升级',
      '后台管理系统',
    ])

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

    const requirements = ref<Requirement[]>([
      // V1.2.0 Mock数据
      {
        id: 1,
        title: '用户登录功能优化',
        project: '电商平台v2.0',
        priority: 'high',
        status: 'in_progress',
        assignee: '张三',
        bug_count: 3,
        created_at: '2026-02-10',
        description: '优化用户登录流程，增加扫码登录功能',
      },
      {
        id: 2,
        title: '商品详情页改版',
        project: '电商平台v2.0',
        priority: 'medium',
        status: 'pending',
        assignee: '李四',
        bug_count: 0,
        created_at: '2026-02-11',
        description: '重新设计商品详情页布局',
      },
      {
        id: 3,
        title: '小程序性能优化',
        project: '小程序优化',
        priority: 'high',
        status: 'in_progress',
        assignee: '王五',
        bug_count: 5,
        created_at: '2026-02-09',
        description: '优化小程序加载速度和用户体验',
      },
      {
        id: 4,
        title: '支付方式扩展',
        project: '支付系统升级',
        priority: 'high',
        status: 'completed',
        assignee: '赵六',
        bug_count: 2,
        created_at: '2026-02-08',
        description: '新增花呗、信用卡等支付方式',
      },
      {
        id: 5,
        title: '后台权限管理',
        project: '后台管理系统',
        priority: 'medium',
        status: 'pending',
        assignee: '钱七',
        bug_count: 0,
        created_at: '2026-02-12',
        description: '实现基于角色的权限控制',
      },
    ]) // V1.2.0

    const filteredRequirements = computed(() => {
      return requirements.value.filter((req) => {
        const matchKeyword = !searchKeyword.value || req.title.includes(searchKeyword.value)
        const matchProject = !filterProject.value || req.project === filterProject.value
        const matchStatus = !filterStatus.value || req.status === filterStatus.value
        const matchPriority = !filterPriority.value || req.priority === filterPriority.value
        return matchKeyword && matchProject && matchStatus && matchPriority
      })
    })

    const handleSearch = () => {
      currentPage.value = 1
    }

    const handlePageChange = (page: number) => {
      currentPage.value = page
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

    const handleRowClick = (row: Requirement) => {
      router.push({ path: `/requirements/${row.id}` })
    }

    const goToDetail = (id: number) => {
      router.push({ path: `/requirements/${id}` })
    }

    const goToEdit = (id: number) => {
      router.push({ path: `/requirements/${id}/edit` })
    }

    const goToBugs = (requirementId: number) => {
      router.push({
        path: '/bugs',
        query: { requirementId: String(requirementId) },
      })
    }

    return {
      loading,
      searchKeyword,
      filterProject,
      filterStatus,
      filterPriority,
      currentPage,
      tableHeight,
      projects,
      priorityLabels,
      statusLabels,
      requirements,
      filteredRequirements,
      getPriorityType,
      getStatusType,
      handleRowClick,
      goToDetail,
      goToEdit,
      goToBugs,
      handleSearch,
      handlePageChange,
    }
  },
})
</script>

<style lang="less" scoped>
.requirement-list {
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
    color: #303133;
  }
}

.search-bar {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  padding: 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
}

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding: 16px;
  background-color: #fff;
  border-radius: 8px;

  .pagination-info {
    color: #909399;
    font-size: 14px;
  }
}

.id-cell {
  color: #409eff;
  font-weight: 500;
}
</style>
