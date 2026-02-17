<!--
  项目列表页面
  V1.2 更新：添加成员管理功能
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="project-list">
    <div class="page-header">
      <h1>项目管理</h1>
      <el-button type="primary" @click="showCreateDialog = true">
        <el-icon><Plus /></el-icon>
        新建项目
      </el-button>
    </div>

    <el-row :gutter="20">
      <el-col :span="8" v-for="project in projectList" :key="project.id">
        <el-card class="project-card" shadow="hover">
          <template #header>
            <div class="project-header">
              <span class="project-name">{{ project.name }}</span>
              <el-tag :type="getStatusType(project.status)" size="small">
                {{ statusLabels[project.status] }}
              </el-tag>
            </div>
          </template>
          <div class="project-desc">{{ project.description || '暂无描述' }}</div>
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
            <el-button type="primary" link @click="viewProject(project)">查看详情</el-button>
            <el-button type="primary" link @click="editProject(project)">编辑</el-button>
            <el-button type="primary" link @click="manageMembers(project)">成员管理</el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-empty v-if="projectList.length === 0" description="暂无项目" />

    <el-dialog v-model="showCreateDialog" title="新建项目" width="500px">
      <el-form ref="formRef" :model="newProject" :rules="rules" label-width="100px">
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="newProject.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目描述">
          <el-input
            v-model="newProject.description"
            type="textarea"
            :rows="3"
            placeholder="请输入项目描述"
          />
        </el-form-item>
        <el-form-item label="项目负责人">
          <el-select v-model="newProject.owner" placeholder="请选择负责人" style="width: 100%">
            <el-option label="张三" value="张三" />
            <el-option label="李四" value="李四" />
            <el-option label="王五" value="王五" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button type="primary" @click="createProjectHandler">创建</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="showMemberDialog"
      :title="`成员管理 - ${currentProject?.name}`"
      width="600px"
    >
      <el-table :data="currentMembers" border style="width: 100%">
        <el-table-column prop="name" label="成员" />
        <el-table-column label="角色" width="150">
          <template #default="{ row }">
            <el-select v-model="row.role" size="small" @change="updateRole(row)">
              <el-option label="开发" value="developer" />
              <el-option label="测试" value="tester" />
              <el-option label="部署" value="deployer" />
              <el-option label="只读" value="viewer" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="80">
          <template #default="{ row }">
            <el-button type="danger" link size="small" @click="removeMember(row)">移除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-divider />

      <div class="add-member">
        <h4>添加成员</h4>
        <el-form inline>
          <el-form-item>
            <el-select v-model="newMember.name" placeholder="选择成员" style="width: 120px">
              <el-option v-for="m in availableMembers" :key="m" :label="m" :value="m" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-select v-model="newMember.role" placeholder="选择角色" style="width: 100px">
              <el-option label="开发" value="developer" />
              <el-option label="测试" value="tester" />
              <el-option label="部署" value="deployer" />
              <el-option label="只读" value="viewer" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="addMember">添加</el-button>
          </el-form-item>
        </el-form>
      </div>

      <template #footer>
        <el-button @click="showMemberDialog = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue' // V1.2.0
import { ElMessage } from 'element-plus' // V1.2.0
import { Plus } from '@element-plus/icons-vue' // V1.2.0

interface ProjectItem {
  // V1.2.0
  id: number // V1.2.0
  name: string // V1.2.0
  description?: string // V1.2.0
  owner: string // V1.2.0
  members: number // V1.2.0
  bugCount: number // V1.2.0
  status: string // V1.2.0
} // V1.2.0

