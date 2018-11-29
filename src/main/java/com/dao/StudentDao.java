package com.dao;

import com.annotation.Cache;
import com.entity.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

@Repository
public class StudentDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Cache
    public List<Student> selectAllStu() throws SQLException {

        List<Student> res = jdbcTemplate.query( "select * from student", new BeanPropertyRowMapper<Student>(Student.class));

        return res;
    }

    @Cache
    public Student selectById(Integer id) {
        String sql = "select * from student where sno = ?";
        Student res = jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<Student>(Student.class), id);
        return res;
    }


}
