import { defineConfig } from '@umijs/max';

export default defineConfig({
  antd: {},
  access: {},
  model: {},
  initialState: {},
  request: {},
  layout: {
    title: 'Lite BI',
  },
  routes: [
    {
      path: '/',
      redirect: '/dashboard',
    },
    {
      icon: 'DashboardOutlined',
      name: '仪表板',
      path: '/dashboard',
      component: './Dashboard',
    },
    {
      icon: 'LineChartOutlined',
      name: '图表',
      path: '/chart',
      component: './Chart',
    },
    {
      icon: 'TableOutlined',
      name: '数据表',
      path: '/table',
      component: './Table',
    },
  ],
  npmClient: 'pnpm',
});

