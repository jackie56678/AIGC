<template>
    <div class="report-container">
      <div v-if="report" class="report-content">
        <div class="image-container">
          <img 
            :src="'http://localhost:8000/report/' + report.url" 
            alt="报告图片"
            @click="showLightbox = true"
          />
        </div>
        <div class="suggestion-container">
          <h2>分析与建议</h2>
          <p>{{ report.suggestion }}</p>
        </div>
      </div>
      <div v-else>
        <p>正在加载报告信息...</p>
      </div>
      <button @click="goBack" class="back-button">返回</button>
      <vue-image-lightbox 
        v-if="showLightbox"
        :images="[imageUrl]"
        @close="showLightbox = false"
      />
    </div>
  </template>
  
  <script>
  import { list2 } from '@/api/OCR';
  import VueImageLightbox from 'vue-image-lightbox';
  import 'vue-image-lightbox/dist/vue-image-lightbox.min.css';
  
  export default {
    name: 'ShowReport',
    components: {
      VueImageLightbox
    },
    data() {
      return {
        report: null,
        hid: this.$route.query.hid,
        showLightbox: false,
        imageUrl: ''
      };
    },
    created() {
      console.log("hid:", this.hid);
      if (this.hid) {
        this.fetchReport();
      } else {
        console.error('缺少 hid 参数');
      }
    },
    methods: {
      fetchReport() {
        list2(this.hid)
          .then(response => {
            if (response && response.data) {
              this.report = response.data;
              this.imageUrl = 'http://localhost:8000/report/' + response.data.url;
            }
          })
          .catch(error => {
            console.error('获取报告信息失败:', error);
          });
      },
      goBack() {
        this.$router.go(-1);
      }
    }
  };
  </script>
  
  <style scoped>
  .report-container {
    padding: 20px;
    max-width: 1400px;
    margin: 0 auto;
    font-family: Arial, sans-serif;
    position: relative;
  }
  
  .report-content {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    border: 1px solid #4A90E2; /* 边框颜色 */
    border-radius: 16px;
    overflow: hidden;
    background: linear-gradient(145deg, #e0f7fa, #ffffff); /* 渐变背景 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: box-shadow 0.3s ease;
    padding: 20px;
    height: 800px;
  }
  
  .report-content:hover {
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
  }
  
  .image-container {
    flex: 1 1 50%;
    border-right: 1px solid #4A90E2; /* 边框颜色 */
    padding: 20px;
    box-sizing: border-box;
  }
  
  .image-container img {
    max-width: 100%;
    height: auto;
    display: block;
    margin: 0 auto;
    border-radius: 8px;
    cursor: pointer;
    transition: transform 0.3s ease;
  }
  
  .image-container img:hover {
    transform: scale(1.05);
  }
  
  .suggestion-container {
    flex: 1 1 50%;
    padding: 20px;
    box-sizing: border-box;
  }
  
  .suggestion-container h2 {
    margin-top: 0;
    color: #4A90E2; /* 标题颜色 */
    font-size: 28px;
    border-bottom: 2px solid #4A90E2; /* 边框颜色 */
    padding-bottom: 10px;
    margin-bottom: 20px;
  }
  
  .suggestion-container p {
    background-color: #fff;
    padding: 20px;
    font-size: 18px;
    border: 1px solid #4A90E2; /* 边框颜色 */
    border-radius: 8px;
    height: 100%;
    max-height: 720px;
    overflow-y: auto;
    white-space: pre-wrap;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
  }
  
  .back-button {
    position: absolute;
    top: 20px;
    right: 20px;
    background-color: #007bff;
    color: white;
    border: none;
    padding: 10px 20px;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s ease, transform 0.3s ease;
  }
  
  .back-button:hover {
    background-color: #0056b3;
    transform: scale(1.05);
  }
  </style>
  