export default defineComponent({
  name: 'ProjectListView',
  components: {
    Plus,
  },
  setup() {
    const loading = ref(false) // V1.2.0
    const showCreateDialog = ref(false) // V1.2.0
    const showMemberDialog = ref(false) // V1.2.0
    const formRef = ref() // V1.2.0
    const currentProject = ref<ProjectItem | null>(null) // V1.2.0
    const currentMembers = ref<{ name: string; role: string }[]>([]) // V1.2.0
    const newMember = ref({ name: '', role: 'developer' }) // V1.2.0

    const statusLabels: Record<string, string> = {
      // V1.2.0
      active: '进行中', // V1.2.0
      completed: '已完成', // V1.2.0
      archived: '已归档', // V1.2.0
    }

    const roleLabels: Record<string, string> = {
      // V1.2.0
      developer: '开发', // V1.2.0
      tester: '测试', // V1.2.0
      deployer: '部署', // V1.2.0
      viewer: '只读', // V1.2.0
    }

    const allMembers = ref([
      // V1.2.0
      { name: '张三', role: 'developer', projects: [1, 4] }, // V1.2.0
      { name: '李四', role: 'developer', projects: [2] }, // V1.2.0
      { name: '王五', role: 'developer', projects: [2] }, // V1.2.0
      { name: '赵六', role: 'tester', projects: [1] }, // V1.2.0
      { name: '钱七', role: 'deployer', projects: [3] }, // V1.2.0
    ]) // V1.2.0

    const projectList = ref<ProjectItem[]>([
      // V1.2.0 Mock数据
      {
        id: 1,
        name: '电商平台v2.0',
        description: '新一代电商平台系统，支持多终端统一管理',
        owner: '张三',
        members: 8,
        bugCount: 23,
        status: 'active',
      },
      {
        id: 2,
        name: '小程序优化',
        description: '微信小程序性能优化与功能迭代',
        owner: '李四',
        members: 5,
        bugCount: 12,
        status: 'active',
      },
      {
        id: 3,
        name: '支付系统升级',
        description: '支付通道升级，支持更多支付方式',
        owner: '王五',
        members: 4,
        bugCount: 8,
        status: 'completed',
      },
      {
        id: 4,
        name: '后台管理系统',
        description: '企业内部管理系统',
        owner: '赵六',
        members: 6,
        bugCount: 15,
        status: 'active',
      },
    ]) // V1.2.0

    const newProject = ref({
      // V1.2.0
      name: '', // V1.2.0
      description: '', // V1.2.0
      owner: '', // V1.2.0
    }) // V1.2.0

    const rules = {
      // V1.2.0
      name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }], // V1.2.0
    } // V1.2.0

    const availableMembers = computed(() => {
      if (!currentProject.value) return []
      const projectMemberNames = currentMembers.value.map((m) => m.name)
      return allMembers.value.filter((m) => !projectMemberNames.includes(m.name)).map((m) => m.name)
    })

    const getStatusType = (status: string) => {
      // V1.2.0
      const types: Record<string, string> = {
        active: 'primary', // V1.2.0
        completed: 'success', // V1.2.0
        archived: 'info', // V1.2.0
      }
      return types[status] || 'info'
    }

    const viewProject = (project: ProjectItem) => {
      console.log('查看项目:', project.id)
    }

    const editProject = (project: ProjectItem) => {
      console.log('编辑项目:', project.id)
    }

    const manageMembers = (project: ProjectItem) => {
      currentProject.value = project
      currentMembers.value = allMembers.value
        .filter((m) => m.projects.includes(project.id))
        .map((m) => ({ name: m.name, role: m.role }))
      showMemberDialog.value = true
    }

    const addMember = () => {
      if (!newMember.value.name || !currentProject.value) return
      currentMembers.value.push({ ...newMember.value })
      const member = allMembers.value.find((m) => m.name === newMember.value.name)
      if (member) {
        member.projects.push(currentProject.value.id)
      }
      currentProject.value.members = currentMembers.value.length
      newMember.value = { name: '', role: 'developer' }
    }

    const removeMember = (member: { name: string; role: string }) => {
      currentMembers.value = currentMembers.value.filter((m) => m.name !== member.name)
      const allMember = allMembers.value.find((m) => m.name === member.name)
      if (allMember && currentProject.value) {
        allMember.projects = allMember.projects.filter((p) => p !== currentProject.value?.id)
      }
      if (currentProject.value) {
        currentProject.value.members = currentMembers.value.length
      }
    }

    const updateRole = (member: { name: string; role: string }) => {
      const allMember = allMembers.value.find((m) => m.name === member.name)
      if (allMember) {
        allMember.role = member.role
      }
    }

    const createProjectHandler = () => {
      // V1.2.0
      if (!newProject.value.name) {
        ElMessage.warning('请填写项目名称')
        return
      }
      const newId = Math.max(...projectList.value.map((p) => p.id)) + 1
      projectList.value.push({
        id: newId,
        name: newProject.value.name,
        description: newProject.value.description,
        owner: newProject.value.owner || '未分配',
        members: 0,
        bugCount: 0,
        status: 'active',
      })
      ElMessage.success('项目创建成功')
      showCreateDialog.value = false
      newProject.value = { name: '', description: '', owner: '' }
    }

    return {
      loading, // V1.2.0
      showCreateDialog, // V1.2.0
      showMemberDialog, // V1.2.0
      formRef, // V1.2.0
      currentProject,
      currentMembers,
      newMember,
      statusLabels,
      roleLabels,
      projectList, // V1.2.0
      newProject,
      rules, // V1.2.0
      availableMembers,
      getStatusType,
      viewProject,
      editProject,
      manageMembers,
      addMember,
      removeMember,
      updateRole,
      createProjectHandler, // V1.2.0
    }
  },
})
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

.project-card {
  margin-bottom: 20px;

  .project-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .project-name {
      font-size: 16px;
      font-weight: 600;
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
    gap: 8px;
  }
}

.add-member {
  h4 {
    margin-bottom: 12px;
    font-weight: 500;
  }
}
</style>
