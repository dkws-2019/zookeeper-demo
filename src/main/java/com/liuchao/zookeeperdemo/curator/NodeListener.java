package com.liuchao.zookeeperdemo.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.concurrent.TimeUnit;

public class NodeListener {
    public static RetryPolicy retryPolicy=new RetryNTimes(3,1000);

    public static void main(String[] args) throws Exception {
        CuratorFramework curator= CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .sessionTimeoutMs(5000)
                .connectionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        curator.start();
        byte[] bytes = curator.getData().forPath("/liuchao/address");
        System.out.println("节点的值 为："+new String(bytes));

        PathChildrenCache pathChildrenCache=new PathChildrenCache(curator,"/liuchao",true);
        pathChildrenCache.start();

        pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                PathChildrenCacheEvent.Type type = pathChildrenCacheEvent.getType();
                switch (type){
                    case CHILD_ADDED:
                        System.out.println("新增子节点的路径："+pathChildrenCacheEvent.getData().getPath());
                        System.out.println("新增子节点的值："+new String(pathChildrenCacheEvent.getData().getData()));
                        break;
                    case CHILD_UPDATED:
                        System.out.println("修改子节点的路径："+pathChildrenCacheEvent.getData().getPath());
                        System.out.println("修改子节点的值："+new String(pathChildrenCacheEvent.getData().getData()));
                        break;
                    case CHILD_REMOVED:
                        System.out.println("新增子节点的路径："+pathChildrenCacheEvent.getData().getPath());
                        System.out.println("新增子节点的值："+new String(pathChildrenCacheEvent.getData().getData()));
                        break;
                }
            }
        });

        // 为了看见控制台输出，我们不能让main线程死得太早
        TimeUnit.MILLISECONDS.sleep(100000);

    }
}
