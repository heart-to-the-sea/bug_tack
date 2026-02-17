<template>
  <div class="bug-detail">
    <div class="page-header">
      <button class="back-btn" @click="$router.back()">← 返回</button>
      <div class="header-actions">
        <button class="btn-secondary" @click="showEditModal = true">编辑</button>
        <button class="btn-primary" @click="showVerifyModal = true">提交验证</button>
      </div>
    </div>

    <div class="detail-content">
      <div class="main-info">
        <div class="bug-header">
          <span class="bug-id">#{{ bug.id }}</span>
          <h1 class="bug-title">{{ bug.title }}</h1>
        </div>

        <div class="info-section">
          <h3>基本信息</h3>
          <div class="info-grid">
            <div class="info-item">
              <label>状态</label>
              <span class="status-tag" :class="'status-' + bug.status">{{
                statusLabels[bug.status]
              }}</span>
            </div>
            <div class="info-item">
              <label>优先级</label>
              <span class="priority-tag" :class="'priority-' + bug.priority">{{
                priorityLabels[bug.priority]
              }}</span>
            </div>
            <div class="info-item">
              <label>问题类型</label>
              <span>{{ bug.type }}</span>
            </div>
            <div class="info-item">
              <label>项目</label>
              <span>{{ bug.project }}</span>
            </div>
            <div class="info-item">
              <label>负责人</label>
              <span>{{ bug.assignee }}</span>
            </div>
            <div class="info-item">
              <label>创建人</label>
              <span>{{ bug.creator }}</span>
            </div>
            <div class="info-item">
              <label>发现环境</label>
              <span>{{ envLabels[bug.environment] }}</span>
            </div>
            <div class="info-item">
              <label>发现版本</label>
              <span>{{ bug.version }}</span>
            </div>
            <div class="info-item">
              <label>创建时间</label>
              <span>{{ bug.createdAt }}</span>
            </div>
          </div>
        </div>

        <div class="info-section">
          <h3>问题描述</h3>
          <div class="description-content">
            {{ bug.description }}
          </div>
        </div>

        <div class="info-section">
          <h3>
            处理轨迹
            <span class="track-count">共 {{ trackRecords.length }} 条记录</span>
          </h3>
          <div class="track-timeline">
            <div
              class="track-item"
              v-for="(record, index) in trackRecords"
              :key="index"
              :class="{ 'is-reject': record.type === 'reject' }"
            >
              <div class="track-header">
                <div class="track-icon" :class="'track-' + record.type">
                  <template v-if="record.type === 'create'">📝</template>
                  <template v-else-if="record.type === 'assign'">👤</template>
                  <template v-else-if="record.type === 'process'">🔧</template>
                  <template v-else-if="record.type === 'submit'">📤</template>
                  <template v-else-if="record.type === 'verify'">✅</template>
                  <template v-else-if="record.type === 'reject'">❌</template>
                  <template v-else-if="record.type === 'transfer'">🔄</template>
                </div>
                <div class="track-info">
                  <span class="track-action">{{ record.action }}</span>
                  <span class="track-time">{{ record.time }}</span>
                </div>
                <div class="track-user">
                  <span class="user-avatar">{{ record.user.charAt(0) }}</span>
                  <span class="user-name">{{ record.user }}</span>
                </div>
              </div>
              <div class="track-body" v-if="record.content">
                <div class="track-content">{{ record.content }}</div>
                <div class="track-env" v-if="record.environment">
                  环境：{{ envLabels[record.environment] }}
                </div>
              </div>
              <div class="track-attachments" v-if="record.attachments && record.attachments.length">
                <div class="attachment-label">附件：</div>
                <a
                  v-for="(file, idx) in record.attachments"
                  :key="idx"
                  :href="file.url"
                  class="attachment-link"
                >
                  📎 {{ file.name }}
                </a>
              </div>
              <div class="track-reject-info" v-if="record.rejectFrom">
                回退自：{{ envLabels[record.rejectFrom] }} -
                {{ record.rejectReason }}
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="sidebar-info">
        <div class="sidebar-section">
          <h4>快速操作</h4>
          <div class="quick-actions">
            <button class="quick-btn" @click="showTransferModal = true">🔄 转交他人</button>
            <button class="quick-btn" @click="addToTodo">📋 添加到待办</button>
            <button class="quick-btn" @click="showRelatedModal">🔗 关联需求</button>
          </div>
        </div>

        <div class="sidebar-section">
          <h4>验证流程</h4>
          <div class="verify-flow">
            <div
              class="verify-item"
              v-for="(item, index) in verifySteps"
              :key="index"
              :class="{
                completed: item.status === 'completed',
                active: item.status === 'active',
                rejected: item.status === 'rejected',
              }"
            >
              <div class="verify-icon">
                <template v-if="item.status === 'completed'">✅</template>
                <template v-else-if="item.status === 'rejected'">❌</template>
                <template v-else-if="item.status === 'active'">🔵</template>
                <template v-else>⚪</template>
              </div>
              <div class="verify-content">
                <div class="verify-name">{{ item.name }}</div>
                <div class="verify-meta" v-if="item.completedAt">
                  <span>{{ item.operator }}</span>
                  <span>{{ item.completedAt }}</span>
                </div>
                <div class="verify-comment" v-if="item.comment">
                  {{ item.comment }}
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="sidebar-section">
          <h4>关联信息</h4>
          <div class="related-info">
            <div class="related-item">
              <label>关联需求</label>
              <span class="related-link">REQ-2026-001</span>
            </div>
            <div class="related-item">
              <label>所属模块</label>
              <span>用户中心</span>
            </div>
            <div class="related-item">
              <label>影响版本</label>
              <span>v2.0.1</span>
            </div>
          </div>
        </div>

        <div class="sidebar-section">
          <h4>附件列表</h4>
          <div class="attachments">
            <a
              href="#"
              class="attachment-item"
              v-for="(file, index) in bug.attachments"
              :key="index"
            >
              <span class="file-icon">📎</span>
              <span class="file-name">{{ file.name }}</span>
              <span class="file-size">{{ file.size }}</span>
            </a>
          </div>
        </div>
      </div>
    </div>

    <div class="modal-overlay" v-if="showVerifyModal" @click.self="showVerifyModal = false">
      <div class="modal-content">
        <div class="modal-header">
          <h2>提交验证</h2>
          <button class="modal-close" @click="showVerifyModal = false">×</button>
        </div>
        <div class="modal-body">
          <div class="form-item">
            <label>验证环境</label>
            <select class="form-select" v-model="verifyForm.environment">
              <option value="dev">开发环境</option>
              <option value="test">测试环境</option>
              <option value="beta">Beta环境</option>
              <option value="prod">线上环境</option>
            </select>
          </div>
          <div class="form-item">
            <label>验证结果</label>
            <div class="radio-group">
              <label class="radio-item">
                <input type="radio" v-model="verifyForm.result" value="passed" />
                <span class="radio-label pass">✅ 测试通过</span>
              </label>
              <label class="radio-item">
                <input type="radio" v-model="verifyForm.result" value="failed" />
                <span class="radio-label fail">❌ 测试未通过</span>
              </label>
            </div>
          </div>
          <div class="form-item" v-if="verifyForm.result === 'failed'">
            <label>未通过原因 <span class="required">*</span></label>
            <textarea
              class="form-textarea"
              v-model="verifyForm.reason"
              placeholder="请描述未通过的原因..."
            ></textarea>
          </div>
          <div class="form-item">
            <label>备注</label>
            <textarea
              class="form-textarea"
              v-model="verifyForm.comment"
              placeholder="可选..."
            ></textarea>
          </div>
          <div class="form-item">
            <label>上传附件</label>
            <div class="upload-area">
              <input type="file" multiple />
              <span>点击或拖拽上传</span>
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button class="btn-secondary" @click="showVerifyModal = false">取消</button>
          <button class="btn-primary" @click="submitVerify">提交</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref } from 'vue'

