<template>
  <div class="resource-page">
    <div class="resource-navbar">
      <el-button type="primary" icon="el-icon-picture" @click="redirectToImagePage">图片资源</el-button>
      <el-button type="primary" icon="el-icon-video-camera" @click="redirectToVideoPage">视频资源</el-button>
    </div>
    <div class="resource-list">
      <div class="image-list">
        <div v-for="(url, index) in imageResources" :key="index" class="image-item">
          <img :src="url" alt="Image" @click="viewImage(url)"> 
        </div>
      </div>
      <el-button class="fixed-add-button" type="primary" icon="el-icon-plus" @click="addImage"></el-button>
    </div>
  </div>
</template>

<script>
import axios from 'axios';
import { getAccessToken } from "@/utils/auth";

export default {
  data() {
    return {
      imageResources: []
    };
  },
  created() {
    this.fetchImageResources();
  },
  methods: {
    fetchImageResources() {
      axios.get('/api/source/images', {
        headers: {
          'Authorization': 'Bearer ' + getAccessToken(),
        }
      })

        .then(response => {
          console.log(response.data.data)
          // const baseURL = 'http://localhost:8000/picture/';
          const baseURL = '/app/picture/';
          for (let i = 0; i < response.data.data.length; i++) {
              response.data.data[i] = baseURL + response.data.data[i];
          }
          this.imageResources = response.data.data;
        })
        .catch(error => {
          console.error('Error fetching image resources:', error);
        });
    },
    getLocalFileURL(path) {
      // 如果浏览器支持 File API，则使用 createObjectURL 将本地文件路径转换为 URL
      if (window.URL && window.URL.createObjectURL) {
        return window.URL.createObjectURL(new File([path], ''));
      } else {
        // 否则，返回空字符串或者默认的图片地址
        return '';
      }

    },
    addImage() {
      this.$router.push('/source/addPicture');
    },
    viewImage(url) {
      console.log('查看图片:', url);
    },
    redirectToVideoPage() {
      this.$router.push('/source/video');
    },
    redirectToImagePage() {
      this.$router.push('/source/picture');
    }
  }
};
</script>

<style scoped>
.resource-page {
  background-color: #f4f6f8;
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
  background-color: #ffffff;
  border: 1px solid #ddd;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  position: relative;
}

.image-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  padding: 10px;
}

.image-item {
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  transition: transform 0.3s;
}

.image-item:hover {
  transform: scale(1.05);
}

.image-item img {
  width: 100%;
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
