<template>
  <div class="quick-test">
    <div class="page-header">
      <h1>快速测试</h1>
      <div class="header-actions">
        <select class="env-select" v-model="selectedEnv">
          <option value="test">测试环境</option>
          <option value="beta">Beta环境</option>
          <option value="prod">线上环境</option>
        </select>
      </div>
    </div>

    <div class="test-progress">
      <div class="progress-info">
        <span class="progress-label"
          >当前环境：{{ envLabels[selectedEnv] }}</span
        >
        <span class="progress-count"
          >完成 {{ completedCount }} / {{ totalCount }}</span
        >
      </div>
      <div class="progress-bar">
        <div
          class="progress-fill"
          :style="{ width: progressPercent + '%' }"
        ></div>
      </div>
    </div>

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
            <span class="test-project">{{ test.project }}</span>
            <span class="test-assignee">负责人：{{ test.assignee }}</span>
          </div>
        </div>
        <div class="test-status" v-if="test.status === 'passed'">✅</div>
      </div>
    </div>

    <div class="test-detail" v-if="currentTest">
      <div class="detail-header">
        <h3>测试项详情 - #{{ currentTest.bugId }}</h3>
        <span class="test-env-badge">{{ envLabels[selectedEnv] }}</span>
      </div>
      <div class="detail-content">
        <div class="detail-row">
          <span class="detail-label">问题标题：</span>
          <span class="detail-value">{{ currentTest.title }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">所属项目：</span>
          <span class="detail-value">{{ currentTest.project }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">问题描述：</span>
          <span class="detail-value">{{ currentTest.description }}</span>
        </div>
        <div class="detail-row">
          <span class="detail-label">关联附件：</span>
          <span class="detail-value">
            <a href="#" class="attachment-link">📎 bug_screenshot.png</a>
          </span>
        </div>
      </div>
      <div class="detail-actions">
        <div class="action-label">测试结果：</div>
        <div class="action-btns">
          <button
            class="action-btn pass"
            :class="{ active: testResult === 'passed' }"
            @click="testResult = 'passed'"
          >
            ✅ 测试通过
          </button>
          <button
            class="action-btn fail"
            :class="{ active: testResult === 'failed' }"
            @click="testResult = 'failed'"
          >
            ❌ 测试未通过
          </button>
        </div>
        <div class="fail-reason" v-if="testResult === 'failed'">
          <label>未通过原因：</label>
          <textarea
            class="reason-input"
            v-model="failReason"
            placeholder="请描述未通过的原因..."
          ></textarea>
        </div>
      </div>
      <div class="detail-footer">
        <div class="keyboard-hint">
          <span>⌨️ 快捷键：← 上一项 | → 下一项 | P 通过 | F 未通过</span>
        </div>
        <div class="footer-btns">
          <button
            class="btn-secondary"
            @click="prevTest"
            :disabled="currentIndex === 0"
          >
            ← 上一项
          </button>
          <button class="btn-primary" @click="submitTest">
            {{ testResult === "failed" ? "提交未通过" : "提交并下一项" }} →
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, ref, computed, watch } from "vue";

interface TestItem {
  id: number;
  bugId: number;
  title: string;
  project: string;
  assignee: string;
  description: string;
  status: string;
}

export default defineComponent({
  name: "QuickTest",
  setup() {
    const selectedEnv = ref("test");
    const currentIndex = ref(0);
    const testResult = ref<string | null>(null);
    const failReason = ref("");

    const envLabels: Record<string, string> = {
      test: "测试环境",
      beta: "Beta环境",
      prod: "线上环境",
    };

    const testQueue = ref<TestItem[]>([
      {
        id: 1,
        bugId: 154,
        title: "购物车数量更新异常",
        project: "小程序优化",
        assignee: "王五",
        description:
          "在购物车页面，修改商品数量后，页面显示的总数未正确更新，需要刷新页面才能看到正确的数量。",
        status: "pending",
      },
      {
        id: 2,
        bugId: 151,
        title: "用户注册短信验证码发送失败",
        project: "电商平台v2.0",
        assignee: "李四",
        description:
          "新用户注册时，点击发送短信验证码按钮后，提示发送失败，但后台日志显示验证码已发送成功。",
        status: "pending",
      },
      {
        id: 3,
        bugId: 148,
        title: "优惠券列表显示错误",
        project: "电商平台v2.0",
        assignee: "张三",
        description:
          "用户拥有的优惠券在列表页显示异常，部分优惠券显示为空白或重复显示。",
        status: "pending",
      },
      {
        id: 4,
        bugId: 145,
        title: "地址选择器加载缓慢",
        project: "小程序优化",
        assignee: "王五",
        description:
          "在结算页面打开地址选择器时，需要等待3-5秒才能加载完所有地址数据。",
        status: "pending",
      },
    ]);

    const currentTest = computed(() => testQueue.value[currentIndex.value]);

    const completedCount = computed(() => {
      return testQueue.value.filter((t) => t.status === "passed").length;
    });

    const totalCount = computed(() => testQueue.value.length);

    const progressPercent = computed(() => {
      return Math.round((completedCount.value / totalCount.value) * 100);
    });

    const prevTest = () => {
      if (currentIndex.value > 0) {
        currentIndex.value--;
        testResult.value = null;
        failReason.value = "";
      }
    };

    const submitTest = () => {
      if (!testResult.value) {
        alert("请选择测试结果");
        return;
      }
      if (testResult.value === "failed" && !failReason.value) {
        alert("请填写未通过原因");
        return;
      }
      if (testResult.value === "passed") {
        testQueue.value[currentIndex.value].status = "passed";
      }
      if (currentIndex.value < testQueue.value.length - 1) {
        currentIndex.value++;
        testResult.value = null;
        failReason.value = "";
      } else {
        alert("所有测试项已完成！");
      }
    };

    watch(currentIndex, () => {
      testResult.value = null;
      failReason.value = "";
    });

    return {
      selectedEnv,
      currentIndex,
      testResult,
      failReason,
      envLabels,
      testQueue,
      currentTest,
      completedCount,
      totalCount,
      progressPercent,
      prevTest,
      submitTest,
    };
  },
});
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

  .env-select {
    padding: 8px 16px;
    border: 1px solid #e8e8e8;
    border-radius: 6px;
    font-size: 14px;
    background-color: #fff;
    cursor: pointer;
  }
}

