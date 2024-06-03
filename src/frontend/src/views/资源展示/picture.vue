<template>
  <div class="resource-page">
    <div class="resource-type-selector">
      <el-button type="primary" >图片资源</el-button>
      <el-button @click="redirectToVideoPage">视频资源</el-button>
    </div>
    <div class="resource-list">
      <!-- <h2>图片资源</h2> -->
      <div class="image-list">
        <div v-for="(url, index) in imageResources" :key="index" class="image-item">
          <img :src="url" alt="Image" @click="viewImage(url)"> 
        </div>
      </div>
      <!-- <img src="https://gss0.baidu.com/9fo3dSag_xI4khGko9WTAnF6hhy/zhidao/pic/item/cc11728b4710b912ba1a8ca0c4fdfc0393452290.jpg"> -->
      <el-button class="fixed-add-button" type="primary" @click="addImage">添加图片</el-button>
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
    this.fetchImageResources(); // 页面加载时自动调用获取图片资源的方法
  },
  methods: {
    fetchImageResources() {
      axios.get('/api/source/images',{
        headers:{
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
      // 在这里你可以实现点击图片后的预览或其他操作
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
  padding-top: 40px;
  padding-bottom: 60px;
  position: relative;
}
.image-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 10px;
  padding: 10px;
}
.image-item {
  margin-right: 10px;
  margin-bottom: 10px;
  cursor: pointer;
}

.image-item img {
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
