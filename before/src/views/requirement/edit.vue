<!--
  创建/编辑需求页面
  V1.2 新增：需求管理功能
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="create-requirement">
    <div class="page-header">
      <el-button link @click="$router.back()">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h1>{{ isEdit ? '编辑需求' : '创建需求' }}</h1>
    </div>

    <el-card class="form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="需求标题" prop="title">
          <el-input
            v-model="form.title"
            placeholder="请输入需求标题"
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

        <el-form-item label="优先级" prop="priority">
          <el-radio-group v-model="form.priority">
            <el-radio label="high">高</el-radio>
            <el-radio label="medium">中</el-radio>
            <el-radio label="low">低</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="负责人" prop="assignee">
          <el-select v-model="form.assignee" placeholder="请选择负责人">
            <el-option v-for="member in members" :key="member" :label="member" :value="member" />
          </el-select>
        </el-form-item>

        <el-form-item label="需求描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="6"
            placeholder="请输入需求描述"
            maxlength="2000"
            show-word-limit
          />
        </el-form-item>

        <el-form-item label="关联BUG">
          <el-select
            v-model="form.related_bugs"
            multiple
            placeholder="请选择关联的BUG（可选）"
            style="width: 100%"
          >
            <el-option
              v-for="bug in availableBugs"
              :key="bug.id"
              :label="`#${bug.id} ${bug.title}`"
              :value="bug.id"
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
import { defineComponent, ref, computed } from 'vue' // V1.2.0
import { useRouter, useRoute } from 'vue-router' // V1.2.0
import { ArrowLeft } from '@element-plus/icons-vue' // V1.2.0
import { ElMessage } from 'element-plus' // V1.2.0

export default defineComponent({
  name: 'RequirementEditView',
  components: {
    ArrowLeft,
  },
  setup() {
    const router = useRouter() // V1.2.0
    const route = useRoute() // V1.2.0
    const formRef = ref() // V1.2.0

    const isEdit = computed(() => !!route.params.id) // V1.2.0

    const form = ref({
      // V1.2.0
      title: '', // V1.2.0
      project_id: null as number | null, // V1.2.0
      priority: 'medium', // V1.2.0
      assignee: '', // V1.2.0
      description: '', // V1.2.0
      related_bugs: [] as number[], // V1.2.0
    }) // V1.2.0

    const rules = {
      // V1.2.0
      title: [{ required: true, message: '请输入需求标题', trigger: 'blur' }], // V1.2.0
      project_id: [{ required: true, message: '请选择所属项目', trigger: 'change' }], // V1.2.0
      priority: [{ required: true, message: '请选择优先级', trigger: 'change' }], // V1.2.0
      assignee: [{ required: true, message: '请选择负责人', trigger: 'change' }], // V1.2.0
      description: [{ required: true, message: '请输入需求描述', trigger: 'blur' }], // V1.2.0
    } // V1.2.0

    const projectList = ref([
      // V1.2.0 Mock数据
      { id: 1, name: '电商平台v2.0' },
      { id: 2, name: '小程序优化' },
      { id: 3, name: '支付系统升级' },
      { id: 4, name: '后台管理系统' },
    ]) // V1.2.0

    const members = ref(['张三', '李四', '王五', '赵六', '钱七']) // V1.2.0

    const availableBugs = ref([
      // V1.2.0 Mock数据
      { id: 156, title: '用户登录接口返回500错误' },
      { id: 155, title: '订单列表分页加载缓慢' },
      { id: 154, title: '购物车数量更新异常' },
      { id: 153, title: '支付回调地址配置错误' },
      { id: 152, title: '商品详情页图片加载失败' },
      { id: 151, title: '用户注册短信验证码发送失败' },
    ]) // V1.2.0

    if (isEdit.value) {
      const reqId = Number(route.params.id)
      if (reqId === 1) {
        form.value = {
          title: '用户登录功能优化',
          project_id: 1,
          priority: 'high',
          assignee: '张三',
          description: '优化用户登录流程，增加扫码登录功能，支持第三方账号绑定',
          related_bugs: [156, 155, 151],
        }
      }
    }

    const handleSubmit = async () => {
      const valid = await formRef.value.validate().catch(() => false)
      if (!valid) return

      ElMessage.success(isEdit.value ? '需求更新成功' : '需求创建成功')
      router.push({ path: '/requirements' })
    }

    return {
      formRef,
      form,
      rules,
      projectList,
      members,
      availableBugs,
      isEdit,
      handleSubmit,
    }
  },
})
</script>

<style lang="less" scoped>
.create-requirement {
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
