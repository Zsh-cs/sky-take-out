package com.sky;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class HttpClientTest {

    private final String GET_URL="http://localhost:8080/user/shop/status";
    private final String POST_URL="http://localhost:8080/admin/employee/login";

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
    public void testPOST() throws IOException {

        // 1.创建HttpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 2.创建请求对象
        HttpPost httpPost=new HttpPost(POST_URL);
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("username","admin");
        jsonObject.put("password","123456");
        StringEntity entity=new StringEntity(jsonObject.toString());
        entity.setContentEncoding("utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);

        // 3.发送请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        // 4.解析返回结果
        System.out.println("服务端返回的状态码为："+response.getStatusLine().getStatusCode());
        System.out.println("服务端返回的数据为："+EntityUtils.toString(response.getEntity()));

        // 5.关闭资源
        response.close();
        httpClient.close();
    }
}
