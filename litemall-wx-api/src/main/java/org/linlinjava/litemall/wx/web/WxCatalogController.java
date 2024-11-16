package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallAd;
import org.linlinjava.litemall.db.domain.LitemallCategory;
import org.linlinjava.litemall.db.domain.LitemallGoods;
import org.linlinjava.litemall.db.dto.LitemallGoodsAndItemName;
import org.linlinjava.litemall.db.service.LitemallAdService;
import org.linlinjava.litemall.db.service.LitemallCategoryService;
import org.linlinjava.litemall.db.service.LitemallGoodsService;
import org.linlinjava.litemall.wx.service.HomeCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 类目服务
 */
@RestController
@RequestMapping("/wx/catalog")
@Validated
public class WxCatalogController {
    private final Log logger = LogFactory.getLog(WxCatalogController.class);

    @Autowired
    private LitemallCategoryService categoryService;

    @Autowired
    private LitemallGoodsService goodsService;

    @Autowired
    private LitemallAdService adService;

    @GetMapping("/getfirstcategory")
    public Object getFirstCategory() {
        // 所有一级分类目录
        List<LitemallCategory> l1CatList = categoryService.queryL1();
        return ResponseUtil.ok(l1CatList);
    }

    @GetMapping("/getsecondcategory")
    public Object getSecondCategory(@NotNull Integer id) {
        // 所有二级分类目录
        List<LitemallCategory> currentSubCategory = categoryService.queryByPid(id);
        return ResponseUtil.ok(currentSubCategory);
    }

    /**
     * 分类详情
     *
     * @param id 分类类目ID。
     *           如果分类类目ID是空，则选择第一个分类类目。
     *           需要注意，这里分类类目是一级类目
     * @return 分类详情
     */
    @GetMapping("index")
    public Object index(Integer id) {
        // 获取所有AD,用作分类广告
        List<LitemallAd> litemallAds = adService.queryIndex();

        // 所有一级分类目录
        List<LitemallCategory> l1CatList = categoryService.queryL1();

        // 当前一级分类目录
        LitemallCategory currentCategory = null;
        if (id != null) {
            currentCategory = categoryService.findById(id);
        } else {
            if (!l1CatList.isEmpty()) {
                currentCategory = l1CatList.get(0);
            }
        }

        // 当前一级分类目录对应的二级分类目录
        List<LitemallCategory> currentSubCategory = null;
        if (null != currentCategory) {
            currentSubCategory = categoryService.queryByPid(currentCategory.getId());
        }

        // 获取所有二级分类
        List<LitemallCategory> allSubCategoryList = null;
        if (null != l1CatList) {
            allSubCategoryList = categoryService.queryL2ByIds(l1CatList.stream()
                    .map(LitemallCategory::getId)
                    .collect(Collectors.toList()));
        }

        // 将一级分类降级为二级分类
        if (null != allSubCategoryList) {
            currentCategory = allSubCategoryList.get(0);
        }

        List<LitemallGoods> allGoodsCategories = null;
        if (null != allSubCategoryList) {
            allGoodsCategories = goodsService.queryByCategoryIds(allSubCategoryList.stream()
                    .map(LitemallCategory::getId).collect(Collectors.toList()));
        }

        // 获取所有对应的goods 包含的itemName
        List<LitemallGoodsAndItemName> goodsAndItemNameList = new ArrayList<>();
        if (null != allSubCategoryList) {
            allSubCategoryList.forEach(each -> {
                LitemallGoodsAndItemName goodsAndItemName = new LitemallGoodsAndItemName();
                goodsAndItemName.setItemName(each.getName());
                goodsAndItemName.setLitemallGoods(goodsService.queryByCategoryId(each.getId()));
                goodsAndItemNameList.add(goodsAndItemName);
            });
        }


        Map<String, Object> data = new HashMap<>();
        data.put("ads", litemallAds);
        data.put("categoryList", l1CatList);
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);
        // 添加所有二级分类
        data.put("allSubCategoryList", allSubCategoryList);
        // 添加所有在售的商品种类
        data.put("allGoodsCategories", allGoodsCategories);
        // 包含每个二级菜单对应的商品
        data.put("goodsAndItemNameList", goodsAndItemNameList);
        return ResponseUtil.ok(data);
    }

    /**
     * 所有分类数据
     *
     * @return 所有分类数据
     */
    @GetMapping("all")
    public Object queryAll() {
        // 优先从缓存中读取
        if (HomeCacheManager.hasData(HomeCacheManager.CATALOG)) {
            return ResponseUtil.ok(HomeCacheManager.getCacheData(HomeCacheManager.CATALOG));
        }


        // 所有一级分类目录
        List<LitemallCategory> l1CatList = categoryService.queryL1();

        // 所有子分类列表
        Map<Integer, List<LitemallCategory>> allList = new HashMap<>();
        List<LitemallCategory> sub;
        for (LitemallCategory category : l1CatList) {
            sub = categoryService.queryByPid(category.getId());
            allList.put(category.getId(), sub);
        }

        // 当前一级分类目录
        LitemallCategory currentCategory = l1CatList.get(0);

        // 当前一级分类目录对应的二级分类目录
        List<LitemallCategory> currentSubCategory = null;
        if (null != currentCategory) {
            currentSubCategory = categoryService.queryByPid(currentCategory.getId());
        }

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("categoryList", l1CatList);
        data.put("allList", allList);
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);

        // 缓存数据
        HomeCacheManager.loadData(HomeCacheManager.CATALOG, data);
        return ResponseUtil.ok(data);
    }

    /**
     * 当前分类栏目
     *
     * @param id 分类类目ID
     * @return 当前分类栏目
     */
    @GetMapping("current")
    public Object current(@NotNull Integer id) {
        // 当前分类
        LitemallCategory currentCategory = categoryService.findById(id);
        if (currentCategory == null) {
            return ResponseUtil.badArgumentValue();
        }
        List<LitemallCategory> currentSubCategory = categoryService.queryByPid(currentCategory.getId());

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("currentCategory", currentCategory);
        data.put("currentSubCategory", currentSubCategory);
        return ResponseUtil.ok(data);
    }
}
