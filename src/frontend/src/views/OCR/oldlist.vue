<!-- <template>
    <div>
      <el-row>
        <el-col :span="24">
          <h2>报告列表
            <el-button type="primary" @click="showAddReportDialog = true" style="float: right;">添加报告</el-button>
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
  
      <el-dialog title="添加报告" :visible.sync="showAddReportDialog">
        <el-form :model="newReport">
          <el-form-item label="体检日期" required>
            <el-date-picker v-model="newReport.date" type="date" placeholder="选择日期"></el-date-picker>
          </el-form-item>
          <el-form-item label="上传图片" required>
            <el-upload
              :action="uploadUrl"
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
              :on-remove="handleRemove"
              list-type="picture-card">
              <i class="el-icon-plus"></i>
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="showAddReportDialog = false">取消</el-button>
          <el-button type="primary" @click="submitReport">提交</el-button>
        </div>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import { list } from '@/api/OCR';
  import axios from 'axios';
  import { getAccessToken } from "@/utils/auth";
  
  export default {
    data() {
      return {
        reports: [],
        showAddReportDialog: false,
        newReport: {
          date: '',
          file: null
        },
        fileList: [],
        uploadUrl: '/api/ocr/add' // Update this to your upload endpoint if necessary
      };
    },
    created() {
      this.fetchReports();
    },
    methods: {
      fetchReports() {
        list().then(response => {
          this.reports = response.data.data;
        }).catch(error => {
          console.error("Error fetching reports:", error);
        });
      },
      viewReport(hid) {
        this.$router.push(`/ocr/list?hid=${hid}`);
      },
      handleBeforeUpload(file) {
        const isValidType = ["image/png", "image/jpg", "image/jpeg"].includes(file.type);
        const isValidSize = file.size / 1024 / 1024 < 10;
  
        if (!isValidType) {
          this.$message.error('文件格式不正确, 请上传 png/jpg/jpeg 格式的文件!');
          return false;
        }
  
        if (!isValidSize) {
          this.$message.error('上传文件大小不能超过 10 MB!');
          return false;
        }
  
        this.fileList = [{ name: file.name, url: URL.createObjectURL(file), raw: file }];
        this.newReport.file = file;
        return false; // 阻止默认的上传行为
      },
      handleRemove(file) {
        this.newReport.file = null;
        this.fileList = [];
      },
      submitReport() {
        if (!this.newReport.date) {
          this.$message.error("请选择体检日期");
          return;
        }
        if (!this.newReport.file) {
          this.$message.error("请添加体检报告");
          return;
        }
  
        const formData = new FormData();
        formData.append('date', this.newReport.date);
        formData.append('file', this.newReport.file);
  
        axios.post(this.uploadUrl, formData, {
          headers: {
            'Authorization': 'Bearer ' + getAccessToken(),
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(() => {
          this.showAddReportDialog = false;
          this.$message.success("报告添加成功！");
          this.fetchReports(); // 刷新报告列表
          this.fileList = []; // 清空上传的文件列表
          this.newReport.date = ''; // 清空表单日期
          this.newReport.file = null; // 清空表单文件
        })
        .catch(error => {
          console.error("Error adding report:", error);
          this.$message.error("添加报告失败！");
        });
      },
    }
  };
  </script>
  
  <style scoped>
  /* 添加一些基本样式 */
  </style>
   -->
   <template>
    <div>
      <el-row>
        <el-col :span="24">
          <h2>报告列表
            <el-button type="primary" @click="showAddReportDialog = true" style="float: right;">添加报告</el-button>
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
  
      <el-dialog title="添加报告" :visible.sync="showAddReportDialog">
        <el-form :model="newReport">
          <el-form-item label="体检日期" required>
            <el-date-picker v-model="newReport.date" type="date" placeholder="选择日期"></el-date-picker>
          </el-form-item>
          <el-form-item label="上传图片" required>
            <el-upload
              :file-list="fileList"
              :before-upload="handleBeforeUpload"
              :on-remove="handleRemove"
              list-type="picture-card">
              <i class="el-icon-plus"></i>
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="showAddReportDialog = false">取消</el-button>
          <el-button type="primary" @click="submitReport">提交</el-button>
        </div>
      </el-dialog>
    </div>
  </template>
  
  <script>
  import { list } from '@/api/OCR';
  import axios from 'axios';
  import { getAccessToken } from "@/utils/auth";
  
  export default {
    data() {
      return {
        reports: [],
        showAddReportDialog: false,
        newReport: {
          date: '',
          file: null
        },
        fileList: [],
        uploadUrl: '/api/ocr/add' // Update this to your upload endpoint if necessary
      };
    },
    created() {
      this.fetchReports();
    },
    methods: {
      fetchReports() {
        list().then(response => {
          this.reports = response.data.data;
        }).catch(error => {
          console.error("Error fetching reports:", error);
        });
      },
      viewReport(hid) {
        this.$router.push(`/ocr/list?hid=${hid}`);
      },
      handleBeforeUpload(file) {
        const isValidType = ["image/png", "image/jpg", "image/jpeg"].includes(file.type);
        const isValidSize = file.size / 1024 / 1024 < 10;
  
        if (!isValidType) {
          this.$message.error('文件格式不正确, 请上传 png/jpg/jpeg 格式的文件!');
          return false;
        }
  
        if (!isValidSize) {
          this.$message.error('上传文件大小不能超过 10 MB!');
          return false;
        }
  
        // 更新 fileList，确保包含 url 属性
        this.fileList = [{
          name: file.name,
          url: URL.createObjectURL(file), // 创建临时 URL 以显示图片
          raw: file
        }];
        this.newReport.file = file;
        return false; // 阻止默认的上传行为
      },
      handleRemove(file) {
        this.newReport.file = null;
        this.fileList = [];
      },
      submitReport() {
        if (!this.newReport.date) {
          this.$message.error("请选择体检日期");
          return;
        }
        if (!this.newReport.file) {
          this.$message.error("请添加体检报告");
          return;
        }
  
        const formData = new FormData();
        formData.append('date', this.newReport.date);
        formData.append('file', this.newReport.file);
  
        axios.post(this.uploadUrl, formData, {
          headers: {
            'Authorization': 'Bearer ' + getAccessToken(),
            'Content-Type': 'multipart/form-data'
          }
        })
        .then(() => {
          this.showAddReportDialog = false;
          this.$message.success("报告添加成功！");
          this.fetchReports(); // 刷新报告列表
          this.fileList = []; // 清空上传的文件列表
          this.newReport.date = ''; // 清空表单日期
          this.newReport.file = null; // 清空表单文件
        })
        .catch(error => {
          console.error("Error adding report:", error);
          this.$message.error("添加报告失败！");
        });
      },
    }
  };
  </script>
  
  <style scoped>
  /* 添加一些基本样式 */
  </style>
  