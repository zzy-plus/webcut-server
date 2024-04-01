package com.zzy.webcut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.webcut.common.R;
import com.zzy.webcut.pojo.Clipboard;
import com.zzy.webcut.pojo.ClipboardDto;

public interface ClipboardService extends IService<Clipboard> {

    void saveClipboard(Clipboard clipboard);

    R<Clipboard> getClipboardByName(Clipboard clipboard);

    R<String> updateClipboardByName(ClipboardDto clipboardDto);

    void deleteClipboardByName(String name);


}
