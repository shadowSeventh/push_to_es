//package com.shadow.push.es;
//
//import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.Settings;
//import org.elasticsearch.common.transport.InetSocketTransportAddress;
//import org.elasticsearch.transport.client.PreBuiltTransportClient;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import java.net.InetAddress;
//import java.net.UnknownHostException;
//
//@Component
//public class Init {
//
//
//    @Bean
//    TransportClient transportClient() throws UnknownHostException {
//        Settings settings = Settings.builder().put("client.transport.ignore_cluster_name", true).build();
//        return new PreBuiltTransportClient(settings).
//                addTransportAddress(getTransportAddress());
//    }
//
//
//    private static InetSocketTransportAddress getTransportAddress() throws UnknownHostException {
//        String host = System.getenv("ES_TEST_HOST");
//        String port = System.getenv("ES_TEST_PORT");
//
//        if (host == null) {
//            host = "es-cn-0pp0fbmym0002j868.elasticsearch.aliyuncs.com";
//            System.out.println("ES_TEST_HOST enviroment variable does not exist. choose default 'localhost'");
//        }
//
//        if (port == null) {
//            port = "9300";
//            System.out.println("ES_TEST_PORT enviroment variable does not exist. choose default '9300'");
//        }
//
//        System.out.println(String.format("Connection details: host: %s. port:%s.", host, port));
//        return new InetSocketTransportAddress(InetAddress.getByName(host), Integer.parseInt(port));
//    }
//}
