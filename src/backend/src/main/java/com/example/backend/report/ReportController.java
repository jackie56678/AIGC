package com.example.backend.report;
import com.example.backend.CommonResult;
import com.example.backend.chat.ParselContent;
import com.example.backend.chat.vivogpt;
import com.example.backend.user.User;
import com.example.backend.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ocr")
public class ReportController {

    @Autowired
    private ReportMapper reportMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private OCR ocr;
    @Value("${file.rep-dir}")
    private String reportDir;
    @GetMapping("/list")
    public CommonResult<List<Report>> list(@RequestHeader("Authorization") String accessToken) {
        try {
            String token = accessToken.substring(7);
            User user = userService.getUserInfoByToken(token);
            List<Report> reports = reportMapper.list(user.getId());
            return CommonResult.success(reports);
        } catch (Exception e) {
            return CommonResult.error(400,"报告获取失败");
        }
    }


    @PostMapping("/add")
    public CommonResult<?> add(@RequestHeader("Authorization") String accessToken,
                                  @RequestParam("file") MultipartFile file,
                                  @RequestParam("date") Date date) throws Exception {
        String token = accessToken.substring(7);
        User user = userService.getUserInfoByToken(token);
        Report report = new Report();
        report.setUid(user.getId());
        report.setDate(date);
        //识别图片+存入result
        String result  = ocr.ocrTest(file);
        //判断error_code是否为0

        //判断error_code是否为0
        System.out.println("result:"+result);
        //处理result

        //处理result
        report.setResult(result);
        //生成suggestion
        String question = "请根据以下的体检报告内容进行分析，并根据报告内容给出保持健康的建议：" + result;
        String jsonResponse = vivogpt.vivogpt(question);
        ParselContent parselContent = new ParselContent();
        CommonResult commonResult = parselContent.parsel(jsonResponse);
        int code = commonResult.getCode();
        if(code==200) {
            String suggestion = commonResult.getData().toString();
            report.setSuggestion(suggestion);
            String uniqueFileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            System.out.println("文件名为" + uniqueFileName);
            File uploadedFile = new File(reportDir + uniqueFileName);
            file.transferTo(uploadedFile);
            report.setUrl(uniqueFileName);
            reportMapper.add(report);
            return CommonResult.success("报告添加成功");
        }
        return CommonResult.error(400,"报告添加失败");
    }

    @GetMapping("/list2")
    public CommonResult<?> list2(@RequestParam("hid") int hid, @RequestHeader("Authorization") String accessToken) throws Exception {
        String token = accessToken.substring(7);
        User user = userService.getUserInfoByToken(token);
        Report report = reportMapper.list2(user.getId(), hid);
        return CommonResult.success(report);
    }
}
