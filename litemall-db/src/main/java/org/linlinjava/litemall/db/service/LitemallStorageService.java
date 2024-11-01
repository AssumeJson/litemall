package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.LitemallStorageMapper;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.domain.LitemallStorageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class LitemallStorageService {
    @Autowired
    private LitemallStorageMapper storageMapper;

    public void deleteByKey(String key) {
        LitemallStorageExample example = new LitemallStorageExample();
        example.or().andKeyEqualTo(key);
        storageMapper.logicalDeleteByExample(example);
    }

    public void add(LitemallStorage storageInfo) {
        storageInfo.setAddTime(LocalDateTime.now());
        storageInfo.setUpdateTime(LocalDateTime.now());
        storageMapper.insertSelective(storageInfo);
    }

    public LitemallStorage findByKey(String key) {
        LitemallStorageExample example = new LitemallStorageExample();
        example.or().andKeyEqualTo(key).andDeletedEqualTo(false);
        return storageMapper.selectOneByExample(example);
    }

    public int update(LitemallStorage storageInfo) {
        storageInfo.setUpdateTime(LocalDateTime.now());
        return storageMapper.updateByPrimaryKeySelective(storageInfo);
    }

    public LitemallStorage findById(Integer id) {
        return storageMapper.selectByPrimaryKey(id);
    }

    public List<LitemallStorage> querySelective(String key, String name, Integer page, Integer limit, String sort, String order) {
        return querySelectiveByType(key, name, page, limit, sort, order, null);
    }


    /**
     * @param key
     * @param name
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @param preType 类型判断
     * @return
     */
    public List<LitemallStorage> querySelectiveByType(String key, String name, Integer page, Integer limit, String sort, String order, String preType) {
        LitemallStorageExample example = new LitemallStorageExample();
        LitemallStorageExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(key)) {
            criteria.andKeyEqualTo(key);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(preType)) {
            criteria.andTypeLike(preType + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return storageMapper.selectByExample(example);
    }

    public String[] querySelectiveByName(String banner) {
        LitemallStorageExample example = new LitemallStorageExample();
        LitemallStorageExample.Criteria criteria = example.createCriteria();
        criteria.andNameLike(banner + "%");
        criteria.andDeletedEqualTo(false);
        PageHelper.startPage(1, 10);
        final List<LitemallStorage> litemallStorages = storageMapper.selectByExample(example);
        ArrayList<String> urls = new ArrayList<>();
        litemallStorages.forEach(litemallStorage -> urls.add(litemallStorage.getUrl()));
        return urls.toArray(new String[]{});
    }
}
