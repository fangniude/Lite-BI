import { defineConfig } from '@umijs/max';
 
export default defineConfig({
  layout: {
    title: 'Ant Design',
    locale: false, // 默认开启，如无需菜单国际化可关闭
    siderWidth: 100,
    childrenLayout: {
        siderWidth: 208, // 修改侧边菜单栏宽度
    },
  },
});