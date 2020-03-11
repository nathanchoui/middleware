package com.github.nathan.zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
 * @author nathan.z
 * @date 2020/3/9.
 */
public class ClientFactory {

    public static void main(String[] args) {

    }

    public static CuratorFramework createSimple(String connectionString) {
        return CuratorFrameworkFactory.newClient(
                connectionString, new ExponentialBackoffRetry(1000, 3));
    }

    public static CuratorFramework createWithOptions(String connectionString, RetryPolicy retryPolicy
            , int connectionTimeoutMs, int sessionTimeoutMs) {
        return CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(retryPolicy)
                .connectionTimeoutMs(connectionTimeoutMs)
                .sessionTimeoutMs(sessionTimeoutMs)
                .build();
    }
}
