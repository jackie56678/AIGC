package com.example.backend.task;
import com.example.backend.user.UserService;
import com.example.backend.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;  // 确保 UserService 能够被注入

    @GetMapping("/mytasks")
    public ResponseEntity<?> getMyTasks(@RequestHeader("Authorization") String ACCESS_TOKEN) {
        try {
            String token = ACCESS_TOKEN.substring(7);
            // 从 token 中获取用户信息
            User user = userService.getUserInfoByToken(token);
            // 使用用户ID获取任务列表
            //System.out.println(user.getId());
            List<Task> tasks = taskService.getTasksByUserId(user.getId());
            return ResponseEntity.ok(tasks);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addTask(@RequestBody Task task, @RequestHeader("Authorization") String ACCESS_TOKEN) {
        try {
            String token = ACCESS_TOKEN.substring(7);
            // 可能需要从 token 解析用户 ID
            User user = userService.getUserInfoByToken(token);
            task.setUid(user.getId());  // 设置任务的用户 ID
            //System.out.println(task.getUid());
            taskService.addTask(task);
            return ResponseEntity.ok("任务添加成功");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("任务添加失败: " + e.getMessage());
        }
    }

    // 在 TaskController.java 中
    @GetMapping("/{taskId}")
    public ResponseEntity<?> getTaskById(@PathVariable Long taskId, @RequestHeader("Authorization") String ACCESS_TOKEN) {
        try {
            String token = ACCESS_TOKEN.substring(7);
            User user = userService.getUserInfoByToken(token);
            Task task = taskService.findTaskById(taskId);

            if (task != null && task.getUid().equals(user.getId())) {
                return ResponseEntity.ok(task);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("访问被拒绝");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("服务器错误: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateTask(@RequestBody Task task, @RequestHeader("Authorization") String ACCESS_TOKEN) {
        try {
            String token = ACCESS_TOKEN.substring(7);
            User user = userService.getUserInfoByToken(token);
            task.setUid(user.getId());  // 设置任务的用户 ID
            taskService.updateTask(task);
            return ResponseEntity.ok("任务更新成功");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("任务添加失败: " + e.getMessage());
        }
    }
    @PostMapping("/delete")
    public ResponseEntity<?> deleteTask(@RequestBody Task task, @RequestHeader("Authorization") String ACCESS_TOKEN) {
        try {
            String token = ACCESS_TOKEN.substring(7);
            User user = userService.getUserInfoByToken(token);
            task.setUid(user.getId());  // 设置任务的用户 ID
            taskService.deleteTask(task);
            return ResponseEntity.ok("任务删除成功");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("任务删除失败: " + e.getMessage());
        }
    }

}
