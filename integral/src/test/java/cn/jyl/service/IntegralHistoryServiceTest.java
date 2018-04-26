package cn.jyl.service;

import cn.jyl.model.IntegralHistory;
import com.sinosoft.microservice.common.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class IntegralHistoryServiceTest {


    @Autowired
    IntegralHistoryService integralHistoryService;

    @Test
    public void testadd(){
        IntegralHistory integralHistory = new IntegralHistory();
        integralHistory.setId("test");
        integralHistory.setIntegralId("test");
        integralHistory.setUpdatetime(new Timestamp(new Date().getTime()));
        integralHistory.setValue("value");
        IntegralHistory history = integralHistoryService.add(integralHistory);

    }

    @Test
    public void testfindAll(){
        List<IntegralHistory> list = integralHistoryService.getList();
    }
}