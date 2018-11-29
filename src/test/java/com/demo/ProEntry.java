package com.demo;

import com.alibaba.fastjson.JSON;
import com.dao.StudentDao;
import com.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class ProEntry {

    @Autowired
    private Jedis jedis;

    @Autowired
    private StudentDao dao;

    @Test
    public void testDao() throws SQLException {
        Integer cmd = 1;
        long start = System.currentTimeMillis();
        //dao.selectById(1);
        dao.selectAllStu();
        System.out.println(System.currentTimeMillis() - start);
    }

}
