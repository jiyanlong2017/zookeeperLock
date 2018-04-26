package cn.jyl.dao;

import cn.jyl.model.User;
import com.sinosoft.microservice.common.repository.BaseJpaRepository;

public interface UserDao extends BaseJpaRepository<User, String> {
}
