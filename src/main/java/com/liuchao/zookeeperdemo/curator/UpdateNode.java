package com.liuchao.zookeeperdemo.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.zookeeper.data.Stat;

import java.util.concurrent.TimeUnit;

public class UpdateNode {
    public static RetryPolicy retryPolicy=new RetryNTimes(3,1000);

    public static void main(String[] args) throws InterruptedException {
        updateNode();
        TimeUnit.SECONDS.sleep(1000);
    }

    private static void updateNode() {
        CuratorFramework curator=CuratorFrameworkFactory.builder()
                                        .connectString("localhost:2181")
                                        .sessionTimeoutMs(5000)
                                        .connectionTimeoutMs(5000)
                                        .retryPolicy(retryPolicy)
                                        .build();
        curator.start();
        try {

            Stat stat=new Stat();
            //读取数据和状态值
            byte[] bytes = curator.getData().storingStatIn(stat).forPath("/liuchao");
            System.out.println(new String(bytes)+" 状态值 为"+stat.toString()+" "+stat.getVersion());

            curator.setData().forPath("/liuchao", "this is my first update node".getBytes());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
