package com.moviecat.www.controller;

import com.moviecat.www.entity.MvcMenu;
import com.moviecat.www.repository.MvcMenuRepository;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ClientInfoStatus;
import java.util.*;

@RestController
public class BasicController {

    private final MvcMenuRepository menuRepository;
    private String responseCode;

    @Autowired
    public BasicController(MvcMenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

@GetMapping("/menuList")
@Operation(summary = "메뉴명 리스트", description = "메뉴에 들어갈 게시판명을 보내주는 api")
    public List<Map<String, Object>> getActiveMenus() {
        List<MvcMenu> activeMenus = menuRepository.findByUseYnOrderBySeqAsc("Y");

        List<Map<String, Object>> result = new ArrayList<>();

        for (MvcMenu menu : activeMenus) {
            Map<String, Object> menuMap = new HashMap<>();
            menuMap.put("menu_id", menu.getMenuId());
            menuMap.put("menu_nm", menu.getMenuNm());
            menuMap.put("menu_path", menu.getMenuPath());
            menuMap.put("seq", menu.getSeq());
            result.add(menuMap);
        }

        return result;
    }
}
