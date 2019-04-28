package com.nbd.ocp.common.log;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
@EnableDiscoveryClient
@ComponentScan(basePackages="com.nbd.ocp.core.jpa")
public class OcpBusiLogTest {
    @Autowired
    private BusiController busiController;
    @Test
    public void testBusiLog() throws InterruptedException {
        busiController.test();
    }
    @Test
    public void testBusiLogError() throws InterruptedException {
        busiController.testError();
    }

}