package com.ragdev.tracker.controller;

import com.ragdev.tracker.dto.ReqFTransactionDto;
import com.ragdev.tracker.dto.ResApiDto;
import com.ragdev.tracker.security.UserDetailsImpl;
import com.ragdev.tracker.service.FinanceTransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/finance-transaction")
public class FTransactionController {

    public final FinanceTransactionService fTransService;

    public FTransactionController(FinanceTransactionService fTransService) {
        this.fTransService = fTransService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResApiDto<Object, Object>> create(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody ReqFTransactionDto dto) {
        return ResponseEntity.ok(ResApiDto.created(fTransService.create(user.getUser(), dto)));
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<ResApiDto<Object, Object>> update(@PathVariable(name = "id") Long id, @RequestBody ReqFTransactionDto dto) {
        return ResponseEntity.ok(ResApiDto.ok(fTransService.update(id, dto)));
    }


}
