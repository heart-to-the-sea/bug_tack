<!--
  字典管理页面
  V1.2 新增：字典管理功能
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="dict-list">
    <div class="page-header">
      <h1>字典管理</h1>
    </div>

    <el-table :data="dictTypes" stripe border style="width: 100%" :height="tableHeight">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="name" label="字典名称" min-width="150" />
      <el-table-column prop="code" label="字典编码" min-width="150" />
      <el-table-column prop="description" label="描述" min-width="200" />
      <el-table-column prop="sort" label="排序" width="80" align="center" />
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="row.status === 'enabled' ? 'success' : 'info'" size="small">
            {{ row.status === 'enabled' ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="200" fixed="right" align="center">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="handleEditDict(row)">编辑</el-button>
          <el-button type="primary" link size="small" @click="handleManageValues(row)"
            >管理值</el-button
          >
          <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog
      v-model="valuesDialogVisible"
      :title="`管理字典值 - ${currentDict?.name}`"
      width="600px"
    >
      <el-table :data="currentDict?.values || []" border stripe>
        <el-table-column prop="label" label="字典标签" min-width="120" />
        <el-table-column prop="value" label="字典值" min-width="120" />
        <el-table-column prop="sort" label="排序" width="80" align="center" />
        <el-table-column prop="status" label="状态" width="80" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 'enabled' ? 'success' : 'info'" size="small">
              {{ row.status === 'enabled' ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100" align="center">
          <template #default="{ row }">
            <el-button type="primary" link size="small" @click="handleEditValue(row)"
              >编辑</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <template #footer>
        <el-button @click="valuesDialogVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue' // V1.2.0
import { ElMessage, ElMessageBox } from 'element-plus' // V1.2.0

interface DictValue {
  // V1.2.0
  label: string // V1.2.0
  value: string // V1.2.0
  sort: number // V1.2.0
  status: string // V1.2.0
} // V1.2.0

interface DictType {
  // V1.2.0
  id: number // V1.2.0
  name: string // V1.2.0
  code: string // V1.2.0
  description: string // V1.2.0
  sort: number // V1.2.0
  status: string // V1.2.0
  values: DictValue[] // V1.2.0
} // V1.2.0

export default defineComponent({
  name: 'DictListView',
  setup() {
    const tableHeight = 'calc(100vh - 180px)' // V1.2.0
    const valuesDialogVisible = ref(false) // V1.2.0
    const currentDict = ref<DictType | null>(null) // V1.2.0

    const dictTypes = ref<DictType[]>([
      // V1.2.0 Mock数据
      {
        id: 1,
        name: '问题类型',
        code: 'bug_type',
        description: '问题的类型分类',
        sort: 1,
        status: 'enabled',
        values: [
          { label: 'Bug', value: 'bug', sort: 1, status: 'enabled' },
          { label: '功能需求', value: 'feature', sort: 2, status: 'enabled' },
          { label: '优化建议', value: 'optimization', sort: 3, status: 'enabled' },
          { label: '技术支持', value: 'support', sort: 4, status: 'enabled' },
        ],
      },
      {
        id: 2,
        name: '严重程度',
        code: 'severity',
        description: '问题的严重程度',
        sort: 2,
        status: 'enabled',
        values: [
          { label: '致命', value: 'critical', sort: 1, status: 'enabled' },
          { label: '严重', value: 'high', sort: 2, status: 'enabled' },
          { label: '一般', value: 'medium', sort: 3, status: 'enabled' },
          { label: '轻微', value: 'low', sort: 4, status: 'enabled' },
        ],
      },
      {
        id: 3,
        name: '优先级',
        code: 'priority',
        description: '问题处理优先级',
        sort: 3,
        status: 'enabled',
        values: [
          { label: '高', value: 'high', sort: 1, status: 'enabled' },
          { label: '中', value: 'medium', sort: 2, status: 'enabled' },
          { label: '低', value: 'low', sort: 3, status: 'enabled' },
        ],
      },
      {
        id: 4,
        name: '发现环境',
        code: 'environment',
        description: '问题发现的环境',
        sort: 4,
        status: 'enabled',
        values: [
          { label: '开发环境', value: 'dev', sort: 1, status: 'enabled' },
          { label: '测试环境', value: 'test', sort: 2, status: 'enabled' },
          { label: 'Beta环境', value: 'beta', sort: 3, status: 'enabled' },
          { label: '线上环境', value: 'prod', sort: 4, status: 'enabled' },
        ],
      },
      {
        id: 5,
        name: '问题状态',
        code: 'bug_status',
        description: '问题的处理状态',
        sort: 5,
        status: 'enabled',
        values: [
          { label: '待分配', value: 'pending', sort: 1, status: 'enabled' },
          { label: '处理中', value: 'processing', sort: 2, status: 'enabled' },
          { label: '待验证', value: 'testing', sort: 3, status: 'enabled' },
          { label: '已修复', value: 'completed', sort: 4, status: 'enabled' },
          { label: '未通过', value: 'rejected', sort: 5, status: 'enabled' },
        ],
      },
    ]) // V1.2.0

    const handleEditDict = (row: DictType) => {
      ElMessage.info(`编辑字典: ${row.name}`)
    }

    const handleManageValues = (row: DictType) => {
      currentDict.value = row
      valuesDialogVisible.value = true
    }

    const handleEditValue = (row: DictValue) => {
      ElMessage.info(`编辑字典值: ${row.label}`)
    }

    const handleDelete = (row: DictType) => {
      ElMessageBox.confirm(`确定要删除字典 "${row.name}" 吗？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        dictTypes.value = dictTypes.value.filter((d) => d.id !== row.id)
        ElMessage.success('删除成功')
      })
    }

    return {
      tableHeight,
      dictTypes,
      valuesDialogVisible,
      currentDict,
      handleEditDict,
      handleManageValues,
      handleEditValue,
      handleDelete,
    }
  },
})
</script>

<style lang="less" scoped>
.dict-list {
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
</style>
