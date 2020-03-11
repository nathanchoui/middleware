package com.github.nathan.zk;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author nathan.z
 * @date 2020/3/10.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ZkIDGeneraor.class})
public class ZkIDGeneratorTest {

    @Autowired
    private ZkIDGeneraor zkIDGeneraor;

    @Test
    public void testIDGenerator() {
        for (int i = 0; i < 10; i ++) {
            System.out.println(zkIDGeneraor.generateID("test/id-"));
        }
    }
}
