<!--
  用户编辑页面
  V1.2 新增：用户管理功能
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="user-edit">
    <div class="page-header">
      <el-button link @click="$router.back()">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h1>{{ isEdit ? '编辑用户' : '新增用户' }}</h1>
    </div>

    <el-card class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="form.username" placeholder="请输入用户名" :disabled="isEdit" />
        </el-form-item>

        <el-form-item label="姓名" prop="realname">
          <el-input v-model="form.realname" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="手机号" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option label="系统管理员" value="admin" />
            <el-option label="项目经理" value="manager" />
            <el-option label="开发人员" value="developer" />
            <el-option label="测试人员" value="tester" />
            <el-option label="只读用户" value="viewer" />
          </el-select>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-switch
            v-model="form.status"
            active-value="enabled"
            inactive-value="disabled"
            active-text="启用"
            inactive-text="禁用"
          />
        </el-form-item>

        <el-form-item label="密码" prop="password" v-if="!isEdit">
          <el-input
            v-model="form.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleSubmit">{{ isEdit ? '保存' : '创建' }}</el-button>
          <el-button @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed } from 'vue' // V1.2.0
import { useRouter, useRoute } from 'vue-router' // V1.2.0
import { ArrowLeft } from '@element-plus/icons-vue' // V1.2.0
import { ElMessage } from 'element-plus' // V1.2.0

export default defineComponent({
  name: 'UserEditView',
  components: {
    ArrowLeft,
  },
  setup() {
    const router = useRouter() // V1.2.0
    const route = useRoute() // V1.2.0
    const formRef = ref() // V1.2.0

    const isEdit = computed(() => !!route.params.id) // V1.2.0
    const userId = computed(() => Number(route.params.id)) // V1.2.0

    const form = ref({
      // V1.2.0
      username: '', // V1.2.0
      realname: '', // V1.2.0
      phone: '', // V1.2.0
      email: '', // V1.2.0
      role: 'developer', // V1.2.0
      status: 'enabled', // V1.2.0
      password: '', // V1.2.0
    }) // V1.2.0

    const rules = {
      // V1.2.0
      username: [{ required: true, message: '请输入用户名', trigger: 'blur' }], // V1.2.0
      realname: [{ required: true, message: '请输入姓名', trigger: 'blur' }], // V1.2.0
      phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }], // V1.2.0
      email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' },
      ], // V1.2.0
      role: [{ required: true, message: '请选择角色', trigger: 'change' }], // V1.2.0
      password: [{ required: true, message: '请输入密码', trigger: 'blur' }], // V1.2.0
    } // V1.2.0

    if (isEdit.value && userId.value === 2) {
      form.value = {
        username: 'zhangsan',
        realname: '张三',
        phone: '13800138001',
        email: 'zhangsan@example.com',
        role: 'developer',
        status: 'enabled',
        password: '',
      }
    }

    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return

      ElMessage.success(isEdit.value ? '用户更新成功' : '用户创建成功')
      router.push({ path: '/system/users' })
    }

    return {
      formRef,
      form,
      rules,
      isEdit,
      handleSubmit,
    }
  },
})
</script>

<style lang="less" scoped>
.user-edit {
  max-width: 600px;
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
