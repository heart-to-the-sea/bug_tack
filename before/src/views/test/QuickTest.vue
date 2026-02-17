<!--
  快速测试页面
  V1.2 变量版本号: v1.2.0
  V1.2 方法版本号: v1.2.0
-->
<template>
  <div class="quick-test">
    <div class="page-header">
      <h1>快速测试</h1>
      <div class="header-actions">
        <el-switch v-model="autoJump" active-text="自动跳转" inactive-text="手动提交" />
        <el-select v-model="selectedEnv" style="width: 140px">
          <el-option label="测试环境" value="test" />
          <el-option label="Beta环境" value="beta" />
          <el-option label="线上环境" value="prod" />
        </el-select>
      </div>
    </div>

    <el-card class="test-progress" shadow="never">
      <div class="progress-info">
        <span>当前环境：{{ envLabels[selectedEnv] }}</span>
        <span class="progress-count">完成 {{ completedCount }} / {{ totalCount }}</span>
      </div>
      <el-progress :percentage="progressPercent" :stroke-width="8" />
    </el-card>

    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="test-list-card" shadow="never">
          <template #header>
            <span>测试队列</span>
          </template>
          <div class="test-list">
            <div
              class="test-item"
              v-for="(test, index) in testQueue"
              :key="test.id"
              :class="{
                active: currentIndex === index,
                completed: test.status === 'passed',
              }"
              @click="currentIndex = index"
            >
              <div class="test-number">{{ index + 1 }}</div>
              <div class="test-content">
                <div class="test-header">
                  <span class="test-bug-id">#{{ test.bugId }}</span>
                  <span class="test-title">{{ test.title }}</span>
                </div>
                <div class="test-meta">
                  <span>{{ test.project }}</span>
                </div>
              </div>
              <el-icon v-if="test.status === 'passed'" class="test-status"><Check /></el-icon>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="16" v-if="currentTest">
        <el-card class="test-detail" shadow="never">
          <template #header>
            <div class="detail-header">
              <span>测试项详情 - #{{ currentTest.bugId }}</span>
              <el-tag type="primary">{{ envLabels[selectedEnv] }}</el-tag>
            </div>
          </template>

          <el-descriptions :column="1" border>
            <el-descriptions-item label="问题标题">{{ currentTest.title }}</el-descriptions-item>
            <el-descriptions-item label="所属项目">{{ currentTest.project }}</el-descriptions-item>
            <el-descriptions-item label="问题描述">{{
              currentTest.description
            }}</el-descriptions-item>
            <el-descriptions-item label="关联附件">
              <el-link type="primary">📎 bug_screenshot.png</el-link>
            </el-descriptions-item>
          </el-descriptions>

          <div class="detail-actions">
            <div class="action-label">测试结果：</div>
            <el-radio-group v-model="testResult">
              <el-radio-button label="passed">
                <el-icon><Check /></el-icon>
                测试通过
              </el-radio-button>
              <el-radio-button label="failed">
                <el-icon><Close /></el-icon>
                测试未通过
              </el-radio-button>
            </el-radio-group>

            <div v-if="testResult === 'failed'" class="fail-reason">
              <span>未通过原因：</span>
              <el-input
                v-model="failReason"
                type="textarea"
                :rows="3"
                placeholder="请描述未通过的原因..."
              />
            </div>
          </div>

          <div class="detail-footer">
            <div class="keyboard-hint">
              <span>快捷键：← 上一项 | → 下一项 | P 通过 | F 未通过</span>
            </div>
            <div class="footer-btns">
              <el-button :disabled="currentIndex === 0" @click="prevTest">← 上一项</el-button>
              <el-button type="primary" @click="submitTest">
                {{ testResult === 'failed' ? '提交未通过' : '提交并下一项' }} →
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch } from 'vue' // V1.2.0
import { ElMessage } from 'element-plus' // V1.2.0
import { Check, Close } from '@element-plus/icons-vue' // V1.2.0

interface TestItem {
  // V1.2.0
  id: number // V1.2.0
  bugId: number // V1.2.0
  title: string // V1.2.0
  project: string // V1.2.0
  assignee: string // V1.2.0
  description: string // V1.2.0
  status: string // V1.2.0
} // V1.2.0

