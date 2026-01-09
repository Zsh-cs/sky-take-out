package com.sky;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class HttpClientTest {

    private final String GET_URL="http://localhost:8080/user/shop/status";

    // 测试通过HttpClient发送GET请求
    @Test
    public void testGET() throws IOException {

        // 1.创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 2.创建请求对象
        HttpGet httpGet=new HttpGet(GET_URL);

        // 3.发送请求
        CloseableHttpResponse response = httpClient.execute(httpGet);

        // 4.获取服务端返回的状态码和数据
        System.out.println("服务端返回的状态码为："+response.getStatusLine().getStatusCode());
        System.out.println("服务端返回的数据为："+EntityUtils.toString(response.getEntity()));

        // 5.关闭资源
        response.close();
        httpClient.close();
    }


    // 测试通过HttpClient发送POST请求
    @Test
    public void testPOST(){

    }
}