interface Attachment {
  name: string
  url?: string
  size?: string
}

interface TrackRecord {
  type: string
  action: string
  user: string
  time: string
  content?: string
  environment?: string
  attachments?: Attachment[]
  rejectFrom?: string
  rejectReason?: string
}

interface VerifyStep {
  name: string
  status: string
  operator?: string
  completedAt?: string
  comment?: string
}

export default defineComponent({
  name: 'BugDetailView',
  setup() {
    const showEditModal = ref(false)
    const showVerifyModal = ref(false)
    const showTransferModal = ref(false)
    const showRelatedModal = ref(false)

    const statusLabels: Record<string, string> = {
      pending: '待分配',
      processing: '处理中',
      testing: '待验证',
      verified: '已验证',
      completed: '已修复',
      rejected: '未通过',
    }

    const priorityLabels: Record<string, string> = {
      high: '高',
      medium: '中',
      low: '低',
    }

    const envLabels: Record<string, string> = {
      dev: '开发环境',
      test: '测试环境',
      beta: 'Beta环境',
      prod: '线上环境',
    }

    const bug = ref({
      id: 156,
      title: '用户登录接口返回500错误',
      status: 'testing',
      priority: 'high',
      type: 'Bug',
      project: '电商平台v2.0',
      assignee: '张三',
      creator: '李四',
      environment: 'test',
      version: 'v2.0.1',
      createdAt: '2026-02-10 10:30',
      description: `1. 问题描述\n用户在使用手机号+验证码登录时，系统返回500错误\n\n2. 复现步骤\n- 打开登录页面\n- 输入手机号\n- 点击获取验证码\n- 输入验证码\n- 点击登录\n- 返回500错误\n\n3. 期望结果\n正常登录并跳转到首页\n\n4. 实际结果\n系统返回500 Internal Server Error\n\n5. 后台日志\n[ERROR] Database connection timeout`,
      attachments: [
        { name: 'bug_screenshot.png', size: '256KB' },
        { name: 'error_log.txt', size: '12KB' },
      ],
    })

    const trackRecords = ref<TrackRecord[]>([
      {
        type: 'create',
        action: '创建问题',
        user: '李四',
        time: '2026-02-10 10:30',
        content: '用户反馈登录失败，请尽快处理',
        environment: 'test',
      },
      {
        type: 'assign',
        action: '分配问题',
        user: '李四',
        time: '2026-02-10 10:35',
        content: '分配给张三处理',
      },
      {
        type: 'process',
        action: '开始处理',
        user: '张三',
        time: '2026-02-10 11:00',
        content: '已定位问题原因：数据库连接池配置过小，高峰期连接不够',
      },
      {
        type: 'process',
        action: '处理中',
        user: '张三',
        time: '2026-02-10 14:00',
        content: '正在调整数据库连接池配置，从50调整到100',
      },
      {
        type: 'submit',
        action: '提交修复',
        user: '张三',
        time: '2026-02-10 14:20',
        content: '已优化数据库连接池配置，增加连接数上限',
        environment: 'dev',
      },
      {
        type: 'verify',
        action: '开发环境验证通过',
        user: '王五',
        time: '2026-02-10 15:00',
        content: '开发环境验证通过，未发现异常',
      },
      {
        type: 'submit',
        action: '提交测试环境验证',
        user: '张三',
        time: '2026-02-10 15:30',
        content: '请在测试环境验证',
        environment: 'test',
      },
      {
        type: 'reject',
        action: '测试环境验证未通过',
        user: '测试小王',
        time: '2026-02-10 16:00',
        content: '高峰期仍有问题，连接超时错误仍然存在',
        environment: 'test',
        rejectFrom: 'test',
        rejectReason: '高峰期仍偶发500错误，需要进一步优化',
      },
      {
        type: 'process',
        action: '继续处理',
        user: '张三',
        time: '2026-02-10 16:30',
        content: '增加连接池最小连接数配置，调整为20-150',
      },
    ])

    const verifySteps = ref<VerifyStep[]>([
      {
        name: '开发环境验证',
        status: 'completed',
        operator: '王五',
        completedAt: '2026-02-10 15:00',
        comment: '通过',
      },
      {
        name: '测试环境验证',
        status: 'rejected',
        operator: '测试小王',
        completedAt: '2026-02-10 16:00',
        comment: '高峰期仍有问题',
      },
      { name: 'Beta环境验证', status: 'pending', comment: '' },
      { name: '线上环境验证', status: 'pending', comment: '' },
    ])

    const verifyForm = ref({
      environment: 'test',
      result: 'passed',
      reason: '',
      comment: '',
    })

    const addToTodo = () => {
      console.log('添加到待办')
    }

    const submitVerify = () => {
      console.log('提交验证:', verifyForm.value)
      showVerifyModal.value = false
    }

    return {
      showEditModal,
      showVerifyModal,
      showTransferModal,
      showRelatedModal,
      statusLabels,
      priorityLabels,
      envLabels,
      bug,
      trackRecords,
      verifySteps,
      verifyForm,
      addToTodo,
      submitVerify,
    }
  },
})
</script>

