package com.ragdev.tracker.controller;

import com.ragdev.tracker.dto.ReqFCategoryDto;
import com.ragdev.tracker.dto.ResApiDto;
import com.ragdev.tracker.security.UserDetailsImpl;
import com.ragdev.tracker.service.FinanceCategoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/financial-category")
public class FCategoryController {

    private final FinanceCategoryService fCategoryService;

    public FCategoryController(FinanceCategoryService fCategoryService) {
        this.fCategoryService = fCategoryService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResApiDto<Object, Object>> register(@AuthenticationPrincipal UserDetailsImpl user
            , @Valid @RequestBody ReqFCategoryDto dto) {
        return ResponseEntity.ok(ResApiDto.created(fCategoryService.create(user.getUser(), dto)));
    }

    @PostMapping("/update")
    public ResponseEntity<ResApiDto<Object, Object>> update(@RequestBody ReqFCategoryDto dto) {
        return ResponseEntity.ok(ResApiDto.ok(fCategoryService.update(dto)));
    }

    @PostMapping("/inActive/{id}")
    public ResponseEntity<ResApiDto<Object, Object>> inActive(@PathVariable(name = "id") Long id) {
        return  ResponseEntity.ok(ResApiDto.ok(fCategoryService.inActive(id)));
    }

    @PostMapping("/active/{id}")
    public ResponseEntity<ResApiDto<Object, Object>> active(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(ResApiDto.ok(fCategoryService.active(id)));
    }

    @PostMapping("/getAll")
    public ResponseEntity<ResApiDto<Object, Object>> getAll(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(ResApiDto.ok(fCategoryService.getAll(user.getUser().getId())));
    }
}
