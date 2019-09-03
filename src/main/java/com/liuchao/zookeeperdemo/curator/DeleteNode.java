package com.liuchao.zookeeperdemo.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

public class DeleteNode {
    public static RetryPolicy retryPolicy=new RetryNTimes(3,1000);

    public static void main(String[] args) {
        deleteNode();

    }

    private static void deleteNode() {
        CuratorFramework curator= CuratorFrameworkFactory.builder()
                                    .connectString("localhost:2181")
                                    .connectionTimeoutMs(5000)
                                    .sessionTimeoutMs(5000)
                                    .retryPolicy(retryPolicy)
                                    .build();
        curator.start();

        try {
            //无子节点的删除
            //curator.delete().forPath("/liuchao");
            //带子节点的删除
            curator.delete().deletingChildrenIfNeeded().forPath("/liuchao");
            System.out.println("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
