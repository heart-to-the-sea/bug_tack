<template>
  <div class="edit-bug-page">
    <div class="page-header">
      <button class="back-btn" @click="$router.back()">← 返回</button>
      <h1>编辑问题 #{{ bugId }}</h1>
    </div>

    <div class="form-container">
      <div class="form-section">
        <h3>基本信息</h3>
        <div class="form-row">
          <div class="form-item">
            <label>问题标题 <span class="required">*</span></label>
            <input
              type="text"
              class="form-input"
              v-model="form.title"
              placeholder="请输入问题标题"
            />
          </div>
        </div>

        <div class="form-row">
          <div class="form-item half">
            <label>项目 <span class="required">*</span></label>
            <select class="form-select" v-model="form.project">
              <option value="">请选择项目</option>
              <option
                v-for="project in projects"
                :key="project"
                :value="project"
              >
                {{ project }}
              </option>
            </select>
          </div>
          <div class="form-item half">
            <label>优先级</label>
            <select class="form-select" v-model="form.priority">
              <option value="low">低</option>
              <option value="medium">中</option>
              <option value="high">高</option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-item half">
            <label>问题类型</label>
            <select class="form-select" v-model="form.type">
              <option value="bug">Bug</option>
              <option value="feature">功能需求</option>
              <option value="optimization">优化建议</option>
            </select>
          </div>
          <div class="form-item half">
            <label>状态</label>
            <select class="form-select" v-model="form.status">
              <option value="pending">待分配</option>
              <option value="processing">处理中</option>
              <option value="testing">待验证</option>
              <option value="completed">已修复</option>
              <option value="rejected">未通过</option>
            </select>
          </div>
        </div>

        <div class="form-row">
          <div class="form-item half">
            <label>发现环境</label>
            <select class="form-select" v-model="form.environment">
              <option value="dev">开发环境</option>
              <option value="test">测试环境</option>
              <option value="beta">Beta环境</option>
              <option value="prod">线上环境</option>
            </select>
          </div>
          <div class="form-item half">
            <label>发现版本</label>
            <input
              type="text"
              class="form-input"
              v-model="form.version"
              placeholder="请输入版本号"
            />
          </div>
        </div>

        <div class="form-row">
          <div class="form-item half">
            <label>负责人</label>
            <select class="form-select" v-model="form.assignee">
              <option value="">请选择负责人</option>
              <option value="张三">张三</option>
              <option value="李四">李四</option>
              <option value="王五">王五</option>
              <option value="赵六">赵六</option>
            </select>
          </div>
          <div class="form-item half">
            <label>所属模块</label>
            <input
              type="text"
              class="form-input"
              v-model="form.module"
              placeholder="请输入模块名称"
            />
          </div>
        </div>
      </div>

      <div class="form-section">
        <h3>问题描述</h3>
        <div class="form-item">
          <label>详细描述</label>
          <textarea
            class="form-textarea"
            v-model="form.description"
            placeholder="请详细描述问题，包括：&#10;1. 问题描述&#10;2. 复现步骤&#10;3. 期望结果&#10;4. 实际结果&#10;5. 相关截图或日志"
          ></textarea>
        </div>
      </div>

      <div class="form-section">
        <h3>修改备注</h3>
        <div class="form-item">
          <label>修改原因 <span class="required">*</span></label>
          <textarea
            class="form-textarea"
            v-model="form.changeReason"
            placeholder="请描述本次修改的原因..."
          ></textarea>
        </div>
      </div>

      <div class="form-section">
        <h3>附件管理</h3>
        <div class="upload-area" @click="triggerUpload">
          <input
            type="file"
            ref="fileInput"
            multiple
            @change="handleFileChange"
            style="display: none"
          />
          <div class="upload-icon">📎</div>
          <div class="upload-text">点击或拖拽文件到此处上传</div>
          <div class="upload-hint">
            支持 png、jpg、gif、txt、log 格式，单个文件不超过 10MB
          </div>
        </div>
        <div class="file-list" v-if="form.attachments.length">
          <div
            class="file-item"
            v-for="(file, index) in form.attachments"
            :key="index"
          >
            <span class="file-icon">📄</span>
            <span class="file-name">{{ file.name }}</span>
            <span class="file-size">{{ formatFileSize(file.size) }}</span>
            <button class="file-remove" @click="removeFile(index)">×</button>
          </div>
        </div>
      </div>

      <div class="form-actions">
        <button class="btn-secondary" @click="$router.back()">取消</button>
        <button class="btn-primary" @click="handleSubmit">保存修改</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted } from "vue";
import { useRoute } from "vue-router";

interface Attachment {
  name: string;
  size: number;
}

