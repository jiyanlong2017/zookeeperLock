package cn.jyl.service;

import cn.jyl.model.Message;
import com.sinosoft.microservice.common.service.BaseCRUDService;
import org.springframework.stereotype.Service;

@Service
public class MessageService extends BaseCRUDService<Message, String> {
}
