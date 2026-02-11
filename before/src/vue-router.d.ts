declare module "vue-router" {
  import { App, ComputedRef, Plugin } from "vue";

  interface RouteRecordRaw {
    path: string;
    name?: string;
    component?: any;
    components?: Record<string, any>;
    children?: RouteRecordRaw[];
    alias?: string | string[];
    props?: Record<string, any> | boolean | ((to: Route) => any);
    beforeEnter?: NavigationGuard | NavigationGuard[];
    meta?: any;
  }

  interface RouteRecord {
    path: string;
    name?: string;
    components: Record<string, any>;
    children: RouteRecordRaw[];
    alias: string[];
    props: Record<string, any>;
    beforeEnter: NavigationGuard[];
    meta: any;
    instances: Record<string, any>;
  }

  interface Route {
    path: string;
    name?: string;
    hash: string;
    query: Record<string, string | (string | null)[]>;
    params: Record<string, string | string[]>;
    fullPath: string;
    matched: RouteRecord[];
    redirectedFrom?: string;
    meta?: any;
  }

  interface Location {
    name?: string;
    path?: string;
    hash?: string;
    query?: Record<string, string | (string | null)[] | null | undefined>;
    params?: Record<string, string | string[]>;
    append?: boolean;
    replace?: boolean;
  }

  interface RouterOptions {
    routes: RouteRecordRaw[];
    history: RouterHistory;
    base?: string;
  }

  interface NavigationGuard {
    (
      to: Route,
      from: Route,
      next: (to?: Location | ((to: Route) => void) | void) => void
    ): void;
  }

  class RouterHistory {
    push(location: Location): void;
    replace(location: Location): void;
    go(delta: number): void;
    back(): void;
    forward(): void;
  }

  class Router implements Plugin {
    install(app: App, ...options: any[]): void;
    push(location: Location): Promise<Route>;
    replace(location: Location): Promise<Route>;
    go(n: number): void;
    back(): void;
    forward(): void;
    beforeEach(guard: NavigationGuard): this;
    afterEach(hook: (to: Route, from: Route) => void): this;
    beforeResolve(guard: NavigationGuard): this;
    currentRoute: ComputedRef<Route>;
    addRoute(route: RouteRecordRaw): void;
    addRoutes(routes: RouteRecordRaw[]): void;
    removeRoute(name: string): void;
    hasRoute(name: string): boolean;
    getRoutes(): RouteRecord[];
    resolve(to: Location): {
      location: Location;
      route: Route;
      href: string;
      normalizedTo: Location;
    };
  }

  function createRouter(options: RouterOptions): Router;
  function useRouter(): Router;
  function useRoute(): Route;
  function createWebHistory(base?: string): RouterHistory;
  function createWebHashHistory(base?: string): RouterHistory;
  function createMemoryHistory(base?: string): RouterHistory;
}
