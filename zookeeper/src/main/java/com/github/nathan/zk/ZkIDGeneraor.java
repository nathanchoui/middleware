package com.github.nathan.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;

/**
 * @author nathan.z
 * @date 2020/3/10.
 */
public class ZkIDGeneraor {

    private final static String zkAddress = "localhost:2181,localhost:2182,localhost:2183";

    public String generateID(String nodeName) {
        String str = createSeqNode(nodeName);
        if (null == str) {
            return null;
        }
        int index = str.lastIndexOf(nodeName);
        if (index >= 0) {
            index += nodeName.length();
            return index <= str.length() ? str.substring(index) : "";
        }
        return str;
    }

    private String createSeqNode(String pathPrefix) {
        CuratorFramework client = ClientFactory.createSimple(zkAddress);
        client.start();
        try {
            String destPath = client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT_SEQUENTIAL)
                    .forPath("/" + pathPrefix);
            return destPath;
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            CloseableUtils.closeQuietly(client);
        }
        return null;
    }
}
