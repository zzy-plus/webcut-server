package com.zzy.webcut.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zzy.webcut.pojo.Clipboard;

public interface ClipboardService extends IService<Clipboard> {

    void saveClipboard(Clipboard clipboard);

    Clipboard getClipboardByName(String name);

    void updateClipboardByName(Clipboard clipboard);

    void deleteClipboardByName(String name);


}
