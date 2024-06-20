<template>
    <div>
      <el-row>
        <el-col :span="24">
          <h2>报告列表
            <el-button type="primary" @click="goToAddReport" style="float: right;">添加报告</el-button>
          </h2>
        </el-col>
      </el-row>
      <el-table :data="reports" style="width: 100%">
        <el-table-column prop="hid" label="报告编号"></el-table-column>
        <el-table-column label="报告图片">
          <template slot-scope="scope">
            <img :src="`http://localhost:8000/report/${scope.row.url}`" alt="报告图片" width="100">
          </template>
        </el-table-column>
        <el-table-column prop="date" label="报告时间"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="primary" @click="viewReport(scope.row.hid)">查看报告</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </template>
<script>
import { list } from '@/api/OCR';

export default {
  data() {
    return {
      reports: []
    };
  },
  created() {
    this.fetchReports();
  },
  methods: {
    fetchReports() {
      list().then(response => {
        this.reports = response.data;
      }).catch(error => {
        console.error("Error fetching reports:", error);
      });
    },
    viewReport(hid) {
      this.$router.push(`/ocr/showreport?hid=${hid}`);
    },
    goToAddReport() {
      this.$router.push('/ocr/addreport');
    }
  }
};
</script>
  