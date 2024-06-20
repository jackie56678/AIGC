package com.example.backend.report;
import com.example.backend.CommonResult;
import com.example.backend.chat.ParselContent;
import com.example.backend.chat.vivogpt;
import com.example.backend.user.User;
import com.example.backend.user.UserService;
import com.example.backend.utils.Parsel;
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

//    @Autowired
//    private Parsel parsel2;
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
        String result  = ocr.ocrTest(file);
        System.out.println("result:"+result);
        Parsel parsel = new Parsel();
        String resultup = parsel.parsel(result).getData().toString();
        System.out.println("resultup:"+resultup);
        report.setResult(resultup);
        String question = "请根据以下的体检报告内容进行分析，并根据报告内容给出保持健康的建议，内容中和报告不相关的文字请忽略，只关注和体检报告相关的内容：" + resultup;
        System.out.println("question:"+question);
        String jsonResponse = vivogpt.vivogpt(question);
        ParselContent parselContent = new ParselContent();
        CommonResult commonResult = parselContent.parsel(jsonResponse);
        int code = commonResult.getCode();
        if(code==200) {
            String suggestion = commonResult.getData().toString();
            System.out.println("suggestion:"+suggestion);
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

    @PostMapping("/list2")
    public CommonResult<?> list2(@RequestBody int hid, @RequestHeader("Authorization") String accessToken) throws Exception {
        String token = accessToken.substring(7);
        User user = userService.getUserInfoByToken(token);
        Report report = reportMapper.list2(user.getId(), hid);
        return CommonResult.success(report);
    }
}
