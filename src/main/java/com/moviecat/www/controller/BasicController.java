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
public Map<String, Object> getActiveMenus() {
    List<MvcMenu> activeMenus = menuRepository.findByUseYnOrderBySeqAsc("Y");

    List<Map<String, Object>> menuList = new ArrayList<>();

    for (MvcMenu menu : activeMenus) {
        Map<String, Object> menuMap = new HashMap<>();
        menuMap.put("menu_id", menu.getMenuId());
        menuMap.put("menu_nm", menu.getMenuNm());
        menuMap.put("menu_path", menu.getMenuPath());
        menuMap.put("seq", menu.getSeq());
        menuList.add(menuMap);
    }

    Map<String, Object> data = new HashMap<>();
    data.put("menu_list", menuList);

    Map<String, Object> response = new HashMap<>();
    response.put("code", 200); // 200 OK일 경우 이렇게 추가해주고 return 해주면 된다.
    response.put("data", data); // 다를경우, code랑 int값 적고, data랑 not found 같은거 적으면 될지도

    return response;
    }
}
