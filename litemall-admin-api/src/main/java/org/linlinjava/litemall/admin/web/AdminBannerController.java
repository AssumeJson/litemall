package org.linlinjava.litemall.admin.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.enums.StorageType;
import org.linlinjava.litemall.core.storage.StorageService;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.core.validator.Order;
import org.linlinjava.litemall.core.validator.Sort;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.service.LitemallStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static org.linlinjava.litemall.core.consts.AdminConstant.STORAGE_FLAG;

/**
 * banner管理 {@link AdminStorageController}
 * 改banner管理上传的banner类型为banner/文件格式
 *
 * @author by <a href="mailto:ligang941012@gmail.com">gang.Li</a>
 * @since 2024/11/1 14:21
 */
@RestController
@RequestMapping("/admin/storage/banner")
@Validated
public class AdminBannerController {
    private final Log logger = LogFactory.getLog(AdminStorageController.class);

    @Autowired
    private StorageService storageService;
    @Autowired
    private LitemallStorageService litemallStorageService;

    @RequiresPermissions("admin:storage:list")
    @RequiresPermissionsDesc(menu = {"商品管理", "banner管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(String key, String name,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @Sort @RequestParam(defaultValue = "add_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        List<LitemallStorage> storageList = litemallStorageService.querySelectiveByType(key, name, page, limit, sort, order, StorageType.BANNER.storageType());
        return ResponseUtil.okList(storageList);
    }

    @RequiresPermissions("admin:storage:create")
    @RequiresPermissionsDesc(menu = {"商品管理", "banner管理"}, button = "上传")
    @PostMapping("/create")
    public Object create(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String bannerType = StorageType.BANNER.storageType() + STORAGE_FLAG + file.getContentType();
        LitemallStorage litemallStorage = storageService.store(file.getInputStream(), file.getSize(),
                bannerType, originalFilename);
        return ResponseUtil.ok(litemallStorage);
    }
}
