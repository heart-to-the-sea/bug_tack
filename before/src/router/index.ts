import { createRouter, createWebHistory, RouteRecordRaw } from "vue-router";

const routes: Array<RouteRecordRaw> = [
  {
    path: "/",
    name: "dashboard",
    component: () => import("../views/dashboard/Dashboard.vue"),
  },
  {
    path: "/bugs",
    name: "bugs",
    component: () => import("../views/bug/BugList.vue"),
  },
  {
    path: "/bugs/create",
    name: "create-bug",
    component: () => import("../views/bug/CreateBug.vue"),
  },
  {
    path: "/bugs/:id",
    name: "bug-detail",
    component: () => import("../views/bug/BugDetail.vue"),
  },
  {
    path: "/bugs/:id/edit",
    name: "edit-bug",
    component: () => import("../views/bug/EditBug.vue"),
  },
  {
    path: "/projects",
    name: "projects",
    component: () => import("../views/project/ProjectList.vue"),
  },
  {
    path: "/test",
    name: "test",
    component: () => import("../views/test/QuickTest.vue"),
  },
  {
    path: "/todo",
    name: "todo",
    component: () => import("../views/todo/TodoList.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;
