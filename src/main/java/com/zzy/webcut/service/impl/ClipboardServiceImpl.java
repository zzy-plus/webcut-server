package com.zzy.webcut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.webcut.common.*;
import com.zzy.webcut.mapper.ClipboardMapper;
import com.zzy.webcut.pojo.Clipboard;
import com.zzy.webcut.pojo.ClipboardDto;
import com.zzy.webcut.pojo.Records;
import com.zzy.webcut.service.ClipboardService;
import com.zzy.webcut.service.RecordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

@Service
@Slf4j
public class ClipboardServiceImpl extends ServiceImpl<ClipboardMapper, Clipboard> implements ClipboardService {

    @Autowired
    private RecordsService recordsService;

    /**
     * 新建剪切板
     *
     * @param clipboard
     */
    @Override
    @Transactional
    public void saveClipboard(Clipboard clipboard) {

        LambdaQueryWrapper<Clipboard> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Clipboard::getName, clipboard.getName());

        Clipboard one = this.getOne(queryWrapper);
        if (one != null) {
            //检查是否过期
            LocalDateTime updateTime = one.getUpdateTime();
            Integer expiration = one.getExpiration();
            LocalDateTime expTime = updateTime.plusSeconds(expiration);
            LocalDateTime now = LocalDateTime.now();
            if (now.isBefore(expTime)) {  //没过期
                throw new ClipboardExistException(String.format("剪切板%s已存在", clipboard.getName()));
            } else {     //已过期
                this.deleteClipboardByName(clipboard.getName());
            }
        }

        //新增
        this.save(clipboard);

        //更新records
        Records records = new Records();
        records.setClipboardId(clipboard.getId());
        records.setAction(0);
        records.setMain(BaseContext.getIp());
        recordsService.save(records);
    }

    /**
     * 根据name获取剪切板
     *
     * @param clipboard0
     * @return
     */
    @Override
    @Transactional
    public R<Clipboard> getClipboardByName(Clipboard clipboard0) {
        String name = clipboard0.getName();
        String password = clipboard0.getPassword();
        LambdaQueryWrapper<Clipboard> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Clipboard::getName, name);
        Clipboard clipboard = this.getOne(queryWrapper);

        if (clipboard == null) {
            throw new ClipboardNotFoundException(String.format("剪切板%s不存在", name));
        }

        //检查是否过期
        LocalDateTime updateTime = clipboard.getUpdateTime();
        Integer expiration = clipboard.getExpiration();
        LocalDateTime expTime = updateTime.plusSeconds(expiration);
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expTime)) {
            this.deleteClipboardByName(name);
            return R.error(Code.CUT_OUT_OF_DATE, String.format("剪切板%s已过期", name));
        }

        //检查是否需要认证
        if(clipboard.getIsPrivate() == 1){
            if(password == null){
                return R.error(Code.AUTH_REQUIRED, "密码不能为空");
            }else if(!clipboard.getPassword().equals(password)){
                return R.error(Code.AUTH_FIALED, "认证失败");
            }
        }

        Records records = new Records();
        records.setClipboardId(clipboard.getId());
        records.setAction(2);
        records.setMain(BaseContext.getIp());

        recordsService.save(records);

        return R.success(clipboard);
    }

    /**
     * 根据name更新剪切板
     *
     * @param clipboard
     */
    @Override
    @Transactional
    public R<String> updateClipboardByName(ClipboardDto clipboard) {
        LambdaQueryWrapper<Clipboard> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Clipboard::getName, clipboard.getName());

        Clipboard clipboard1 = this.getOne(queryWrapper);
        if (clipboard1 == null) {
            throw new ClipboardNotFoundException(String.format("剪切板%s不存在", clipboard.getName()));
        }


        //检查是否过期
        LocalDateTime updateTime = clipboard1.getUpdateTime();
        Integer expiration = clipboard1.getExpiration();
        LocalDateTime expTime = updateTime.plusSeconds(expiration);
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expTime)) {
            this.deleteClipboardByName(clipboard.getName());
            return R.error(Code.CUT_OUT_OF_DATE, String.format("剪切板%s已过期", clipboard.getName()));
        }

        //认证
        if(clipboard1.getIsPrivate() == 1){
            if(!clipboard1.getPassword().equals(clipboard.getPassword())){
                return R.error(Code.AUTH_FIALED, "修改该剪切板需要密码");
            }
        }

        if(StringUtils.hasLength(clipboard.getNewPassword())){
            clipboard.setPassword(clipboard.getNewPassword());
        }

        clipboard.setId(clipboard1.getId());
        this.updateById(clipboard);

        Records records = new Records();
        records.setClipboardId(clipboard.getId());
        records.setAction(1);
        records.setMain(BaseContext.getIp());

        recordsService.save(records);

        return R.success("success");
    }

    /**
     * 根据name删除剪切板
     *
     * @param name
     */
    @Override
    @Transactional
    public void deleteClipboardByName(String name) {

        LambdaQueryWrapper<Clipboard> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Clipboard::getName, name);

        Clipboard clipboard = this.getOne(queryWrapper);
        if (clipboard == null) {
            throw new ClipboardNotFoundException(String.format("剪切板%s不存在", name));
        }

        this.removeById(clipboard.getId());

        LambdaQueryWrapper<Records> recordsLambdaQueryWrapper = new LambdaQueryWrapper<>();
        recordsLambdaQueryWrapper.eq(Records::getClipboardId, clipboard.getId());

        recordsService.remove(recordsLambdaQueryWrapper);

    }


}