<style lang="less" scoped>
.bug-detail {
  width: 100%;
  margin: 0 auto;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
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

  .header-actions {
    display: flex;
    gap: 12px;
  }
}

.btn-primary {
  padding: 10px 20px;
  background-color: #00c4c4;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;

  &:hover {
    background-color: #00b0b0;
  }
}

.btn-secondary {
  padding: 10px 20px;
  background-color: #fff;
  border: 1px solid #e8e8e8;
  border-radius: 6px;
  cursor: pointer;

  &:hover {
    border-color: #00c4c4;
    color: #00c4c4;
  }
}

.detail-content {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 24px;
}

.main-info {
  background-color: #fff;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.bug-header {
  margin-bottom: 24px;

  .bug-id {
    color: #00c4c4;
    font-weight: 600;
    margin-right: 12px;
  }

  .bug-title {
    display: inline;
    font-size: 20px;
    font-weight: 600;
    color: #333;
  }
}

.info-section {
  margin-bottom: 24px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }

  h3 {
    display: flex;
    align-items: center;
    font-size: 16px;
    font-weight: 600;
    margin-bottom: 16px;
    color: #333;

    .track-count {
      font-size: 12px;
      font-weight: normal;
      color: #999;
      margin-left: 12px;
    }
  }
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 20px;

  .info-item {
    label {
      display: block;
      font-size: 12px;
      color: #999;
      margin-bottom: 4px;
    }

    span {
      font-size: 14px;
      color: #333;
    }
  }
}

