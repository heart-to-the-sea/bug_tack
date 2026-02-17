<!--
  创建/编辑会议纪要页面
  V1.2 新增：会议纪要管理功能
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="create-meeting" v-loading="loading">
    <div class="page-header">
      <el-button link @click="$router.back()">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h1>{{ isEdit ? '编辑会议纪要' : '创建会议纪要' }}</h1>
    </div>

    <el-card class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="会议主题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="请输入会议主题"
            maxlength="100"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="所属项目" prop="project_id">
          <el-select v-model="form.project_id" placeholder="请选择项目">
            <el-option
              v-for="project in projectList"
              :key="project.id"
              :label="project.name"
              :value="project.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="会议日期" prop="meeting_date">
          <el-date-picker
            v-model="form.meeting_date"
            type="date"
            placeholder="选择会议日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>

        <el-form-item label="参与人" prop="participants">
          <el-select
            v-model="form.participants"
            multiple
            placeholder="请选择参与人"
            style="width: 100%"
          >
            <el-option v-for="member in members" :key="member" :label="member" :value="member" />
          </el-select>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="pending">未讨论</el-radio>
            <el-radio label="discussed">已讨论</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="会议内容" prop="content">
          <el-input
            v-model="form.content"
            type="textarea"
            :rows="4"
            placeholder="请输入会议内容"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="会议结论" prop="conclusion">
          <el-input
            v-model="form.conclusion"
            type="textarea"
            :rows="4"
            placeholder="请输入会议结论"
            maxlength="1000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="腾讯会议链接">
          <el-input v-model="form.meeting_link" placeholder="请输入腾讯会议链接（可选）" />
        </el-form-item>

        <el-form-item label="附件">
          <el-upload
            action="#"
            :auto-upload="false"
            :show-file-list="false"
            accept=".pdf,.doc,.docx,.xls,.xlsx"
          >
            <el-button type="primary" link>
              <el-icon><Upload /></el-icon>
              上传附件
            </el-button>
          </el-upload>
        </el-form-item>

        <el-form-item label="关联需求">
          <el-select
            v-model="form.related_requirement_id"
            placeholder="请选择关联的需求（可选）"
            clearable
            style="width: 100%"
          >
            <el-option
              v-for="req in requirementList"
              :key="req.id"
              :label="req.title"
              :value="req.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit">
            {{ isEdit ? '保存' : '创建' }}
          </el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, onMounted } from 'vue' // V1.2.0
import { useRouter, useRoute } from 'vue-router' // V1.2.0
import { ArrowLeft, Upload } from '@element-plus/icons-vue' // V1.2.0
import { ElMessage } from 'element-plus' // V1.2.0
import {
  getMeetingDetail,
  createMeeting,
  updateMeeting,
  getProjectList,
  getRequirementList,
  Meeting,
} from '@/api' // V1.2.0

