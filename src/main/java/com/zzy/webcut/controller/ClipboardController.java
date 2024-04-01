package com.zzy.webcut.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzy.webcut.common.BaseContext;
import com.zzy.webcut.common.Code;
import com.zzy.webcut.common.R;
import com.zzy.webcut.pojo.Clipboard;
import com.zzy.webcut.pojo.ClipboardDto;
import com.zzy.webcut.service.ClipboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/clipboard")
@Slf4j
public class ClipboardController {

    @Autowired
    private ClipboardService clipboardService;

    /**
     * 新建剪切板
     *
     * @param clipboard
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Clipboard clipboard, HttpServletRequest request) {

        BaseContext.setIp(request.getRemoteAddr());

        clipboardService.saveClipboard(clipboard);

        return R.success("创建成功");
    }

    /**
     * 根据名称获取剪切板数据
     *
     * @param clipboard
     * @return
     */
    @PostMapping("/info")
    public R<ClipboardDto> getByName(@RequestBody Clipboard clipboard, HttpServletRequest request) {

        BaseContext.setIp(request.getRemoteAddr());

        R<Clipboard> result = clipboardService.getClipboardByName(clipboard);

        if(result.getCode().equals(Code.CUT_OUT_OF_DATE)){
            return R.error(Code.CUT_OUT_OF_DATE, result.getMsg());
        }else if(result.getCode().equals(Code.AUTH_REQUIRED)){
            return R.error(Code.AUTH_REQUIRED, result.getMsg());
        }else if(result.getCode().equals(Code.AUTH_FIALED)){
            return R.error(Code.AUTH_FIALED, result.getMsg());
        }

        Clipboard data = result.getData();
        ClipboardDto clipboardDto = new ClipboardDto();
        BeanUtils.copyProperties(data, clipboardDto);
        clipboardDto.setSid(data.getId().toString());

        return R.success(clipboardDto);
    }

    /**
     * 更新剪切板
     *
     * @param clipboardDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody ClipboardDto clipboardDto, HttpServletRequest request) {

        BaseContext.setIp(request.getRemoteAddr());

        R<String> result = clipboardService.updateClipboardByName(clipboardDto);

        if(result.getCode().equals(Code.CUT_OUT_OF_DATE)){
            return R.error(Code.CUT_OUT_OF_DATE, result.getMsg());
        }else if(result.getCode().equals(Code.AUTH_FIALED)){
            return R.error(Code.AUTH_FIALED, result.getMsg());
        }

        return R.success("修改成功");
    }

    /**
     * 删除剪切板
     * @param name
     * @return
     */
    @DeleteMapping("/{name}")
    public R<String> delete(@PathVariable String name) {

        clipboardService.deleteClipboardByName(name);

        return R.success("删除成功");
    }


    @PostMapping("/access")
    public R<String> access(@RequestBody Clipboard clipboard){
        if(clipboard.getName() == null || clipboard.getPassword() == null){
            return R.error(Code.AUTH_FIALED, "认证失败");
        }

        LambdaQueryWrapper<Clipboard> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Clipboard::getName, clipboard.getName());
        Clipboard one = clipboardService.getOne(queryWrapper);
        if(one == null){
            return R.error("未知错误");
        }
        if(clipboard.getPassword().equals(one.getPassword())){
            return R.success("认证成功");
        }else {
            return R.error(Code.AUTH_FIALED, "认证失败");
        }
    }


}
