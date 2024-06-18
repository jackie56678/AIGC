<template>
    <div class="task-list">
      <el-card class="box-card">
        <div slot="header" class="clearfix">
          <!-- <el-button type="primary" @click="showInfo" style="float: left;">个人信息</el-button> -->
          <span>我的记事本</span>
          <el-button type="primary" @click="showAddTaskModal" style="float: right;">添加事项</el-button>
        </div>
  
        <el-table :data="tasks" style="width: 100%">
          <el-table-column prop="name" label="任务事项" width="300" ></el-table-column>
          <el-table-column prop="ddl" label="截止时间" width="300"></el-table-column>
          <el-table-column prop="completed" label="完成状态" width="300">
            <template slot-scope="scope">
              <el-tag :type="scope.row.completed ? 'success' : 'info'">
                {{ scope.row.completed ? '已完成' : '未完成' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="修改操作" width="300">
            <template slot-scope="scope">
              <el-button size="mini" @click="editTask(scope.row)">修改</el-button>
            </template>
          </el-table-column>
          <el-table-column label="删除操作" width="300">
            <template slot-scope="scope">
              <el-button size="mini" @click="deleteTask(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
  
      </el-card>
    </div>
  </template>
  
  <script>
  import { fetchtasks } from '@/api/task';
  import { deletetask } from '@/api/task';
  export default {
    data() {
      return {
        tasks: []
      };
    },
    created() {
      this.fetchTasks();
    },
    methods: {
      fetchTasks() {
        fetchtasks()
            .then(response => {
              this.tasks = response;
            })
            .catch(error => {
              this.$message.error('获取任务列表失败');
              //this.$router.push('/');
            });
      },
      showAddTaskModal() {
        // 打开一个模态框或其他界面用于添加新任务
        this.$router.push('/todo/add'); // 假设你有一个路由用于添加任务
      },
      editTask(task) {
        // 导航到编辑任务页面，使用 query 参数传递 taskId
        // console.log(task)
        this.$router.push({
          path: '/todo/edit',
          query: { taskId: task.id }
        });
      },
      deleteTask(task) {
        // const token = localStorage.getItem('ACCESS_TOKEN');
        // axios.post('/api/tasks/delete', task, {
        //   headers: {
        //     'Authorization': 'Bearer '+token
        //   }
        // })
        deletetask(task)
            .then(response => {
              this.$message.success('任务删除成功');
              this.fetchTasks();
            })
            .catch(error => {
              this.$message.error('任务删除失败');
            });
  
      }
    }
  };
  </script>
  