.status-tag,
.priority-tag {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 4px;
  font-size: 12px;
}

.status-pending {
  background-color: #fff7e6;
  color: #d46b08;
}

.status-processing {
  background-color: #e6f7ff;
  color: #1890ff;
}

.status-testing {
  background-color: #f9f0ff;
  color: #722ed1;
}

.status-verified {
  background-color: #e6fffb;
  color: #13c2c2;
}

.status-completed {
  background-color: #f6ffed;
  color: #52c41a;
}

.status-rejected {
  background-color: #fff2f0;
  color: #ff4d4f;
}

.priority-high {
  background-color: #fff2f0;
  color: #ff4d4f;
}

.priority-medium {
  background-color: #fff7e6;
  color: #d46b08;
}

.priority-low {
  background-color: #f6ffed;
  color: #52c41a;
}

.description-content {
  font-size: 14px;
  line-height: 1.8;
  color: #333;
  white-space: pre-wrap;
}

.track-timeline {
  .track-item {
    position: relative;
    padding-left: 40px;
    padding-bottom: 20px;

    &:not(:last-child)::before {
      content: '';
      position: absolute;
      left: 15px;
      top: 32px;
      width: 2px;
      height: calc(100% - 32px);
      background-color: #e8e8e8;
    }

    &.is-reject {
      .track-header {
        background-color: #fff2f0;
        border-radius: 6px;
        padding: 12px;
        margin: -12px;
      }
    }

    .track-header {
      display: flex;
      align-items: center;
      gap: 12px;
      margin-bottom: 8px;

      .track-icon {
        width: 32px;
        height: 32px;
        border-radius: 50%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 16px;
        position: absolute;
        left: 0;
        top: 0;
      }

      .track-create {
        background-color: #e6f7ff;
      }

      .track-assign {
        background-color: #fff7e6;
      }

      .track-process {
        background-color: #f6ffed;
      }

      .track-submit {
        background-color: #f9f0ff;
      }

      .track-verify {
        background-color: #e6fffb;
      }

      .track-reject {
        background-color: #fff2f0;
      }

      .track-transfer {
        background-color: #fff1f0;
      }

      .track-info {
        flex: 1;

        .track-action {
          font-weight: 500;
          color: #333;
          margin-right: 12px;
        }

        .track-time {
          font-size: 12px;
          color: #999;
        }
      }

      .track-user {
        display: flex;
        align-items: center;
        gap: 8px;

        .user-avatar {
          width: 24px;
          height: 24px;
          border-radius: 50%;
          background-color: #00c4c4;
          color: #fff;
          display: flex;
          align-items: center;
          justify-content: center;
          font-size: 12px;
        }

        .user-name {
          font-size: 13px;
          color: #666;
        }
      }
    }

    .track-body {
      background-color: #fafafa;
      border-radius: 6px;
      padding: 12px;
      margin-top: 8px;

      .track-content {
        font-size: 14px;
        color: #333;
        line-height: 1.6;
      }

      .track-env {
        font-size: 12px;
        color: #1890ff;
        margin-top: 8px;
      }
    }

    .track-attachments {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-top: 8px;
      flex-wrap: wrap;

      .attachment-label {
        font-size: 12px;
        color: #999;
      }

      .attachment-link {
        font-size: 12px;
        color: #00c4c4;

        &:hover {
          text-decoration: underline;
        }
      }
    }

    .track-reject-info {
      background-color: #fff2f0;
      color: #ff4d4f;
      font-size: 12px;
      padding: 8px 12px;
      border-radius: 4px;
      margin-top: 8px;
    }
  }
}

