package cn.jyl.dao;

import cn.jyl.model.Message;
import com.sinosoft.microservice.common.repository.BaseJpaRepository;

public interface MessageDao extends BaseJpaRepository<Message, String> {
}
