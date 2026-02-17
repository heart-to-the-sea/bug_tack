<!--
  用户管理页面
  V1.2 新增：用户管理功能
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="user-list">
    <div class="page-header">
      <h1>用户管理</h1>
      <el-button type="primary" @click="$router.push('/system/users/edit')">
        <el-icon><Plus /></el-icon>
        新增用户
      </el-button>
    </div>

    <div class="search-bar">
      <el-input
        v-model="searchKeyword"
        placeholder="搜索用户名/手机号..."
        clearable
        style="width: 240px"
      />
      <el-select v-model="filterRole" placeholder="全部角色" clearable style="width: 140px">
        <el-option label="系统管理员" value="admin" />
        <el-option label="项目经理" value="manager" />
        <el-option label="开发人员" value="developer" />
        <el-option label="测试人员" value="tester" />
        <el-option label="只读用户" value="viewer" />
      </el-select>
      <el-select v-model="filterStatus" placeholder="全部状态" clearable style="width: 120px">
        <el-option label="启用" value="enabled" />
        <el-option label="禁用" value="disabled" />
      </el-select>
    </div>

    <el-table :data="filteredUsers" stripe border style="width: 100%" :height="tableHeight">
      <el-table-column prop="id" label="ID" width="80" align="center" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="realname" label="姓名" width="100" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="email" label="邮箱" min-width="180" />
      <el-table-column prop="role" label="角色" width="120" align="center">
        <template #default="{ row }">
          <el-tag :type="getRoleType(row.role)" size="small">{{ roleLabels[row.role] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80" align="center">
        <template #default="{ row }">
          <el-switch
            v-model="row.status"
            active-value="enabled"
            inactive-value="disabled"
            @change="handleStatusChange(row)"
          />
        </template>
      </el-table-column>
      <el-table-column prop="created_at" label="创建时间" width="120" />
      <el-table-column label="操作" width="150" fixed="right" align="center">
        <template #default="{ row }">
          <el-button type="primary" link size="small" @click="handleEdit(row.id)">编辑</el-button>
          <el-button type="primary" link size="small" @click="handleResetPwd(row)"
            >重置密码</el-button
          >
          <el-button type="danger" link size="small" @click="handleDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination">
      <span class="pagination-info">共 {{ filteredUsers.length }} 条记录</span>
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="10"
        :total="filteredUsers.length"
        layout="prev, pager, next"
        background
      />
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue' // V1.2.0
import { useRouter } from 'vue-router' // V1.2.0
import { Plus } from '@element-plus/icons-vue' // V1.2.0
import { ElMessage, ElMessageBox } from 'element-plus' // V1.2.0

interface User {
  // V1.2.0
  id: number // V1.2.0
  username: string // V1.2.0
  realname: string // V1.2.0
  phone: string // V1.2.0
  email: string // V1.2.0
  role: string // V1.2.0
  status: string // V1.2.0
  created_at: string // V1.2.0
} // V1.2.0

export default defineComponent({
  name: 'UserListView',
  components: {
    Plus,
  },
  setup() {
    const router = useRouter() // V1.2.0
    const searchKeyword = ref('') // V1.2.0
    const filterRole = ref('') // V1.2.0
    const filterStatus = ref('') // V1.2.0
    const currentPage = ref(1) // V1.2.0
    const tableHeight = 'calc(100vh - 280px)' // V1.2.0

    const roleLabels: Record<string, string> = {
      // V1.2.0
      admin: '系统管理员',
      manager: '项目经理',
      developer: '开发人员',
      tester: '测试人员',
      viewer: '只读用户',
    }

    const users = ref<User[]>([
      // V1.2.0 Mock数据
      {
        id: 1,
        username: 'admin',
        realname: '系统管理员',
        phone: '13800138000',
        email: 'admin@example.com',
        role: 'admin',
        status: 'enabled',
        created_at: '2026-01-01',
      },
      {
        id: 2,
        username: 'zhangsan',
        realname: '张三',
        phone: '13800138001',
        email: 'zhangsan@example.com',
        role: 'developer',
        status: 'enabled',
        created_at: '2026-01-05',
      },
      {
        id: 3,
        username: 'lisi',
        realname: '李四',
        phone: '13800138002',
        email: 'lisi@example.com',
        role: 'manager',
        status: 'enabled',
        created_at: '2026-01-08',
      },
      {
        id: 4,
        username: 'wangwu',
        realname: '王五',
        phone: '13800138003',
        email: 'wangwu@example.com',
        role: 'developer',
        status: 'enabled',
        created_at: '2026-01-10',
      },
      {
        id: 5,
        username: 'zhaoliu',
        realname: '赵六',
        phone: '13800138004',
        email: 'zhaoliu@example.com',
        role: 'tester',
        status: 'disabled',
        created_at: '2026-01-12',
      },
    ]) // V1.2.0

    const filteredUsers = computed(() => {
      return users.value.filter((user) => {
        const matchKeyword =
          !searchKeyword.value ||
          user.username.includes(searchKeyword.value) ||
          user.phone.includes(searchKeyword.value) ||
          user.realname.includes(searchKeyword.value)
        const matchRole = !filterRole.value || user.role === filterRole.value
        const matchStatus = !filterStatus.value || user.status === filterStatus.value
        return matchKeyword && matchRole && matchStatus
      })
    })

    const getRoleType = (role: string) => {
      const types: Record<string, string> = {
        admin: 'danger',
        manager: 'warning',
        developer: 'primary',
        tester: 'success',
        viewer: 'info',
      }
      return types[role] || 'info'
    }

    const handleStatusChange = (user: User) => {
      ElMessage.success(`用户 ${user.realname} 已${user.status === 'enabled' ? '启用' : '禁用'}`)
    }

    const handleEdit = (id: number) => {
      router.push({ path: `/system/users/${id}/edit` })
    }

    const handleResetPwd = (user: User) => {
      ElMessageBox.confirm(`确定要重置用户 ${user.realname} 的密码吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        ElMessage.success('密码已重置为: 123456')
      })
    }

    const handleDelete = (user: User) => {
      ElMessageBox.confirm(`确定要删除用户 ${user.realname} 吗？`, '警告', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }).then(() => {
        users.value = users.value.filter((u) => u.id !== user.id)
        ElMessage.success('删除成功')
      })
    }

    return {
      searchKeyword,
      filterRole,
      filterStatus,
      currentPage,
      tableHeight,
      roleLabels,
      users,
      filteredUsers,
      getRoleType,
      handleStatusChange,
      handleEdit,
      handleResetPwd,
      handleDelete,
    }
  },
})
</script>

<style lang="less" scoped>
.user-list {
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
</style>
