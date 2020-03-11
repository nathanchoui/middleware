package com.github.nathan.zk;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.CreateMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author nathan.z
 * @date 2020/3/9.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {ClientFactory.class})
public class ZkCRUDTest {

    private final static String zkAddress = "localhost:2181,localhost:2182,localhost:2183";

    @Test
    public void testSetData() {
        CuratorFramework curatorFramework = ClientFactory.createSimple(zkAddress);
        curatorFramework.start();
        try {
            curatorFramework.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath("/test/crud/node-1", "hello,zk".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            CloseableUtils.closeQuietly(curatorFramework);
        }
    }

    @Test
    public void testSetTempData() {
        CuratorFramework client = ClientFactory.createSimple(zkAddress);
        client.start();
        try {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath("/test/crud/node-3", "hello,zk".getBytes());
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void testGetData() {
        CuratorFramework client = ClientFactory.createSimple(zkAddress);
        client.start();
        try {
            byte[] data = client.getData().forPath("/test/crud/node-1");
            System.out.println("===============");
            System.out.println(new String(data));
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void testUpdateData() {
        CuratorFramework client = ClientFactory.createSimple(zkAddress);
        client.start();
        try {
            client.setData().forPath("/test/crud/node-1", "hello zk again".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void testAsyncSetData() throws InterruptedException {
        CuratorFramework client = ClientFactory.createSimple(zkAddress);
        AsyncCallback.StringCallback callback = ((i, s, o, s1) -> System.out.println(
                " i = " + i + " | " +
                        "s = " + s + " | " +
                        "o = " + o + " | " +
                        "s1 = " + s1
        ));
        client.start();
        try {
            client.create()
                    .withMode(CreateMode.PERSISTENT)
                    .inBackground(callback)
                    .forPath("/test/crud/node-2", "hello, async".getBytes());
            TimeUnit.SECONDS.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void testGetChildren() {
        CuratorFramework client = ClientFactory.createSimple(zkAddress);
        client.start();
        try {
            List<String> list = client.getChildren().forPath("/test/crud");
            list.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            CloseableUtils.closeQuietly(client);
        }
    }

    @Test
    public void testDeleteData() {
        CuratorFramework client = ClientFactory.createSimple(zkAddress);
        client.start();
        try {
            client.delete().forPath("/test/crud/node-1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            CloseableUtils.closeQuietly(client);
        }
    }

}