export default defineComponent({
  name: 'MeetingEditView',
  components: {
    ArrowLeft,
    Upload,
  },
  setup() {
    const router = useRouter() // V1.2.0
    const route = useRoute() // V1.2.0
    const formRef = ref() // V1.2.0
    const loading = ref(false) // V1.2.0
    const isEdit = computed(() => !!route.params.id) // V1.2.0
    const meetingId = computed(() => Number(route.params.id)) // V1.2.0

    const form = ref({
      // V1.2.0
      title: '', // V1.2.0
      project_id: null as number | null, // V1.2.0
      meeting_date: '', // V1.2.0
      participants: [] as string[], // V1.2.0
      status: 'pending', // V1.2.0
      content: '', // V1.2.0
      conclusion: '', // V1.2.0
      meeting_link: '', // V1.2.0
      related_requirement_id: null as number | null, // V1.2.0
    }) // V1.2.0

    const rules = {
      // V1.2.0
      title: [{ required: true, message: '请输入会议主题', trigger: 'blur' }], // V1.2.0
      project_id: [{ required: true, message: '请选择所属项目', trigger: 'change' }], // V1.2.0
      meeting_date: [{ required: true, message: '请选择会议日期', trigger: 'change' }], // V1.2.0
      participants: [{ required: true, message: '请选择参与人', trigger: 'change' }], // V1.2.0
      content: [{ required: true, message: '请输入会议内容', trigger: 'blur' }], // V1.2.0
    } // V1.2.0

    const projectList = ref<Array<{ id: number; name: string }>>([]) // V1.2.0
    const members = ref(['张三', '李四', '王五', '赵六', '钱七']) // V1.2.0
    const requirementList = ref<Array<{ id: number; title: string }>>([]) // V1.2.0

    const fetchProjectList = async () => {
      // V1.2.0
      try {
        const res = await getProjectList({ page: 1, page_size: 100 }) // V1.2.0
        projectList.value = res.data?.list || [] // V1.2.0
      } catch (error) {
        // V1.2.0
        console.error('获取项目列表失败', error) // V1.2.0
      }
    } // V1.2.0

    const fetchRequirementList = async () => {
      // V1.2.0
      try {
        const res = await getRequirementList({ page: 1, page_size: 100 }) // V1.2.0
        requirementList.value = res.data?.list || [] // V1.2.0
      } catch (error) {
        // V1.2.0
        console.error('获取需求列表失败', error) // V1.2.0
      }
    } // V1.2.0

    const fetchDetail = async () => {
      // V1.2.0
      if (!isEdit.value) return // V1.2.0
      loading.value = true // V1.2.0
      try {
        const res = await getMeetingDetail(meetingId.value) // V1.2.0
        const data = res.data as Meeting // V1.2.0
        form.value = {
          // V1.2.0
          title: data.title || '', // V1.2.0
          project_id: data.project_id || null, // V1.2.0
          meeting_date: data.meeting_date || '', // V1.2.0
          participants: data.participants || [], // V1.2.0
          status: data.status || 'pending', // V1.2.0
          content: data.content || '', // V1.2.0
          conclusion: data.conclusion || '', // V1.2.0
          meeting_link: data.meeting_link || '', // V1.2.0
          related_requirement_id: data.related_requirement_id || null, // V1.2.0
        } // V1.2.0
      } catch (error) {
        // V1.2.0
        ElMessage.error('获取会议详情失败') // V1.2.0
      } finally {
        loading.value = false // V1.2.0
      }
    } // V1.2.0

    const handleSubmit = async () => {
      // V1.2.0
      const valid = await formRef.value.validate().catch(() => false) // V1.2.0
      if (!valid) return // V1.2.0

      loading.value = true // V1.2.0
      try {
        if (isEdit.value) {
          // V1.2.0
          await updateMeeting(meetingId.value, {
            // V1.2.0
            title: form.value.title, // V1.2.0
            project_id: form.value.project_id as number, // V1.2.0
            meeting_date: form.value.meeting_date, // V1.2.0
            participants: form.value.participants, // V1.2.0
            status: form.value.status, // V1.2.0
            content: form.value.content, // V1.2.0
            conclusion: form.value.conclusion, // V1.2.0
            meeting_link: form.value.meeting_link || undefined, // V1.2.0
            related_requirement_id: form.value.related_requirement_id || undefined, // V1.2.0
          }) // V1.2.0
          ElMessage.success('会议纪要保存成功') // V1.2.0
        } else {
          // V1.2.0
          const projectId = form.value.project_id // V1.2.0
          if (!projectId) {
            // V1.2.0
            ElMessage.error('请选择所属项目') // V1.2.0
            return // V1.2.0
          } // V1.2.0
          await createMeeting({
            // V1.2.0
            title: form.value.title, // V1.2.0
            project_id: projectId, // V1.2.0
            meeting_date: form.value.meeting_date, // V1.2.0
            participants: form.value.participants as unknown as number[], // V1.2.0
            status: form.value.status, // V1.2.0
            content: form.value.content, // V1.2.0
            conclusion: form.value.conclusion || undefined, // V1.2.0
            meeting_link: form.value.meeting_link || undefined, // V1.2.0
            related_requirement_id: form.value.related_requirement_id || undefined, // V1.2.0
          }) // V1.2.0
          ElMessage.success('会议纪要创建成功') // V1.2.0
        }
        router.push({ path: '/meetings' }) // V1.2.0
      } catch (error) {
        // V1.2.0
        ElMessage.error(isEdit.value ? '保存会议纪要失败' : '创建会议纪要失败') // V1.2.0
      } finally {
        loading.value = false // V1.2.0
      }
    } // V1.2.0

    onMounted(() => {
      // V1.2.0
      fetchProjectList() // V1.2.0
      fetchRequirementList() // V1.2.0
      fetchDetail() // V1.2.0
    }) // V1.2.0

    return {
      formRef,
      form,
      rules,
      projectList,
      members,
      requirementList,
      isEdit,
      loading,
      handleSubmit,
    }
  },
})
</script>

<style lang="less" scoped>
.create-meeting {
  max-width: 800px;
  margin: 0 auto;
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
  }
}

.form-card {
  border-radius: 8px;
}
</style>
