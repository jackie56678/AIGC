<template>
    <div :class="['sider', { collapsed: isCollapsed }]">
      <el-menu
        :collapse="isCollapsed"
        :default-active="$route.path"
        class="el-menu-vertical-demo"
        background-color="#1a202c"
        text-color="#cbd5e0"
        active-text-color="#68d391"
      >
        <el-menu-item index="/profile/info" @click="handleMenuClick('/profile')">
          <i class="el-icon-house"></i>
          <span slot="title">个人主页</span>
        </el-menu-item>
        <el-menu-item index="/relative/info" @click="handleMenuClick('/relative/info')">
          <i class="el-icon-menu"></i>
          <span slot="title">亲人信息</span>
        </el-menu-item>
        <el-menu-item index="/source/picture" @click="handleMenuClick('/source/picture')">
          <i class="el-icon-picture"></i>
          <span slot="title">资源展示</span>
        </el-menu-item>
        <el-menu-item index="/ai/chat" @click="handleMenuClick('/ai/chat')">
          <i class="el-icon-chat-square"></i>
          <span slot="title">与AI聊天</span>
        </el-menu-item>
        <el-menu-item index="/health" @click="handleMenuClick('/health')">
          <i class="el-icon-s-claim"></i>
          <span slot="title">养生知识</span>
        </el-menu-item>
        <el-menu-item index="/todo/list" @click="handleMenuClick('/todo/list')">
          <i class="el-icon-notebook-1"></i>
          <span slot="title">语音记事本</span>
        </el-menu-item>
        <el-menu-item index="/logout" @click="logout">
          <i class="el-icon-switch-button"></i>
          <span slot="title">退出</span>
        </el-menu-item>
      </el-menu>
    </div>
  </template>
  
  <script>
  import { removeToken } from "@/utils/auth";
  
  export default {
    name: "Sider",
    props: {
      isCollapsed: {
        type: Boolean,
        required: true
      }
    },
    methods: {
      handleMenuClick(path) {
        if (this.$route.path !== path) {
          this.$router.push(path);
        }
      },
      logout() {
        this.$confirm("确定注销并退出系统吗？", "提示").then(() => {
          removeToken();
          this.$router.push({ path: "/" }); // Redirect to login page
        }).catch(() => {});
      }
    }
  };
  </script>
  
  <style lang="scss" scoped>
  .sider {
    width: 220px;
    height: 100%;
    background-color: #1a202c;
    position: fixed;
    left: 0;
    top: 0;
    overflow-x: hidden;
    transition: width 0.3s;
    z-index: 1000;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  }
  
  .sider.collapsed {
    width: 60px;
  }
  
  .el-menu-vertical-demo {
    background-color: transparent !important;
    border: none !important;
    margin-top: 20px;
    transition: all 0.3s;
    width: auto;
  }
  
  .el-menu-vertical-demo .el-menu-item {
    height: 50px;
    line-height: 50px;
  }
  
  .el-menu-vertical-demo .el-menu-item:hover {
    background-color: #2d3748 !important;
  }
  
  .el-menu-vertical-demo .el-menu-item .el-icon {
    margin-right: 10px;
  }
  
  .el-menu-vertical-demo .el-submenu__title .el-icon-arrow-down {
    float: right;
  }
  </style>
  