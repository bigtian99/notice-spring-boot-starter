package club.bigtian.notice.controller;

import club.bigtian.notice.anno.DingTalk;
import club.bigtian.notice.constant.SystemConstant;
import club.bigtian.notice.domain.TExceptionInfo;
import club.bigtian.notice.domain.dto.PageListDto;
import club.bigtian.notice.service.INoticeService;
import club.bigtian.notice.service.ISystemCacheService;
import club.bigtian.notice.service.TExceptionInfoService;
import cn.hutool.core.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private INoticeService noticeService;



    /**
     * 通过主键查询单条数据
     *
     * @param id    主键
     * @param model 传输对象
     * @return 单条数据
     */
    @GetMapping("/selectOne")
    public String selectOne(Model model, @RequestParam(SystemConstant.ID) Long id) {
        TExceptionInfo tExceptionInfo = exceptionInfoService.selectByPrimaryKey(id);
        Assert.notNull(tExceptionInfo, "找不到该异常信息");
        model.addAttribute(SystemConstant.ERR_INFO, tExceptionInfo.getContent());
        model.addAttribute(SystemConstant.ID, id);
        model.addAttribute(SystemConstant.STATUS, tExceptionInfo.getHandled());
        return SystemConstant.ERROR_INFO;
    }

    @GetMapping("/list")
    public String list(PageListDto dto, Model model) {
        int page = dto.getPage();
        List<TExceptionInfo> list = exceptionInfoService.list(dto);
        Long count = exceptionInfoService.count(dto);
        Map<Object, Object> groupCount = exceptionInfoService.groupCount();
        model.addAttribute(SystemConstant.GROUP_COUNT, groupCount);
        model.addAttribute(SystemConstant.COUNT, count);
        model.addAttribute(SystemConstant.LIMIT, dto.getLimit());
        model.addAttribute(SystemConstant.PAGE, page);
        model.addAttribute(SystemConstant.HANDLED, dto.getHandled());
        model.addAttribute(SystemConstant.LIST, list);
        return SystemConstant.ERROR_LIST;
    }

    @GetMapping("/index")
    @ResponseBody
    public Map index(PageListDto dto) {
        int page = dto.getPage();
        List<TExceptionInfo> list = exceptionInfoService.list(dto);
        Long count = exceptionInfoService.count(dto);
        Map<Object, Object> groupCount = exceptionInfoService.groupCount();
        Map<String, Object> model = new HashMap<>();
        model.put(SystemConstant.GROUP_COUNT, groupCount);
        model.put(SystemConstant.COUNT, count);
        model.put(SystemConstant.LIMIT, dto.getLimit());
        model.put(SystemConstant.PAGE, page);
        model.put(SystemConstant.HANDLED, dto.getHandled());
        model.put(SystemConstant.DATA, list);
        model.put(SystemConstant.CODE, 0);
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
            noticeService.sendHandledMessage(info);
        }
        return flag;
    }

}
