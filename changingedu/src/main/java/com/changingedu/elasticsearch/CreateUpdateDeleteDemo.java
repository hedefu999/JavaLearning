package com.changingedu.elasticsearch;

import com.qingqing.common.util.CollectionsUtil;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CreateUpdateDeleteDemo {
    public static final String HOST = "172.22.12.3345435";//"http://api.es.idc.cedu.cn";
    public static final Integer PORT = 9201;
    static RestHighLevelClient client = buildClient();

    static RestHighLevelClient buildClient(){
        List<HttpHost> hosts = new ArrayList<>();
        String[] ipAddresses = {HOST};
        for (String ipAddress : ipAddresses){
            HttpHost httpHost = new HttpHost(ipAddress.trim(), PORT);
            hosts.add(httpHost);
        }
        HttpHost[] httpHosts = {};
        RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(hosts.toArray(httpHosts)));
        return client;
    }

    static class IndexCreateIfNotExist{
        static Map<Integer, String> map = new HashMap<Integer, String>(){{put(12,"jack");put(13,"lucy");}};
        static void tempTest(){
            boolean isAllPullFinish = true;
            do{
                for (Map.Entry<Integer, String> entry : map.entrySet()){
                    List<Integer> list = Arrays.asList(12,13,14);
                    int count = 0;
                    while (CollectionsUtil.isNotEmpty(list)){
                        for (Integer number : list){
                            //processing ...
                        }
                        count++;
                        if (count == 3){
                            isAllPullFinish = false;
                            break;
                        }
                        list = Arrays.asList(16,18);
                    }

                }
            }while (isAllPullFinish);
        }
        public static void main(String[] args) {
            tempTest();
        }
    }
    static class MappingAPIDemo{

    }
}
