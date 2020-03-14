package com.github.nathan.zk.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author nathan.z
 * @date 2020/3/11.
 */
@Configuration
public class ZookeeperAutoConfiguration {

    private String zkServerList = "localhost:2181,localhost:2182,localhost:2183";

    @Bean
    public CuratorFramework curatorFramework() {
        return CuratorFrameworkFactory.newClient(
                zkServerList, new ExponentialBackoffRetry(1000, 3));
    }
}