export default defineComponent({
  name: "EditBugView",
  setup() {
    const route = useRoute();
    const bugId = route.params.id as string;
    const fileInput = ref<HTMLInputElement | null>(null);

    const projects = ref([
      "电商平台v2.0",
      "小程序优化",
      "支付系统升级",
      "后台管理系统",
    ]);

    const envLabels: Record<string, string> = {
      dev: "开发环境",
      test: "测试环境",
      beta: "Beta环境",
      prod: "线上环境",
    };

    const form = ref({
      title: "用户登录接口返回500错误",
      project: "电商平台v2.0",
      priority: "high",
      type: "bug",
      status: "processing",
      environment: "test",
      version: "v2.0.1",
      module: "用户中心",
      assignee: "张三",
      description: `1. 问题描述\n用户在使用手机号+验证码登录时，系统返回500错误\n\n2. 复现步骤\n- 打开登录页面\n- 输入手机号\n- 点击获取验证码\n- 输入验证码\n- 点击登录\n- 返回500错误\n\n3. 期望结果\n正常登录并跳转到首页\n\n4. 实际结果\n系统返回500 Internal Server Error\n\n5. 后台日志\n[ERROR] Database connection timeout`,
      changeReason: "",
      attachments: [
        { name: "bug_screenshot.png", size: 262144 },
        { name: "error_log.txt", size: 12288 },
      ] as Attachment[],
    });

    onMounted(() => {
      // TODO: 根据 bugId 获取问题详情
      console.log("加载问题:", bugId);
    });

    const triggerUpload = () => {
      fileInput.value?.click();
    };

    const handleFileChange = (event: Event) => {
      const target = event.target as HTMLInputElement;
      if (target.files) {
        for (let i = 0; i < target.files.length; i++) {
          const file = target.files[i];
          form.value.attachments.push({
            name: file.name,
            size: file.size,
          });
        }
      }
    };

    const removeFile = (index: number) => {
      form.value.attachments.splice(index, 1);
    };

    const formatFileSize = (bytes: number) => {
      if (bytes < 1024) return bytes + " B";
      if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + " KB";
      return (bytes / (1024 * 1024)).toFixed(1) + " MB";
    };

    const handleSubmit = () => {
      if (!form.value.title || !form.value.project) {
        alert("请填写必填项");
        return;
      }
      if (!form.value.changeReason) {
        alert("请填写修改原因");
        return;
      }
      console.log("保存修改:", form.value);
      alert("修改保存成功！");
      // TODO: 调用API保存修改
    };

    return {
      bugId,
      fileInput,
      projects,
      envLabels,
      form,
      triggerUpload,
      handleFileChange,
      removeFile,
      formatFileSize,
      handleSubmit,
    };
  },
});
</script>

<style lang="less" scoped>
.edit-bug-page {
  max-width: 900px;
  margin: 0 auto;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 24px;

  .back-btn {
    padding: 8px 16px;
    border: 1px solid #e8e8e8;
    background-color: #fff;
    border-radius: 6px;
    cursor: pointer;

    &:hover {
      border-color: #00c4c4;
      color: #00c4c4;
    }
  }

  h1 {
    font-size: 20px;
    font-weight: 600;
    color: #333;
  }
}

.form-container {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.form-section {
  padding: 24px;
  border-bottom: 1px solid #f0f0f0;

  &:last-of-type {
    border-bottom: none;
  }

  h3 {
    font-size: 16px;
    font-weight: 600;
    color: #333;
    margin-bottom: 20px;
  }
}

.form-row {
  display: flex;
  gap: 20px;
  margin-bottom: 20px;

  &:last-child {
    margin-bottom: 0;
  }
}

.form-item {
  flex: 1;

  &.half {
    flex: 0.5;
  }

  label {
    display: block;
    margin-bottom: 8px;
    font-weight: 500;
    color: #333;

    .required {
      color: #ff4d4f;
    }
  }

  .form-input,
  .form-select,
  .form-textarea {
    width: 100%;
    padding: 10px 12px;
    border: 1px solid #e8e8e8;
    border-radius: 6px;
    font-size: 14px;
    outline: none;

    &:focus {
      border-color: #00c4c4;
    }
  }

  .form-textarea {
    min-height: 120px;
    resize: vertical;
    line-height: 1.6;
  }
}

.upload-area {
  border: 2px dashed #e8e8e8;
  border-radius: 8px;
  padding: 40px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;

  &:hover {
    border-color: #00c4c4;
    background-color: #fafafa;
  }

  .upload-icon {
    font-size: 32px;
    margin-bottom: 12px;
  }

  .upload-text {
    font-size: 14px;
    color: #333;
    margin-bottom: 8px;
  }

  .upload-hint {
    font-size: 12px;
    color: #999;
  }
}

.file-list {
  margin-top: 16px;

  .file-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px;
    background-color: #fafafa;
    border-radius: 6px;
    margin-bottom: 8px;

    .file-icon {
      font-size: 18px;
    }

    .file-name {
      flex: 1;
      font-size: 14px;
      color: #333;
    }

    .file-size {
      font-size: 12px;
      color: #999;
    }

    .file-remove {
      width: 24px;
      height: 24px;
      border: none;
      background: none;
      color: #999;
      font-size: 18px;
      cursor: pointer;

      &:hover {
        color: #ff4d4f;
      }
    }
  }
}

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 24px;
  background-color: #fafafa;
  border-top: 1px solid #f0f0f0;
}

.btn-primary {
  padding: 10px 24px;
  background-color: #00c4c4;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;

  &:hover {
    background-color: #00b0b0;
  }
}

.btn-secondary {
  padding: 10px 24px;
  background-color: #fff;
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  font-size: 14px;
  cursor: pointer;

  &:hover {
    border-color: #00c4c4;
    color: #00c4c4;
  }
}
</style>
