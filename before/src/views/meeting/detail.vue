<!--
  会议纪要详情页面
  V1.2 新增：会议纪要管理功能
-->
<template>
  <div class="meeting-detail">
    <div class="page-header">
      <el-button link @click="$router.back()">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h1>会议纪要详情</h1>
      <el-button type="primary" @click="handleEdit">编辑</el-button>
    </div>

    <el-card class="info-card">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="会议ID">
          <span class="id-cell">#{{ meeting.id }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="meeting.status === 'discussed' ? 'success' : 'warning'" size="small">
            {{ meeting.status === 'discussed' ? '已讨论' : '未讨论' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="会议主题" :span="2">
          {{ meeting.title }}
        </el-descriptions-item>
        <el-descriptions-item label="所属项目">
          {{ meeting.project }}
        </el-descriptions-item>
        <el-descriptions-item label="会议日期">
          {{ meeting.meetingDate }}
        </el-descriptions-item>
        <el-descriptions-item label="参与人" :span="2">
          {{ meeting.participants.join(', ') }}
        </el-descriptions-item>
        <el-descriptions-item label="会议内容" :span="2">
          {{ meeting.content }}
        </el-descriptions-item>
        <el-descriptions-item label="会议结论" :span="2">
          {{ meeting.conclusion || '暂无' }}
        </el-descriptions-item>
        <el-descriptions-item label="腾讯会议链接" v-if="meeting.meetingLink">
          <el-link type="primary" :href="meeting.meetingLink" target="_blank">
            {{ meeting.meetingLink }}
          </el-link>
        </el-descriptions-item>
        <el-descriptions-item label="附件" v-if="meeting.attachment">
          <el-button type="primary" link @click="handleDownload"> 下载附件 </el-button>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ meeting.createdAt }}
        </el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card class="related-requirement-card" v-if="meeting.relatedRequirement">
      <template #header>
        <div class="card-header">
          <span>关联的需求</span>
        </div>
      </template>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="需求标题">
          {{ meeting.relatedRequirement.title }}
        </el-descriptions-item>
        <el-descriptions-item label="需求状态">
          <el-tag size="small">{{ meeting.relatedRequirement.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="操作">
          <el-button type="primary" link @click="viewRequirement"> 查看需求详情 </el-button>
        </el-descriptions-item>
      </el-descriptions>
    </el-card>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default defineComponent({
  name: 'MeetingDetailView',
  components: {
    ArrowLeft,
  },
  setup() {
    const router = useRouter()
    const route = useRoute()

    const meeting = ref({
      id: Number(route.params.id),
      title: '电商平台v2.0需求评审会',
      project: '电商平台v2.0',
      meetingDate: '2026-02-10',
      participants: ['张三', '李四', '王五', '赵六'],
      status: 'discussed',
      content: '讨论用户登录优化、订单分页性能优化等需求',
      conclusion: '通过用户登录优化需求，订单分页优化延后',
      meetingLink: 'https://meeting.tencent.com/abc123',
      attachment: 'meeting_notes.pdf', // V1.2.0
      createdAt: '2026-02-10 10:00:00',
      relatedRequirement: {
        id: 1,
        title: '用户登录优化',
        status: '进行中',
      },
    })

    const handleEdit = () => {
      router.push({ path: `/meetings/${meeting.value.id}/edit` })
    }

    const handleDownload = () => {
      if (meeting.value.attachment) {
        ElMessage.success(`下载附件: ${meeting.value.attachment}`)
      }
    }

    const viewRequirement = () => {
      if (meeting.value.relatedRequirement) {
        router.push({
          path: `/requirements/${meeting.value.relatedRequirement.id}`,
        })
      }
    }

    return {
      meeting,
      handleEdit,
      handleDownload,
      viewRequirement,
    }
  },
})
</script>

<style lang="less" scoped>
.meeting-detail {
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
.related-requirement-card {
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
</style>
