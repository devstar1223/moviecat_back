package com.moviecat.www.controller;

import com.moviecat.www.entity.MvcMenu;
import com.moviecat.www.repository.MvcMenuRepository;
import com.moviecat.www.service.MvcMenuService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


import java.sql.ClientInfoStatus;
import java.util.*;

@RestController
@RequestMapping("/api")
public class BasicController {

    @Autowired
    private MvcMenuService menuService;

    @GetMapping("/menuList")
    @Operation(summary = "메뉴명 리스트", description = "메뉴에 들어갈 게시판명을 보내주는 api")
    public Map<String, Object> getActiveMenus() {
        List<Map<String, Object>> menuList = menuService.getActiveMenus(); // 서비스에서 메뉴 리스트 받아온다.

        Map<String, Object> data = new HashMap<>(); // 만약 메뉴 데이터를 보내줘야 한다면, 필요하다.
        data.put("menu_list", menuList);

        Map<String, Object> response = new HashMap<>(); // 이 부분에 200과 데이터가 들어감, 오류 발생시 오류코드와 이유 보내게 수정
        response.put("code", 200);
        response.put("data", data);

        return response;
    }

}

