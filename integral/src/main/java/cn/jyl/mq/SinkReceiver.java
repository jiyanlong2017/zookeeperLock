package cn.jyl.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@Slf4j
@EnableBinding(value = {Sink.class})
public class SinkReceiver {


    @StreamListener(Sink.INPUT)//将被修饰的方法注册为消息中间件上数据流的事件监听器，
    public void receive(Object payload) {

        log.info("Received: " + payload);

    }

}