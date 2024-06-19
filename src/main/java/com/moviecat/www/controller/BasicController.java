package com.moviecat.www.controller;

import com.moviecat.www.service.MvcMainBoardService;
import com.moviecat.www.service.MvcMenuService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BasicController {

    private final MvcMenuService menuService;
    private final MvcMainBoardService mainBoardService;

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

    @GetMapping("/mainBoard")
    @Operation(summary = "메인 화면", description = "영화리뷰(1) 인기글3 일반7, 영화토크(2) 인기글3 일반7, 영화평점(4) 일반8")
    public ResponseEntity<String> mainBoard() {
        try {
            String jsonMainBoard = mainBoardService.mainBoard();
            return new ResponseEntity<>(jsonMainBoard, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("메인화면 출력 실패", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}

