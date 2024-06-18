<template>
  <div class="resource-page">
    <div class="resource-navbar">
      <el-button type="primary" icon="el-icon-picture" @click="redirectToImagePage">图片资源</el-button>
      <el-button type="primary" icon="el-icon-video-camera" @click="redirectToVideoPage">视频资源</el-button>
    </div>
    <div class="resource-list">
      <div class="video-list">
        <div v-for="(url, index) in videoResources" :key="index" class="video-item">
          <video :src="url" controls alt="Video" @click="viewVideo(url)"></video>
        </div>
      </div>
      <el-button class="fixed-add-button" type="primary" icon="el-icon-plus" @click="addVideo"></el-button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { getAccessToken } from "@/utils/auth";

export default {
  data() {
    return {
      videoResources: []
    };
  },
  created() {
    this.fetchVideoResources();
  },
  methods: {
    fetchVideoResources() {
      axios.get('/api/source/videos', {
        headers: {
          'Authorization': 'Bearer ' + getAccessToken(),
        }
      })
        .then(response => {
          console.log(response.data.data)
          // const baseURL = 'http://localhost:8000/video/';
          const baseURL = '/app/video/';
          for (let i = 0; i < response.data.data.length; i++) {
              response.data.data[i] = baseURL + response.data.data[i];
          }
          this.videoResources = response.data.data;
        })
        .catch(error => {
          console.error('Error fetching video resources:', error);
        });

    },
    addVideo() {
      this.$router.push('/source/addVideo');
    },
    viewVideo(url) {
      console.log(url);
    },
    redirectToImagePage() {
      this.$router.push('/source/picture');
    },
    redirectToVideoPage() {
      this.$router.push('/source/video');
    }
  }
};
</script>

<style scoped>
.resource-page {
  background-color: #f4f6f8; /* 浅灰色背景 */
  min-height: 100vh;
  padding: 20px;
}

.resource-navbar {
  display: flex;
  justify-content: flex-start;
  gap: 10px;
  margin-bottom: 20px;
}

.resource-list {
  background-color: #ffffff; /* 白色背景 */
  border: 1px solid #ddd; /* 浅灰色边框 */
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 阴影效果 */
  position: relative;
}

.video-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 一行四列 */
  gap: 20px;
  padding: 10px;
}

.video-item {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.video-item:hover {
  transform: scale(1.05); /* 鼠标悬停放大效果 */
}

.video-item video {
  width: 100%; /* 视频宽度调整 */
  height: auto;
  display: block;
}

.fixed-add-button {
  position: fixed;
  bottom: 20px;
  right: 20px;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  padding: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  transition: background-color 0.3s;
}

.fixed-add-button:hover {
  background-color: #409EFF;
}
</style>
