package org.linlinjava.litemall.db.dto;

import java.util.List;

/**
 * 包含goods所在的二级菜单的名称
 *
 * @author by <a href="mailto:ligang941012@gmail.com">gang.Li</a>
 * @since 2024/11/16 22:38
 */
public class LitemallGoodsAndItemName {
    private String itemName;

    private List<LitemallGoodsDto> litemallGoodsDto;

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public List<LitemallGoodsDto> getLitemallGoodsDto() {
        return litemallGoodsDto;
    }

    public void setLitemallGoodsDto(List<LitemallGoodsDto> litemallGoodsDto) {
        this.litemallGoodsDto = litemallGoodsDto;
    }
}
