package com.zzy.webcut.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.webcut.common.ClipboardNotFoundException;
import com.zzy.webcut.common.Code;
import com.zzy.webcut.common.R;
import com.zzy.webcut.mapper.ClipboardMapper;
import com.zzy.webcut.pojo.Clipboard;
import com.zzy.webcut.pojo.Records;
import com.zzy.webcut.service.ClipboardService;
import com.zzy.webcut.service.RecordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        this.save(clipboard);
        //更新records
        Records records = new Records();
        records.setClipboardId(clipboard.getId());
        records.setAction(0);
        records.setMain("匿名");
        recordsService.save(records);
    }

    /**
     * 根据name获取剪切板
     *
     * @param name
     * @return
     */
    @Override
    @Transactional
    public R<Clipboard> getClipboardByName(String name) {
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
        if(now.isAfter(expTime)){
            this.deleteClipboardByName(name);
            return R.error(Code.CUT_OUT_OF_DATE, String.format("剪切板%s已过期", name));
        }

        Records records = new Records();
        records.setClipboardId(clipboard.getId());
        records.setAction(2);
        records.setMain("匿名");

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
    public R<String> updateClipboardByName(Clipboard clipboard) {
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
        if(now.isAfter(expTime)){
            this.deleteClipboardByName(clipboard.getName());
            return R.error(Code.CUT_OUT_OF_DATE, String.format("剪切板%s已过期", clipboard.getName()));
        }



        clipboard.setId(clipboard1.getId());
        this.updateById(clipboard);

        Records records = new Records();
        records.setClipboardId(clipboard.getId());
        records.setAction(1);
        records.setMain("匿名");

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