.test-progress {
  background-color: #fff;
  border-radius: 8px;
  padding: 16px 20px;
  margin-bottom: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);

  .progress-info {
    display: flex;
    justify-content: space-between;
    margin-bottom: 12px;

    .progress-label {
      font-size: 14px;
      color: #666;
    }

    .progress-count {
      font-size: 14px;
      color: #00c4c4;
      font-weight: 500;
    }
  }

  .progress-bar {
    height: 8px;
    background-color: #f0f0f0;
    border-radius: 4px;
    overflow: hidden;

    .progress-fill {
      height: 100%;
      background-color: #00c4c4;
      border-radius: 4px;
      transition: width 0.3s;
    }
  }
}

.test-list {
  background-color: #fff;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  margin-bottom: 20px;
}

.test-item {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px 20px;
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
    width: 32px;
    height: 32px;
    border-radius: 50%;
    background-color: #f0f0f0;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    font-weight: 600;
    color: #666;
  }

  .test-content {
    flex: 1;

    .test-header {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 6px;

      .test-bug-id {
        color: #00c4c4;
        font-weight: 500;
      }

      .test-title {
        font-size: 14px;
        color: #333;
      }
    }

    .test-meta {
      display: flex;
      gap: 16px;
      font-size: 12px;
      color: #999;
    }
  }

  .test-status {
    font-size: 20px;
  }
}

.test-detail {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  overflow: hidden;
}

.detail-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  border-bottom: 1px solid #f0f0f0;

  h3 {
    font-size: 16px;
    font-weight: 600;
  }

  .test-env-badge {
    padding: 4px 10px;
    background-color: #e6f7ff;
    color: #1890ff;
    border-radius: 4px;
    font-size: 12px;
  }
}

.detail-content {
  padding: 20px;

  .detail-row {
    display: flex;
    margin-bottom: 12px;

    .detail-label {
      width: 100px;
      color: #999;
      font-size: 14px;
      flex-shrink: 0;
    }

    .detail-value {
      flex: 1;
      font-size: 14px;
      color: #333;

      .attachment-link {
        color: #00c4c4;
      }
    }
  }
}

.detail-actions {
  padding: 20px;
  background-color: #fafafa;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;

  .action-label {
    font-weight: 500;
    margin-bottom: 12px;
    color: #333;
  }

  .action-btns {
    display: flex;
    gap: 12px;

    .action-btn {
      flex: 1;
      padding: 12px 20px;
      border: 2px solid #e8e8e8;
      background-color: #fff;
      border-radius: 8px;
      font-size: 14px;
      cursor: pointer;
      transition: all 0.2s;

      &.pass.active {
        border-color: #52c41a;
        background-color: #f6ffed;
        color: #52c41a;
      }

      &.fail.active {
        border-color: #ff4d4f;
        background-color: #fff2f0;
        color: #ff4d4f;
      }
    }
  }

  .fail-reason {
    margin-top: 16px;

    label {
      display: block;
      margin-bottom: 8px;
      font-size: 14px;
      color: #666;
    }

    .reason-input {
      width: 100%;
      padding: 12px;
      border: 1px solid #e8e8e8;
      border-radius: 6px;
      font-size: 14px;
      min-height: 80px;
      resize: vertical;
      outline: none;

      &:focus {
        border-color: #00c4c4;
      }
    }
  }
}

.detail-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;

  .keyboard-hint {
    font-size: 13px;
    color: #999;
  }

  .footer-btns {
    display: flex;
    gap: 12px;
  }
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

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
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

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}
</style>
