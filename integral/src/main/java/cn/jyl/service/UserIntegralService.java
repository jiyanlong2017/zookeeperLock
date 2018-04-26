package cn.jyl.service;

import cn.jyl.model.UserIntegral;
import com.sinosoft.microservice.common.service.BaseCRUDService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserIntegralService extends BaseCRUDService<UserIntegral, String> {


    @Transactional
    public UserIntegral createUserIntegrel(UserIntegral userIntegral){

        return add(userIntegral);

    }

    /**
     * 积分递减 ， 每更新一次用户，减一个积分
     * @param userId
     */
    @Transactional
    public boolean decrease(String userId) {

        UserIntegral userIntegral = getOneByField("userid", userId);

        String value = userIntegral.getValue();

        Integer integer = Integer.valueOf(value);

        //积分大于0 再减
        if(integer > 0) {
            integer -= 1;
            userIntegral.setValue(String.valueOf(integer));
            update(userIntegral);
            return true;
        } else {

            return false;
        }



    }
}
