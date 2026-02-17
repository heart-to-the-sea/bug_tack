<!--
  问题列表页面
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="bug-list">
    <div class="page-header">
      <h1>问题管理</h1>
      <el-button type="primary" @click="$router.push('/bugs/edit')">
        <el-icon><Plus /></el-icon>
        新建问题
      </el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索问题标题..."
        clearable
        style="width: 240px"
      />
      <el-select v-model="filterProject" placeholder="全部项目" clearable style="width: 160px">
        <el-option v-for="project in projects" :key="project" :label="project" :value="project" />
      </el-select>
      <el-select v-model="filterStatus" placeholder="全部状态" clearable style="width: 140px">
        <el-option label="待分配" value="pending" />
        <el-option label="处理中" value="processing" />
        <el-option label="待验证" value="testing" />
        <el-option label="已修复" value="completed" />
        <el-option label="未通过" value="rejected" />
      </el-select>
      <el-select v-model="filterPriority" placeholder="全部优先级" clearable style="width: 140px">
        <el-option label="高" value="high" />
        <el-option label="中" value="medium" />
        <el-option label="低" value="low" />
      </el-select>
    </div>

    <el-table
      :data="filteredBugs"
      stripe
      border
      style="width: 100%"
      row-key="id"
      :height="tableHeight"
    >
      <el-table-column prop="id" label="ID" width="80" align="center">
        <template #default="{ row }">
          <span class="id-cell">#{{ row.id }}</span>
        </template>
      </el-table-column>

      <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip>
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
      <el-table-column prop="createdAt" label="创建时间" width="100" />

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
      <span class="pagination-info">共 {{ filteredBugs.length }} 条记录</span>
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="10"
        :total="filteredBugs.length"
        layout="prev, pager, next"
        background
      />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'

interface Bug {
  id: number
  title: string
  project: string
  priority: string
  status: string
  assignee: string
  createdAt: string
  description?: string
}

export default defineComponent({
  name: 'BugListView',
  components: {
    Plus,
  },
  setup() {
    const router = useRouter()
    const searchKeyword = ref('')
    const filterProject = ref('')
    const filterStatus = ref('')
    const filterPriority = ref('')
    const currentPage = ref(1)
    const tableHeight = 'calc(100vh - 280px)'

    const projects = ref(['电商平台v2.0', '小程序优化', '支付系统升级', '后台管理系统'])

    const priorityLabels: Record<string, string> = {
      high: '高',
      medium: '中',
      low: '低',
    }

    const statusLabels: Record<string, string> = {
      pending: '待分配',
      processing: '处理中',
      testing: '待验证',
      completed: '已修复',
      rejected: '未通过',
    }

    const bugs = ref<Bug[]>([
      {
        id: 156,
        title: '用户登录接口返回500错误',
        project: '电商平台v2.0',
        priority: 'high',
        status: 'processing',
        assignee: '张三',
        createdAt: '2026-02-10',
      },
      {
        id: 155,
        title: '订单列表分页加载缓慢',
        project: '电商平台v2.0',
        priority: 'medium',
        status: 'pending',
        assignee: '李四',
        createdAt: '2026-02-10',
      },
      {
        id: 154,
        title: '购物车数量更新异常',
        project: '小程序优化',
        priority: 'high',
        status: 'testing',
        assignee: '王五',
        createdAt: '2026-02-09',
      },
      {
        id: 153,
        title: '支付回调地址配置错误',
        project: '支付系统升级',
        priority: 'low',
        status: 'completed',
        assignee: '张三',
        createdAt: '2026-02-09',
      },
      {
        id: 152,
        title: '商品详情页图片加载失败',
        project: '电商平台v2.0',
        priority: 'medium',
        status: 'pending',
        assignee: '赵六',
        createdAt: '2026-02-08',
      },
      {
        id: 151,
        title: '用户注册短信验证码发送失败',
        project: '电商平台v2.0',
        priority: 'high',
        status: 'processing',
        assignee: '李四',
        createdAt: '2026-02-08',
      },
      {
        id: 150,
        title: '搜索结果排序不正确',
        project: '电商平台v2.0',
        priority: 'medium',
        status: 'rejected',
        assignee: '王五',
        createdAt: '2026-02-07',
      },
      {
        id: 149,
        title: '订单导出功能无响应',
        project: '后台管理系统',
        priority: 'high',
        status: 'pending',
        assignee: '张三',
        createdAt: '2026-02-07',
      },
    ])

    const filteredBugs = computed(() => {
      return bugs.value.filter((bug) => {
        const matchKeyword = !searchKeyword.value || bug.title.includes(searchKeyword.value)
        const matchProject = !filterProject.value || bug.project === filterProject.value
        const matchStatus = !filterStatus.value || bug.status === filterStatus.value
        const matchPriority = !filterPriority.value || bug.priority === filterPriority.value
        return matchKeyword && matchProject && matchStatus && matchPriority
      })
    })

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
        processing: 'primary',
        testing: 'info',
        completed: 'success',
        rejected: 'danger',
      }
      return types[status] || 'info'
    }

    const goToDetail = (id: number) => {
      router.push({ path: `/bugs/${id}` })
    }

    const goToEdit = (id: number) => {
      router.push({ path: `/bugs/${id}/edit` })
    }

    return {
      searchKeyword,
      filterProject,
      filterStatus,
      filterPriority,
      currentPage,
      tableHeight,
      projects,
      priorityLabels,
      statusLabels,
      bugs,
      filteredBugs,
      getPriorityType,
      getStatusType,
      goToDetail,
      goToEdit,
    }
  },
})
</script>

<style lang="less" scoped>
.bug-list {
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
