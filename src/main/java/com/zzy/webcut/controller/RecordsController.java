package com.zzy.webcut.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zzy.webcut.common.R;
import com.zzy.webcut.pojo.Records;
import com.zzy.webcut.service.RecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records")
public class RecordsController {

    @Autowired
    private RecordsService recordsService;

    /**
     * 根据id获取records
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Page<Records>> page(@PathVariable String id){
        Page<Records> pageInfo = new Page<>(1,30);
        LambdaQueryWrapper<Records> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Records::getClipboardId, Long.parseLong(id));
        queryWrapper.orderByDesc(Records::getTime);
        queryWrapper.orderByDesc(Records::getAction);

        Page<Records> page = recordsService.page(pageInfo, queryWrapper);

        return R.success(page);
    }

}
