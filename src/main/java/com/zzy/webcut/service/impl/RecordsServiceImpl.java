package com.zzy.webcut.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzy.webcut.mapper.RecordsMapper;
import com.zzy.webcut.pojo.Records;
import com.zzy.webcut.service.RecordsService;
import org.springframework.stereotype.Service;

@Service
public class RecordsServiceImpl extends ServiceImpl<RecordsMapper, Records> implements RecordsService {
}
