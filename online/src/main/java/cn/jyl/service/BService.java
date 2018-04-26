package cn.jyl.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 纪炎龙.
 * 创建时间 2018/2/3.
 */
@Service
public class BService {

    @Autowired
    public RestTemplate restTemplate;


    @HystrixCommand(fallbackMethod = "error", groupKey = "queryBService", commandKey = "queryBService",  threadPoolKey = "queryBService")
    public List queryBService(){

        ResponseEntity<List> entity = restTemplate.getForEntity("http://svcb-service/query", List.class);

        switch (entity.getStatusCode()) {

            case OK:
                System.out.println("查询成功");
                break;
        }

        return entity.getBody();
    }

    public List error(Throwable throwable){

        throwable.printStackTrace();

        return new ArrayList();
    }
}
