package com.ragdev.tracker.controller;

import com.ragdev.tracker.dto.ResApiDto;
import com.ragdev.tracker.security.UserDetailsImpl;
import com.ragdev.tracker.service.CheckListService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todo-check-list")
public class TodoCheckListController {

    private final CheckListService checkListService;

    public TodoCheckListController(CheckListService checkListService) {
        this.checkListService = checkListService;
    }

    @PostMapping("check")
    public ResponseEntity<ResApiDto<Object, Object>> check(@RequestParam Long checkId) {
        return ResponseEntity.ok(ResApiDto.ok( checkListService.check(checkId)));
    }

    @PostMapping("uncheck")
    public ResponseEntity<ResApiDto<Object, Object>> unCheck(@RequestParam Long checkId) {
        return ResponseEntity.ok(ResApiDto.ok( checkListService.unCheck(checkId)));
    }

    @PostMapping("insert-test")
    public ResponseEntity<ResApiDto<Object, Object>> insertTest(@AuthenticationPrincipal UserDetailsImpl userDetails,  @RequestParam Long taskId) {
        return ResponseEntity.ok(ResApiDto.ok( checkListService.insert(userDetails.getUser().getId(),taskId)));
    }

    @PostMapping("get-all")
    public ResponseEntity<ResApiDto<Object, Object>> getAll(@RequestParam(defaultValue = "true") Boolean status) {
        return ResponseEntity.ok(ResApiDto.ok(checkListService.getAll(status)));
    }
}
