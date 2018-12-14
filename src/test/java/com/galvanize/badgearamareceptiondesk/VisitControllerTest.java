package com.galvanize.badgearamareceptiondesk;

import com.galvanize.badgearamareceptiondesk.controller.VisitController;
import com.galvanize.badgearamareceptiondesk.entity.ExtendedPersonFrontEnd;
import com.galvanize.badgearamareceptiondesk.service.RestCallToRegisterService;
import com.galvanize.badgearamareceptiondesk.service.VisitorService;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(value = VisitController.class, secure = false)
public class VisitControllerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(VisitControllerTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VisitorService visitService;

    @MockBean
    private RestCallToRegisterService restCallToRegisterService;

    @Test
    public void findVisitorByPhone_Test() throws Exception {
        ExtendedPersonFrontEnd person = ExtendedPersonFrontEnd.builder().build();
        String phoneNumber = "222-333-4444";
        person.setPhoneNumber("222-333-4444");
        when(visitService.findVisitorByPhone(phoneNumber)).thenReturn(person);
        //LOGGER.info("perform is: {}", perform);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/visit/visitor/lookup/{phoneNumber}", phoneNumber)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andDo(print())
                .andReturn();
        verify(visitService, times(1)).findVisitorByPhone(phoneNumber);
    }

    /*@Test
    public void findVisitorByPhoneWhenNull_Test() throws Exception {
        String phoneNumber = "222-333-4444";
        when(visitService.findVisitorByPhone(phoneNumber)).thenReturn(null);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/visit/visitor/lookup/{phoneNumber}", phoneNumber)
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andReturn();
        verify(visitService, times(1)).findVisitorByPhone(phoneNumber);
    }*/

}