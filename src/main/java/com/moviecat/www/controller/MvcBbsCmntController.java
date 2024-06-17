package com.moviecat.www.controller;

import com.moviecat.www.dto.MvcCmntDto;
import com.moviecat.www.entity.MvcBbsCmnt;
import com.moviecat.www.service.MvcBbsCmntService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MvcBbsCmntController {

    private final MvcBbsCmntService mvcBbsCmntService;
    @PostMapping("/bbsWriteCmnt")
    @Operation(summary = "댓글 작성", description ="답글 X")
    public ResponseEntity<String> bbsWriteCmnt(@ModelAttribute MvcCmntDto mvcCmntDto){
        mvcBbsCmntService.bbsWriteCmnt(mvcCmntDto);
        try{
            return new ResponseEntity<>("댓글 작성 성공", HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>("오류 발생", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