export default defineComponent({
  name: 'QuickTest',
  components: {
    Check,
    Close,
  },
  setup() {
    const selectedEnv = ref('test') // V1.2.0
    const currentIndex = ref(0) // V1.2.0
    const testResult = ref<string | null>(null) // V1.2.0
    const failReason = ref('') // V1.2.0
    const autoJump = ref(true) // V1.2.0 自动跳转开关

    const envLabels: Record<string, string> = {
      // V1.2.0
      test: '测试环境', // V1.2.0
      beta: 'Beta环境', // V1.2.0
      prod: '线上环境', // V1.2.0
    }

    const testQueue = ref<TestItem[]>([
      // V1.2.0
      {
        id: 1,
        bugId: 154,
        title: '购物车数量更新异常',
        project: '小程序优化',
        assignee: '王五',
        description:
          '在购物车页面，修改商品数量后，页面显示的总数未正确更新，需要刷新页面才能看到正确的数量。',
        status: 'pending',
      },
      {
        id: 2,
        bugId: 151,
        title: '用户注册短信验证码发送失败',
        project: '电商平台v2.0',
        assignee: '李四',
        description:
          '新用户注册时，点击发送短信验证码按钮后，提示发送失败，但后台日志显示验证码已发送成功。',
        status: 'pending',
      },
      {
        id: 3,
        bugId: 148,
        title: '优惠券列表显示错误',
        project: '电商平台v2.0',
        assignee: '张三',
        description: '用户拥有的优惠券在列表页显示异常，部分优惠券显示为空白或重复显示。',
        status: 'pending',
      },
      {
        id: 4,
        bugId: 145,
        title: '地址选择器加载缓慢',
        project: '小程序优化',
        assignee: '王五',
        description: '在结算页面打开地址选择器时，需要等待3-5秒才能加载完所有地址数据。',
        status: 'pending',
      },
    ]) // V1.2.0

    const currentTest = computed(() => testQueue.value[currentIndex.value])

    const completedCount = computed(() => {
      return testQueue.value.filter((t) => t.status === 'passed').length
    })

    const totalCount = computed(() => testQueue.value.length)

    const progressPercent = computed(() => {
      return totalCount.value > 0 ? Math.round((completedCount.value / totalCount.value) * 100) : 0
    })

    const prevTest = () => {
      if (currentIndex.value > 0) {
        currentIndex.value--
        testResult.value = null
        failReason.value = ''
      }
    }

    const submitTest = () => {
      if (!testResult.value) {
        ElMessage.warning('请选择测试结果')
        return
      }
      if (testResult.value === 'failed' && !failReason.value) {
        ElMessage.warning('请填写未通过原因')
        return
      }
      if (testResult.value === 'passed') {
        testQueue.value[currentIndex.value].status = 'passed'
        if (autoJump.value) {
          if (currentIndex.value < testQueue.value.length - 1) {
            currentIndex.value++
            testResult.value = null
            failReason.value = ''
          } else {
            ElMessage.success('所有测试项已完成！')
          }
        } else {
          if (currentIndex.value < testQueue.value.length - 1) {
            currentIndex.value++
            testResult.value = null
            failReason.value = ''
          } else {
            ElMessage.success('所有测试项已完成！')
          }
        }
      } else {
        ElMessage.info('测试未通过，请修改后重新提交')
      }
    }

    watch(currentIndex, () => {
      testResult.value = null
      failReason.value = ''
    })

    return {
      selectedEnv,
      currentIndex,
      testResult,
      failReason,
      autoJump,
      envLabels,
      testQueue,
      currentTest,
      completedCount,
      totalCount,
      progressPercent,
      prevTest,
      submitTest,
    }
  },
})
</script>

<style lang="less" scoped>
.quick-test {
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

.test-progress {
  margin-bottom: 20px;

  .progress-info {
    display: flex;
    justify-content: space-between;
    margin-bottom: 12px;
    font-size: 14px;
    color: #666;

    .progress-count {
      color: #00c4c4;
      font-weight: 500;
    }
  }
}

.test-list-card {
  height: calc(100vh - 280px);
  overflow-y: auto;

  .test-list {
    max-height: calc(100vh - 340px);
    overflow-y: auto;
  }
}

.test-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background-color 0.2s;

  &:last-child {
    border-bottom: none;
  }

  &:hover {
    background-color: #fafafa;
  }

  &.active {
    background-color: #e6f7ff;
    border-left: 3px solid #00c4c4;
  }

  &.completed {
    opacity: 0.6;
  }

  .test-number {
    width: 28px;
    height: 28px;
    border-radius: 50%;
    background-color: #f0f0f0;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    font-weight: 600;
    color: #666;
    flex-shrink: 0;
  }

  .test-content {
    flex: 1;
    min-width: 0;

    .test-header {
      display: flex;
      align-items: center;
      gap: 8px;
      margin-bottom: 4px;

      .test-bug-id {
        color: #00c4c4;
        font-weight: 500;
        font-size: 13px;
        flex-shrink: 0;
      }

      .test-title {
        font-size: 13px;
        color: #333;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }

    .test-meta {
      font-size: 12px;
      color: #999;
    }
  }

  .test-status {
    color: #52c41a;
    font-size: 18px;
    flex-shrink: 0;
  }
}

.test-detail {
  .detail-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-weight: 600;
  }

  .detail-actions {
    margin-top: 20px;
    padding: 20px;
    background-color: #fafafa;
    border-radius: 8px;

    .action-label {
      font-weight: 500;
      margin-bottom: 16px;
      color: #333;
    }

    .fail-reason {
      margin-top: 16px;

      span {
        display: block;
        margin-bottom: 8px;
        font-size: 14px;
        color: #666;
      }
    }
  }

  .detail-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;

    .keyboard-hint {
      font-size: 13px;
      color: #999;
    }

    .footer-btns {
      display: flex;
      gap: 12px;
    }
  }
}
</style>
