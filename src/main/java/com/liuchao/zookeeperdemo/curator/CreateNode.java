package com.liuchao.zookeeperdemo.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.CreateMode;

import java.util.concurrent.TimeUnit;

public class CreateNode {
    //每1秒重试一次总共重试三次
    private static RetryPolicy retryPolicy= new RetryNTimes(3,1000);

    public static void main(String[] args) throws InterruptedException {
        createNode();
        TimeUnit.SECONDS.sleep(1000L);
    }

    private static void createNode() {
        CuratorFramework curator= CuratorFrameworkFactory
                .builder()
                .connectString("localhost:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        curator.start();
        try {
            String newNode=curator.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .forPath("/liuchao/address","localhost:8080".getBytes());
            System.out.println("创建 新节点成功"+newNode);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
