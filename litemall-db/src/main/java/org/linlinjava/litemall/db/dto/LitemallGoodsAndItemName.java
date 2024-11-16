package org.linlinjava.litemall.db.dto;

import org.linlinjava.litemall.db.domain.LitemallGoods;

import java.util.List;

/**
 * 包含goods所在的二级菜单的名称
 *
 * @author by <a href="mailto:ligang941012@gmail.com">gang.Li</a>
 * @since 2024/11/16 22:38
 */
public class LitemallGoodsAndItemName {
    private String itemName;
        private List<LitemallGoods> litemallGoods;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<LitemallGoods> getLitemallGoods() {
        return litemallGoods;
    }

    public void setLitemallGoods(List<LitemallGoods> litemallGoods) {
        this.litemallGoods = litemallGoods;
    }
}
