<template>
  <div class="main-layout">
    <aside class="sidebar">
      <div class="logo">
        <span class="logo-icon">🐛</span>
        <span class="logo-text">Bug Track</span>
      </div>
      <nav class="nav-menu">
        <router-link to="/" class="nav-item">
          <span class="nav-icon">📊</span>
          <span class="nav-text">仪表盘</span>
        </router-link>
        <router-link to="/bugs" class="nav-item">
          <span class="nav-icon">📝</span>
          <span class="nav-text">问题管理</span>
        </router-link>
        <router-link to="/projects" class="nav-item">
          <span class="nav-icon">📁</span>
          <span class="nav-text">项目管理</span>
        </router-link>
        <router-link to="/test" class="nav-item">
          <span class="nav-icon">🧪</span>
          <span class="nav-text">快速测试</span>
        </router-link>
        <router-link to="/todo" class="nav-item">
          <span class="nav-icon">📋</span>
          <span class="nav-text">我的待办</span>
        </router-link>
      </nav>
      <div class="sidebar-footer">
        <div class="user-info">
          <div class="user-avatar">张</div>
          <div class="user-details">
            <span class="user-name">张三</span>
            <span class="user-role">开发人员</span>
          </div>
        </div>
      </div>
    </aside>
    <main class="main-content">
      <header class="top-header">
        <div class="header-left">
          <h1 class="page-title">{{ currentPageTitle }}</h1>
        </div>
        <div class="header-right">
          <button class="header-btn">🔔</button>
          <button class="header-btn">⚙️</button>
        </div>
      </header>
      <div class="page-wrapper">
        <router-view />
      </div>
    </main>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from "vue";
import { useRoute } from "vue-router";

export default defineComponent({
  name: "MainLayout",
  setup() {
    const route = useRoute();

    const pageTitles: Record<string, string> = {
      "/": "仪表盘",
      "/bugs": "问题管理",
      "/projects": "项目管理",
      "/test": "快速测试",
      "/todo": "我的待办",
    };

    const currentPageTitle = computed(() => {
      return pageTitles[route.path] || "Bug Track";
    });

    return {
      currentPageTitle,
    };
  },
});
</script>

<style lang="less" scoped>
.main-layout {
  display: flex;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.sidebar {
  width: 240px;
  background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
  display: flex;
  flex-direction: column;
  position: fixed;
  height: 100vh;
  z-index: 100;
}

.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);

  .logo-icon {
    font-size: 28px;
  }

  .logo-text {
    font-size: 18px;
    font-weight: 600;
    color: #fff;
  }
}

.nav-menu {
  flex: 1;
  padding: 16px 0;
  overflow-y: auto;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 20px;
  color: rgba(255, 255, 255, 0.7);
  text-decoration: none;
  transition: all 0.2s;

  &:hover {
    background-color: rgba(255, 255, 255, 0.1);
    color: #fff;
  }

  &.router-link-exact-active {
    background-color: rgba(0, 196, 196, 0.2);
    color: #00c4c4;
    border-right: 3px solid #00c4c4;
  }

  .nav-icon {
    font-size: 18px;
  }

  .nav-text {
    font-size: 14px;
  }
}

.sidebar-footer {
  padding: 16px 20px;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;

  .user-avatar {
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background-color: #00c4c4;
    color: #fff;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    font-weight: 600;
  }

  .user-details {
    display: flex;
    flex-direction: column;

    .user-name {
      color: #fff;
      font-size: 14px;
      font-weight: 500;
    }

    .user-role {
      color: rgba(255, 255, 255, 0.5);
      font-size: 12px;
    }
  }
}

.main-content {
  flex: 1;
  margin-left: 240px;
  display: flex;
  flex-direction: column;
}

.top-header {
  height: 60px;
  background-color: #fff;
  border-bottom: 1px solid #e8e8e8;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  position: sticky;
  top: 0;
  z-index: 50;

  .header-left {
    .page-title {
      font-size: 18px;
      font-weight: 600;
      color: #333;
    }
  }

  .header-right {
    display: flex;
    gap: 8px;

    .header-btn {
      width: 36px;
      height: 36px;
      border: none;
      background: none;
      border-radius: 6px;
      cursor: pointer;
      font-size: 16px;
      transition: background-color 0.2s;

      &:hover {
        background-color: #f5f5f5;
      }
    }
  }
}

.page-wrapper {
  flex: 1;
  padding: 24px;
  overflow-y: auto;
}
</style>
