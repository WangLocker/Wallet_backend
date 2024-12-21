package com.example.payment.controller;

import com.example.payment.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/req")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/paysubmit")
    public ResponseEntity<?> paySubmit(@RequestBody Map<?, ?> submitData) {
        String userName = (String) submitData.get("username");
        Map<?, ?> payForm = (Map<?, ?>) submitData.get("payForm");
        String infoPayee = (String) payForm.get("inforPayee");
        String payeeType = (String) payForm.get("payeeType");
        Double amount = Double.parseDouble((String) payForm.get("amount"));
        String memo = (String) payForm.get("memo");
        int ret = requestService.paymentCheckAndInsert(userName, infoPayee, payeeType, amount, memo);
        if (ret == -1) {
            return ResponseEntity.status(201).body("Sender not found");
        } else if (ret == -2) {
            return ResponseEntity.status(202).body("Recipient not found");
        } else if (ret == -3) {
            return ResponseEntity.status(203).body("Invalid amount");
        } else {
            return ResponseEntity.ok("Payment successfully submitted");
        }
    }

    public ResponseEntity<?> addCard(@RequestBody Map<?, ?> form) {
        String userName = (String) form.get("User");
        Map<?, ?> cardForm = (Map<?, ?>) form.get("CardData");
        String bankId = (String) cardForm.get("a_bank_id");
        String accountNum = (String) cardForm.get("a_account_num");
        int ret = requestService.addCard(userName, bankId, accountNum);
        if (ret == 0) return ResponseEntity.ok("success");
        else return ResponseEntity.status(201).body("user not found");
    }


}
