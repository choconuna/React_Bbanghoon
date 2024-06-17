package com.example.blog_back.controller;

import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class TableInfoController {

    private static final Logger logger = LoggerFactory.getLogger(TableInfoController.class);

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/table")
    public String getTableInfo(Model model) {
        String tableName = "user"; // 테이블 이름
        String databaseName = "bbanghoondb"; // 데이터베이스 이름
        try {
            List<Map<String, Object>> columns = jdbcTemplate.queryForList(
                "SELECT TABLE_NAME, COLUMN_NAME, DATA_TYPE, COLUMN_KEY FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = ?",
                new Object[] { databaseName });
    
            List<Map<String, Object>> tableData = jdbcTemplate.queryForList(
                "SELECT * FROM `" + databaseName + "`.`" + tableName + "`");
    
            System.out.println("database result: " + tableName + " " + columns + " " + tableData);
            model.addAttribute("tableName", tableName);
            model.addAttribute("columns", columns);
            model.addAttribute("tableData", tableData); // 테이블 데이터를 모델에 추가
    
            return "table-info.html"; // "table-info" 뷰 이름 반환
        } catch (Exception e) {
            // 에러 처리
            logger.error("Error fetching table info", e);
            model.addAttribute("errorMessage", "에러 메시지 출력");
            System.out.println("Error message: " + e.getMessage());
            e.printStackTrace();
            return "error.html"; // 오류 페이지로 이동
        }
    }    
}
