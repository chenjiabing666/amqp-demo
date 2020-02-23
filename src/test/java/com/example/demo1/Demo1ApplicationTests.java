package com.example.demo1;

import com.example.demo1.service.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Demo1ApplicationTests {

    @Autowired
    private MessageServiceImpl messageService;

    @Test
    void contextLoads() {
        messageService.sendMsg("陈加兵");
    }

}
