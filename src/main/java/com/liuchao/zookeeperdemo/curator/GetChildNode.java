package com.liuchao.zookeeperdemo.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;


import java.util.Arrays;
import java.util.List;

public class GetChildNode {
    public static RetryPolicy retryPolicy=new RetryNTimes(3,1000);
    public static CuratorFramework curator;

    public static void main(String[] args) {
        getConnect();
        getChildNode("/");
    }

    private static void getConnect() {
         curator= CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(5000)
                .retryPolicy(retryPolicy)
                .build();
        curator.start();
    }

    /**
     * 获取跟节点下面的所有的值
     * @param basepath
     */
    private static void getChildNode(String basepath) {



        try {
            List<String> strings = curator.getChildren().forPath(basepath);
            if(strings!=null && strings.size()>0){
                for(String str:strings){
                    byte[] bytes = curator.getData().forPath(basepath);
                    System.out.println("节点："+basepath+" "+new String(bytes));
                    if("/".equals(basepath)){
                        getChildNode(basepath+str);
                    }else{
                        getChildNode(basepath+"/"+str);
                    }

                }
            }else{
                byte[] bytes = curator.getData().forPath(basepath.substring(0,basepath.length()));
                System.out.println("节点："+basepath+" "+new String(bytes));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
