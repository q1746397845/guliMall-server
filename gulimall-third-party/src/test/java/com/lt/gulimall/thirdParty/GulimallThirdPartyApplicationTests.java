package com.lt.gulimall.thirdParty;

import com.aliyun.oss.OSSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@SpringBootTest
class GulimallThirdPartyApplicationTests {


    @Autowired
    private OSSClient ossClient;

    @Test
    void contextLoads() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(new File("C:\\Users\\Hasee\\Desktop\\对外新增接口文档.docx"));
        ossClient.putObject("ggulimall-lt","对外新增接口文档.docx",fileInputStream);
        ossClient.shutdown();
    }
}