.sidebar-info {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.sidebar-section {
  background-color: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  h4 {
    font-size: 14px;
    font-weight: 600;
    margin-bottom: 16px;
    color: #333;
  }
}

.quick-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;

  .quick-btn {
    padding: 10px 12px;
    border: 1px solid #e8e8e8;
    background-color: #fff;
    border-radius: 6px;
    text-align: left;
    cursor: pointer;
    font-size: 14px;
    transition: all 0.2s;

    &:hover {
      border-color: #00c4c4;
      color: #00c4c4;
    }
  }
}

.verify-flow {
  .verify-item {
    display: flex;
    gap: 12px;
    padding: 12px 0;
    border-bottom: 1px solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .verify-icon {
      width: 24px;
      height: 24px;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 14px;
    }

    .verify-content {
      flex: 1;

      .verify-name {
        font-size: 13px;
        color: #333;
        margin-bottom: 4px;
      }

      .verify-meta {
        display: flex;
        justify-content: space-between;
        font-size: 12px;
        color: #999;
      }

      .verify-comment {
        font-size: 12px;
        color: #ff4d4f;
        margin-top: 4px;
      }
    }

    &.completed .verify-icon {
      color: #52c41a;
    }

    &.active .verify-icon {
      color: #1890ff;
    }

    &.rejected .verify-icon {
      color: #ff4d4f;
    }
  }
}

.related-info {
  .related-item {
    display: flex;
    justify-content: space-between;
    padding: 8px 0;
    border-bottom: 1px solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    label {
      font-size: 13px;
      color: #999;
    }

    span {
      font-size: 13px;
      color: #333;
    }

    .related-link {
      color: #00c4c4;
      cursor: pointer;

      &:hover {
        text-decoration: underline;
      }
    }
  }
}

.attachments {
  .attachment-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 0;
    border-bottom: 1px solid #f5f5f5;

    &:last-child {
      border-bottom: none;
    }

    .file-icon {
      font-size: 16px;
    }

    .file-name {
      flex: 1;
      font-size: 13px;
      color: #00c4c4;

      &:hover {
        text-decoration: underline;
      }
    }

    .file-size {
      font-size: 12px;
      color: #999;
    }
  }
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  width: 90%;
  max-width: 500px;
  background-color: #fff;
  border-radius: 12px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid #f0f0f0;

  h2 {
    font-size: 18px;
    font-weight: 600;
  }

  .modal-close {
    width: 32px;
    height: 32px;
    border: none;
    background: none;
    font-size: 24px;
    cursor: pointer;
    color: #999;

    &:hover {
      color: #333;
    }
  }
}

.modal-body {
  padding: 24px;
}

.form-item {
  margin-bottom: 20px;

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
    min-height: 80px;
    resize: vertical;
  }
}

.radio-group {
  display: flex;
  gap: 20px;

  .radio-item {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;

    input[type='radio'] {
      width: 18px;
      height: 18px;
    }

    .radio-label {
      font-size: 14px;

      &.pass {
        color: #52c41a;
      }

      &.fail {
        color: #ff4d4f;
      }
    }
  }
}

.upload-area {
  border: 2px dashed #e8e8e8;
  border-radius: 6px;
  padding: 24px;
  text-align: center;
  cursor: pointer;
  position: relative;

  &:hover {
    border-color: #00c4c4;
  }

  input[type='file'] {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    opacity: 0;
    cursor: pointer;
  }

  span {
    font-size: 14px;
    color: #999;
  }
}

.modal-footer {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding: 20px 24px;
  border-top: 1px solid #f0f0f0;
}
</style>
