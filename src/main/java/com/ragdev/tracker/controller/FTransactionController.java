package com.ragdev.tracker.controller;

import com.ragdev.tracker.dto.ReqFTransactionDto;
import com.ragdev.tracker.dto.ReqGetAllFTransDto;
import com.ragdev.tracker.dto.ReqGetTotalDto;
import com.ragdev.tracker.dto.ResApiDto;
import com.ragdev.tracker.security.UserDetailsImpl;
import com.ragdev.tracker.service.FinanceTransactionService;
import org.apache.coyote.Response;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;

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


    @PostMapping("/type")
    public ResponseEntity<ResApiDto<Object, Object>> getType() {
        return ResponseEntity.ok(ResApiDto.ok(fTransService.getType()));
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<ResApiDto<Object,Object>> delete(@PathVariable(name = "id") Long id) {
        fTransService.deleteById(id);
        return ResponseEntity.ok(ResApiDto.ok(null));
    }

    @PostMapping("/getAll")
    public ResponseEntity<ResApiDto<Object,Object>> getAll(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(ResApiDto.ok(fTransService.getAll(user.getUser().getId())));
    }

    @PostMapping("/getCurrentMonth")
    public ResponseEntity<ResApiDto<Object, Object>> getCurrMonth(@AuthenticationPrincipal UserDetailsImpl user) {
        return ResponseEntity.ok(ResApiDto.ok(fTransService.getFTransCurrMonth(user.getUser().getId())));
    }

    @PostMapping("/getTotal")
    public ResponseEntity<ResApiDto<Object, Object>> getTotal(@AuthenticationPrincipal UserDetailsImpl user, @RequestBody ReqGetTotalDto dto) {
        return ResponseEntity.ok(ResApiDto.ok(fTransService.getTotal(user.getUser().getId(), dto.getStart(), dto.getEnd())));
    }

    @PostMapping("/getAllType")
    public ResponseEntity<ResApiDto<Object,Object>> getAllWithType(@AuthenticationPrincipal UserDetailsImpl user,
                                                                   @RequestBody ReqGetAllFTransDto dto,
                                                                   @RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "15")  int size){
        return ResponseEntity.ok(ResApiDto.ok(fTransService.getAllByTransactionWithType(user.getUser().getId(), dto, page, size)));
    }

    @PostMapping("/getMonthlyIncomeExpense")
    public ResponseEntity<ResApiDto<Object, Object>> findMonthlyIncomeExpense(@AuthenticationPrincipal UserDetailsImpl user,
                                                                              @RequestParam("year") int year){
        return ResponseEntity.ok(ResApiDto.ok(fTransService.findMonthlyIncomeExpense(user.getUser().getId(), year)));
    }
}
