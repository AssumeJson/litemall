package org.linlinjava.litemall.core;

import org.apache.catalina.StoreManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.linlinjava.litemall.core.storage.LocalStorage;
import org.linlinjava.litemall.db.dao.LitemallStorageMapper;
import org.linlinjava.litemall.db.domain.LitemallStorage;
import org.linlinjava.litemall.db.domain.LitemallStorageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@WebAppConfiguration
@RunWith(SpringRunner.class)
@SpringBootTest
public class LocalStorageTest {

    private final Log logger = LogFactory.getLog(LocalStorageTest.class);
    @Autowired
    private LocalStorage localStorage;

    @Autowired
    private LitemallStorageMapper storeManager;

    @Test
    public void test() throws IOException {
        String test = getClass().getClassLoader().getResource("litemall.png").getFile();
        File testFile = new File(test);
        localStorage.store(new FileInputStream(test), testFile.length(), "image/png", "litemall.png");
        Resource resource = localStorage.loadAsResource("litemall.png");
        String url = localStorage.generateUrl("litemall.png");
        logger.info("test file " + test);
        logger.info("store file " + resource.getURI());
        logger.info("generate url " + url);
    }

    @Test
    public void modifyUrls() {
        final String address = localStorage.getAddress();
        // 获取所有的litemallStorage
        LitemallStorageExample example = new LitemallStorageExample();
        example.createCriteria().getAllCriteria();
        final List<LitemallStorage> litemallStorages = storeManager.selectByExample(example);
        litemallStorages.forEach(litemallStorage -> {
            litemallStorage.setUrl(address + litemallStorage.getKey());
            storeManager.updateByPrimaryKeySelective(litemallStorage);
        });

    }

}