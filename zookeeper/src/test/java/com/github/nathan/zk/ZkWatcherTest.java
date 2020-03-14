package com.github.nathan.zk;

import com.github.nathan.zk.config.ZookeeperAutoConfiguration;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * @author nathan.z
 * @date 2020/3/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ZookeeperAutoConfiguration.class})
public class ZkWatcherTest {

    @Autowired
    private CuratorFramework client;

    private final String workerPath = "/test/listener/remoteNode";

    private final String subWorkPath = "/test/listener/remoteNode/id-";

    @Before
    public void before() throws Exception {
        client.start();
        if (null == client.checkExists().forPath(workerPath)) {
            client
                    .create().creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(workerPath, null);
        }
    }

    @After
    public void after() {
        CloseableUtils.closeQuietly(client);
    }

    @Test
    public void testWatcher() {
        try {
            Watcher w = watchedEvent -> System.out.println("=============" + watchedEvent);
            client.getData().usingWatcher(w).forPath(workerPath);
            client.setData().forPath(workerPath, "first time update".getBytes());
            client.setData().forPath(workerPath, "second time update".getBytes());
            // 两次数据变动，只监听到一次，适用于会话超时、授权失败等场景。
            TimeUnit.SECONDS.sleep(10);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Node cache  节点监听
     * Path Cache znode的子节点监听
     * Tree Cache znode 和 子节点
     */
    @Test
    public void testNodeCache() {
        try {
            NodeCache nodeCache = new NodeCache(client, workerPath);
            NodeCacheListener listener = () -> {
                ChildData childData =   nodeCache.getCurrentData();
                System.out.println("=====================");
                System.out.println("data: " + childData.getData());
                System.out.println("stat: " + childData.getStat());
                System.out.println("=====================");
            };
            nodeCache.getListenable().addListener(listener);
            nodeCache.start();
            client.setData().forPath(workerPath, "1".getBytes());
            TimeUnit.SECONDS.sleep(2);
            client.setData().forPath(workerPath, "2".getBytes());
            TimeUnit.SECONDS.sleep(2);
            client.setData().forPath(workerPath, "3".getBytes());
            TimeUnit.SECONDS.sleep(2);
            client.setData().forPath(workerPath, "4".getBytes());
            TimeUnit.SECONDS.sleep(2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
