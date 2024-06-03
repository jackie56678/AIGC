<template>
  <div class="resource-page">
    <div class="resource-type-selector">
      <el-button  @click="redirectToImagePage">图片资源</el-button>
      <el-button type="primary" @click="redirectToVideoPage">视频资源</el-button>
    </div>
    <div class="resource-list">
      <!-- <h2>视频资源</h2> -->

      <div class="video-list">
        <div v-for="(url, index) in videoResources" :key="index" class="video-item">
          <video :src="url" controls alt="Video" @click="viewVideo(url)"></video>
        </div>
      </div>

      <el-button class="fixed-add-button" type="primary" @click="addVideo">添加视频</el-button>
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
      axios.get('/api/source/videos',{
        headers:{
          'Authorization': 'Bearer ' + getAccessToken(),
        }
      })
        .then(response => {
          console.log(response.data.data)
          // const baseURL = 'http://localhost:8000/video/';
          const baseURL = 'app/ssadmin-server/source/video/';
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
      // 跳转到查看资源的页面，传递当前选中的资源类型和资源ID
      // this.$router.push({ path: '/viewResource', query: { type, id: resourceId }});
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
  padding: 20px;
}
.resource-type-selector {
  margin-bottom: 20px;
}
.resource-list {
  border: 1px solid #ccc;
  padding: 20px;
  padding-top: 40px; /* 增加上内边距 */
  padding-bottom: 60px; /* 增加下内边距 */
  position: relative; /* 为了定位固定按钮 */
}
.video-list {
  display: flex;
  flex-wrap: wrap;
  max-height: 500px; /* 固定高度 */
  overflow-y: auto; /* 超出部分滚动 */
}
.video-item {
  margin-right: 10px;
  margin-bottom: 10px;
  cursor: pointer;
}

.video-item video {
  width: 70%;
  height: auto;
  cursor: pointer;
}
.fixed-add-button {
  position: absolute;
  top: 20px;
  right: 20px;
}
</style>
