<template>
  <MainLayout>
    <router-view />
  </MainLayout>
</template>

<script lang="ts">
import { defineComponent } from "vue";
import MainLayout from "./layouts/MainLayout.vue";

export default defineComponent({
  name: "App",
  components: {
    MainLayout,
  },
});
</script>

<style lang="less">
@import "./assets/styles/global.less";
</style>
