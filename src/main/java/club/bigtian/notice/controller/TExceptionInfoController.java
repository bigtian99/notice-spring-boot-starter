package club.bigtian.notice.controller;

import club.bigtian.notice.anno.DingTalk;
import club.bigtian.notice.domain.TExceptionInfo;
import club.bigtian.notice.domain.dto.PageListDto;
import club.bigtian.notice.service.TExceptionInfoService;
import club.bigtian.notice.utils.DingTalkUtil;
import cn.hutool.core.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常处理控制层
 *
 * @author bigtian
 */
@Controller
@RequestMapping("/exception")
public class TExceptionInfoController {
    /**
     * 服务对象
     */
    @Autowired
    private TExceptionInfoService exceptionInfoService;

    /**
     * 通过主键查询单条数据
     *
     * @param id 主键
     * @param model 传输对象
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public String selectOne(Model model, @RequestParam("id") Long id) {
        TExceptionInfo tExceptionInfo = exceptionInfoService.selectByPrimaryKey(id);
        Assert.notNull(tExceptionInfo, "找不到该异常信息");
        model.addAttribute("errInfo", tExceptionInfo.getContent());
        model.addAttribute("id", id);
        model.addAttribute("status", tExceptionInfo.getHandled());
        return "errorInfo";
    }

    @GetMapping("/list")
    public String list(PageListDto dto, Model model) {
        int page = dto.getPage();
        List<TExceptionInfo> list = exceptionInfoService.list(dto);
        Long count = exceptionInfoService.count(dto);
        Map<Object, Object> groupCount = exceptionInfoService.groupCount();
        model.addAttribute("groupCount", groupCount);
        model.addAttribute("count", count);
        model.addAttribute("limit", dto.getLimit());
        model.addAttribute("page", page);
        model.addAttribute("handled", dto.getHandled());
        model.addAttribute("list", list);
        return "errorList";
    }

    @GetMapping("/index")
    @ResponseBody
    public HashMap index(PageListDto dto) {
        int page = dto.getPage();
        List<TExceptionInfo> list = exceptionInfoService.list(dto);
        Long count = exceptionInfoService.count(dto);
        Map<Object, Object> groupCount = exceptionInfoService.groupCount();
        HashMap<String, Object> model = new HashMap<>();
        model.put("groupCount", groupCount);
        model.put("count", count);
        model.put("limit", dto.getLimit());
        model.put("page", page);
        model.put("handled", dto.getHandled());
        model.put("data", list);
        model.put("code", 0);
        return model;
    }

    @PostMapping("/handler")
    @ResponseBody
    public boolean handler(@RequestBody TExceptionInfo info) {
        info.setHandledTime(new Date());
        info.setHandled("Y");
        boolean flag = exceptionInfoService.updateByPrimaryKeySelective(info) > 0;
        if (flag) {
            //发送处理消息到钉钉
            DingTalkUtil.sendHandledMd(info);
        }
        return flag;
    }

    @GetMapping("thr")
    @ResponseBody
    @DingTalk(author = "bigtian")
    public void thr(HttpServletRequest request) {
        int i = 1 / 0;
    }


}
