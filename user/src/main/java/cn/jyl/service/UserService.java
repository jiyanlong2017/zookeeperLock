package cn.jyl.service;

import cn.jyl.model.User;
import com.sinosoft.microservice.common.JsonResult;
import com.sinosoft.microservice.common.service.BaseCRUDService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;
import java.util.logging.Logger;

@Service
@Slf4j
public class UserService extends BaseCRUDService<User, String> {

    @Autowired
    private RestTemplate restTemplate;

    private final String INTEGRAL = "http://integral";

    //创建用户的操作并操作integral服务添加积分
    @Transactional
    public User createUser(User user){

        JsonResult result = restTemplate.postForObject(INTEGRAL + "/userintegral/add/" + user.getId(), null, JsonResult.class);
        if(result.isSuccess()){
            return add(user);
        }

        return null;
    }

    /**
     * 更新用户信息的时候 会扣除 1个积分
     * @param userId
     * @param password
     * @return
     */
    @Transactional
    public JsonResult updateUser(String userId, String password) {

        User user = get(userId);

        log.info("触发用户更新接口， userid={}发生更新, 旧密码是{}， 新密码是{} ", userId, user.getPassword(), password);

        user.setPassword(password);

        //更新本服务数据库
        update(user);

        //更新Integral 服务数据库 ，进行减积分
        restTemplate.put(INTEGRAL + "/userintegral/decrease/" + userId, null);

        //如果此时异常出现
//        int i = 1/0;

        return JsonResult.success();

    }
}
