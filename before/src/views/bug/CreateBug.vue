<template>
  <div class="create-bug-page">
    <div class="page-header">
      <el-button @click="$router.back()">
        <el-icon><ArrowLeft /></el-icon>
        返回
      </el-button>
      <h1>新建问题</h1>
    </div>

    <el-card class="form-card">
      <el-form :model="form" label-width="100px" :rules="rules" ref="formRef">
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item label="问题标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入问题标题" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="项目" prop="project">
              <el-select
                v-model="form.project"
                placeholder="请选择项目"
                style="width: 100%"
              >
                <el-option
                  v-for="project in projects"
                  :key="project"
                  :label="project"
                  :value="project"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="优先级">
              <el-select v-model="form.priority" style="width: 100%">
                <el-option label="低" value="low" />
                <el-option label="中" value="medium" />
                <el-option label="高" value="high" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="问题类型">
              <el-select v-model="form.type" style="width: 100%">
                <el-option label="Bug" value="bug" />
                <el-option label="功能需求" value="feature" />
                <el-option label="优化建议" value="optimization" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发现环境">
              <el-select v-model="form.environment" style="width: 100%">
                <el-option label="开发环境" value="dev" />
                <el-option label="测试环境" value="test" />
                <el-option label="Beta环境" value="beta" />
                <el-option label="线上环境" value="prod" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发现版本">
              <el-input v-model="form.version" placeholder="请输入版本号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属模块">
              <el-input v-model="form.module" placeholder="请输入模块名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="问题描述">
              <el-input
                v-model="form.description"
                type="textarea"
                :rows="6"
                placeholder="请详细描述问题，包括：&#10;1. 问题描述&#10;2. 复现步骤&#10;3. 期望结果&#10;4. 实际结果&#10;5. 相关截图或日志"
              />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="附件上传">
              <el-upload
                drag
                action="#"
                :auto-upload="false"
                :on-change="handleFileChange"
                :on-remove="handleFileRemove"
                multiple
              >
                <el-icon class="el-icon--upload"><Upload /></el-icon>
                <div class="el-upload__text">
                  将文件拖到此处，或<em>点击上传</em>
                </div>
                <template #tip>
                  <div class="el-upload__tip">
                    支持 png、jpg、gif、txt、log 格式，单个文件不超过 10MB
                  </div>
                </template>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item>
              <el-button type="primary" @click="handleSubmit"
                >创建问题</el-button
              >
              <el-button @click="$router.back()">取消</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { ElMessage } from "element-plus";
import { ArrowLeft, Upload } from "@element-plus/icons-vue";
import type { FormInstance, FormRules } from "element-plus";

interface Attachment {
  name: string;
  size: number;
}

export default defineComponent({
  name: "CreateBugView",
  components: {
    ArrowLeft,
    Upload,
  },
  setup() {
    const router = useRouter();
    const formRef = ref<FormInstance>();

    const projects = ref([
      "电商平台v2.0",
      "小程序优化",
      "支付系统升级",
      "后台管理系统",
    ]);

    const form = reactive({
      title: "",
      project: "",
      priority: "medium",
      type: "bug",
      environment: "test",
      version: "",
      module: "",
      description: "",
      attachments: [] as Attachment[],
    });

    const rules: FormRules = {
      title: [{ required: true, message: "请输入问题标题", trigger: "blur" }],
      project: [{ required: true, message: "请选择项目", trigger: "change" }],
    };

    const handleFileChange = (file: any) => {
      form.attachments.push({
        name: file.name,
        size: file.size,
      });
    };

    const handleFileRemove = (file: any) => {
      const index = form.attachments.findIndex((a) => a.name === file.name);
      if (index !== -1) {
        form.attachments.splice(index, 1);
      }
    };

    const handleSubmit = async () => {
      if (!formRef.value) return;
      await formRef.value.validate((valid) => {
        if (valid) {
          console.log("创建问题:", form);
          ElMessage.success("问题创建成功！");
          router.push({ path: "/bugs" });
        }
      });
    };

    return {
      formRef,
      projects,
      form,
      rules,
      handleFileChange,
      handleFileRemove,
      handleSubmit,
    };
  },
});
</script>

<style lang="less" scoped>
.create-bug-page {
  max-width: 1000px;
  margin: 0 auto;
  padding: 20px;
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
  }
}

.form-card {
  :deep(.el-card__body) {
    padding: 24px;
  }
}
</style>
