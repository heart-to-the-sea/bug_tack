<!--
  会议详情弹窗组件
  V1.2 新增：抽取弹窗组件
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <el-dialog v-model="visible" title="会议详情" width="600px">
    <el-descriptions :column="1" border v-if="meeting">
      <el-descriptions-item label="会议主题">
        {{ meeting.title }}
      </el-descriptions-item>
      <el-descriptions-item label="项目">
        {{ meeting.project }}
      </el-descriptions-item>
      <el-descriptions-item label="会议日期">
        {{ meeting.meetingDate }}
      </el-descriptions-item>
      <el-descriptions-item label="参与人">
        {{ meeting.participants.join(', ') }}
      </el-descriptions-item>
      <el-descriptions-item label="会议内容">
        {{ meeting.content }}
      </el-descriptions-item>
      <el-descriptions-item label="会议结论">
        {{ meeting.conclusion }}
      </el-descriptions-item>
      <el-descriptions-item label="腾讯会议链接" v-if="meeting.meetingLink">
        <el-link type="primary" :href="meeting.meetingLink" target="_blank">
          {{ meeting.meetingLink }}
        </el-link>
      </el-descriptions-item>
      <el-descriptions-item label="附件" v-if="meeting.attachment">
        <el-button type="primary" link @click="handleDownload"> 下载附件 </el-button>
      </el-descriptions-item>
    </el-descriptions>
    <template #footer>
      <el-button @click="close">关闭</el-button>
    </template>
  </el-dialog>
</template>

<script lang="ts">
import { defineComponent, ref, watch } from 'vue'
import { ElMessage } from 'element-plus'

interface Meeting {
  id: number
  title: string
  project: string
  meetingDate: string
  participants: string[]
  status: string
  content: string
  conclusion: string
  attachment?: string
  meetingLink?: string
}

export default defineComponent({
  name: 'MeetingDetailDialog',
  props: {
    modelValue: {
      type: Boolean,
      default: false,
    },
    meeting: {
      type: Object as () => Meeting | null,
      default: null,
    },
  },
  emits: ['update:modelValue'],
  setup(props, { emit }) {
    const visible = ref(props.modelValue) // V1.2.0

    watch(
      () => props.modelValue,
      (val) => {
        visible.value = val
      }
    )

    watch(visible, (val) => {
      // V1.2.0
      emit('update:modelValue', val)
    })

    const close = () => {
      // V1.2.0
      visible.value = false
    }

    const handleDownload = () => {
      // V1.2.0
      if (props.meeting?.attachment) {
        ElMessage.success(`下载附件: ${props.meeting.attachment}`)
      }
    }

    return {
      visible,
      close,
      handleDownload,
    }
  },
})
</script>
