package com.moviecat.www.service;

import com.moviecat.www.entity.MvcMenu;
import com.moviecat.www.repository.MvcMenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MvcMenuService {

    private final MvcMenuRepository mvcMenuRepository;
    public List<Map<String, Object>> getActiveMenus() { // 메뉴 정보만을 주는 service모듈
        List<MvcMenu> activeMenus = mvcMenuRepository.findByUseYnOrderBySeqAsc("Y"); // 활성화된 메뉴를 리스트로 담아와서
        List<Map<String, Object>> menuList = new ArrayList<>(); // menuList라는 새로운 List의 원소를 만든다

        for (MvcMenu menu : activeMenus) { // 액티브 메뉴라는걸 for문으로 돌려서 (하나 뜯어서 데이터 다 넣기)
            Map<String, Object> menuMap = new HashMap<>(); // 가장 작은 단위의 값을 만들자
            menuMap.put("menu_id", menu.getMenuId()); // 액티브 메뉴의 순서대로 맵에 쭉쭉 넣자
            menuMap.put("menu_nm", menu.getMenuNm());
            menuMap.put("menu_path", menu.getMenuPath());
            menuMap.put("seq", menu.getSeq());
            menuList.add(menuMap); // 그리고 작은 단위 값을 리스트에 추가한다.
        }

        return menuList; // 반환할때 각각의 메뉴들의 정보를 담은 list들을 반환해준다.
    }
}
