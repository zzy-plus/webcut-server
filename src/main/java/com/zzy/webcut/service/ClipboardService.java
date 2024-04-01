package com.zzy.webcut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.webcut.common.R;
import com.zzy.webcut.pojo.Clipboard;

public interface ClipboardService extends IService<Clipboard> {

    void saveClipboard(Clipboard clipboard);

    R<Clipboard> getClipboardByName(String name);

    R<String> updateClipboardByName(Clipboard clipboard);

    void deleteClipboardByName(String name);


}
