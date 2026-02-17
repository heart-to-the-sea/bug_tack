<!--
  会议纪要列表页面
  V1.2 新增：会议纪要管理功能
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="meeting-list">
    <div class="page-header">
      <h1>会议纪要</h1>
      <el-button type="primary" @click="$router.push('/meetings/edit')">
        <el-icon><Plus /></el-icon>
        新建会议
      </el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索会议主题..."
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
        <el-option label="已讨论" value="discussed" />
        <el-option label="未讨论" value="pending" />
      </el-select>
      <el-button type="primary" @click="handleSearch">搜索</el-button>
    </div>

    <el-table
      v-loading="loading"
      :data="meetings"
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

      <el-table-column prop="title" label="会议主题" min-width="200" show-overflow-tooltip>
        <template #default="{ row }">
          <el-link type="primary" :underline="false" @click="viewDetail(row.id)">
            {{ row.title }}
          </el-link>
        </template>
      </el-table-column>

      <el-table-column prop="project" label="项目" width="120" />

      <el-table-column prop="meeting_date" label="会议日期" width="120" align="center" />

      <el-table-column prop="participants" label="参与人" width="150">
        <template #default="{ row }">
          <el-tooltip :content="(row.participants || []).join(', ')" placement="top">
            <span>{{ (row.participants || []).length }}人</span>
          </el-tooltip>
        </template>
      </el-table-column>

      <el-table-column prop="status" label="状态" width="100" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 'discussed' ? 'success' : 'warning'" size="small">
            {{ row.status === 'discussed' ? '已讨论' : '未讨论' }}
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="附件/链接" width="120" align="center">
        <template #default="{ row }">
          <el-button
            v-if="row.attachment || row.meeting_link"
            type="primary"
            link
            size="small"
            @click="openAttachment(row)"
          >
            查看
          </el-button>
          <span v-else class="no-attachment">无</span>
        </template>
      </el-table-column>

      <el-table-column prop="created_at" label="创建时间" width="100" />

      <el-table-column label="操作" width="100" fixed="right" align="center">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="goToEdit(row.id)"> 编辑 </el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <span class="pagination-info">共 {{ total }} 条记录</span>
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="10"
        :total="total"
        layout="prev, pager, next"
        background
        @current-change="handlePageChange"
      />
    </div>

    <MeetingDetailDialog v-model="dialogVisible" :meeting="currentMeeting" />
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue' // V1.2.0
import { useRouter } from 'vue-router' // V1.2.0
import { Plus } from '@element-plus/icons-vue' // V1.2.0
import { ElMessage } from 'element-plus' // V1.2.0
import MeetingDetailDialog from './components/MeetingDetailDialog.vue' // V1.2.0

interface Meeting {
  // V1.2.0
  id: number // V1.2.0
  title: string // V1.2.0
  project: string // V1.2.0
  meeting_date: string // V1.2.0
  participants: string[] // V1.2.0
  status: string // V1.2.0
  attachment?: string // V1.2.0
  meeting_link?: string // V1.2.0
  created_at: string // V1.2.0
} // V1.2.0

export default defineComponent({
  name: 'MeetingListView',
  components: {
    Plus,
    MeetingDetailDialog,
  },
  setup() {
    const router = useRouter() // V1.2.0
    const loading = ref(false) // V1.2.0
    const searchKeyword = ref('') // V1.2.0
    const filterProject = ref('') // V1.2.0
    const filterStatus = ref('') // V1.2.0
    const currentPage = ref(1) // V1.2.0
    const tableHeight = 'calc(100vh - 280px)' // V1.2.0
    const total = ref(0) // V1.2.0
    const dialogVisible = ref(false) // V1.2.0
    const currentMeeting = ref<Meeting | null>(null) // V1.2.0

    const projects = ref([
      // V1.2.0
      '电商平台v2.0',
      '小程序优化',
      '支付系统升级',
      '后台管理系统',
    ])

    const meetings = ref<Meeting[]>([
      // V1.2.0 Mock数据
      {
        id: 1,
        title: '电商平台v2.0需求评审会议',
        project: '电商平台v2.0',
        meeting_date: '2026-02-12',
        participants: ['张三', '李四', '王五', '赵六'],
        status: 'discussed',
        meeting_link: 'https://meeting.tencent.com/abc123',
        created_at: '2026-02-10',
      },
      {
        id: 2,
        title: '小程序性能优化讨论',
        project: '小程序优化',
        meeting_date: '2026-02-13',
        participants: ['王五', '赵六'],
        status: 'pending',
        created_at: '2026-02-11',
      },
      {
        id: 3,
        title: '支付系统升级方案评审',
        project: '支付系统升级',
        meeting_date: '2026-02-11',
        participants: ['张三', '李四', '钱七'],
        status: 'discussed',
        attachment: '支付系统升级方案.pdf',
        created_at: '2026-02-09',
      },
    ]) // V1.2.0

    total.value = meetings.value.length

    const handleSearch = () => {
      currentPage.value = 1
    }

    const handlePageChange = (page: number) => {
      currentPage.value = page
    } // V1.2.0

    const viewDetail = (id: number) => {
      // V1.2.0
      currentMeeting.value = meetings.value.find((m) => m.id === id) || null // V1.2.0
      dialogVisible.value = true // V1.2.0
    } // V1.2.0

    const goToEdit = (id: number) => {
      // V1.2.0
      router.push({ path: `/meetings/${id}/edit` }) // V1.2.0
    } // V1.2.0

    const openAttachment = (meeting: Meeting) => {
      // V1.2.0
      if (meeting.meeting_link) {
        window.open(meeting.meeting_link, '_blank') // V1.2.0
      } else if (meeting.attachment) {
        ElMessage.success(`下载附件: ${meeting.attachment}`) // V1.2.0
      }
    } // V1.2.0

    return {
      loading,
      searchKeyword,
      filterProject,
      filterStatus,
      currentPage,
      tableHeight,
      total,
      projects,
      meetings,
      dialogVisible,
      currentMeeting,
      handleSearch,
      handlePageChange,
      viewDetail,
      goToEdit,
      openAttachment,
    }
  },
})
</script>

<style lang="less" scoped>
.meeting-list {
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

.no-attachment {
  color: #c0c4cc;
}
</style>
