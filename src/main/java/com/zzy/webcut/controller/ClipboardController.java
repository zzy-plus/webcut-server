package com.zzy.webcut.controller;


import com.zzy.webcut.common.R;
import com.zzy.webcut.pojo.Clipboard;
import com.zzy.webcut.service.ClipboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clipboard")
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
    public R<String> save(@RequestBody Clipboard clipboard) {

        clipboardService.saveClipboard(clipboard);

        return R.success("创建成功");
    }

    /**
     * 根据名称获取剪切板数据
     *
     * @param name
     * @return
     */
    @GetMapping("/{name}")
    public R<Clipboard> getByName(@PathVariable String name) {

        Clipboard clipboard = clipboardService.getClipboardByName(name);

        return R.success(clipboard);
    }

    /**
     * 更新剪切板
     *
     * @param clipboard
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Clipboard clipboard) {

        clipboardService.updateClipboardByName(clipboard);

        return R.success("修改成功");
    }

    @DeleteMapping("/{name}")
    public R<String> delete(@PathVariable String name) {

        clipboardService.deleteClipboardByName(name);

        return R.success("删除成功");
    }


}
