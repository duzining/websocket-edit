package com.du.websocketedit.controller;

import com.du.websocketedit.entity.Text;
import com.du.websocketedit.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController

public class TextController {

    @Autowired
    private TextService service;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @RequestMapping(value = "/texts", method = RequestMethod.GET)
    public List<Map<String, Object>> findAll(){
        List<Map<String, Object>> list = service.findAll();
        return list;
    }

    @RequestMapping(value = "/updateText",method = RequestMethod.POST )
    public void update(@RequestBody Text text){
        String sql = "update txt set text=? where id=? ";
        jdbcTemplate.update(sql,text.getText(),text.getId());
    }
}
