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
        String infoPayee = (String) payForm.get("infoPayee");
        String payeeType = (String) payForm.get("payeeType");
        Double amount = Double.parseDouble((String) payForm.get("amount"));
        String memo = (String) payForm.get("memo");
        Map ret = requestService.paymentCheckAndInsert(userName, infoPayee, payeeType, amount, memo);
        if (ret == null) {
            return ResponseEntity.status(201).body("error");
//        } else if (ret == -2) {
//            return ResponseEntity.status(202).body("Recipient not found");
//        } else if (ret == -3) {
//            return ResponseEntity.status(203).body("Invalid amount");}
        }  else {
            return ResponseEntity.ok(ret);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/addCard")
    public ResponseEntity<?> addCard(@RequestBody Map<?, ?> form) {
        String userName = (String) form.get("User");
        Map<?, ?> cardForm = (Map<?, ?>) form.get("CardData");
        String bankId = (String) cardForm.get("a_bank_id");
        String accountNum = (String) cardForm.get("a_account_num");
        int ret = requestService.addCard(userName, bankId, accountNum);
        if (ret == 0) return ResponseEntity.ok("success");
        else return ResponseEntity.status(201).body("user not found");
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/handleTrans")
    public ResponseEntity<?> handleTrans(@RequestBody Map<?, ?> form) {
        String userName = (String) form.get("User");
        Map<?, ?> transForm = (Map<?, ?>) form.get("Trans");
        String transId = (String) transForm.get("t_id");
        String requesterId = (String) transForm.get("t_requester_id");
        String recipientId = (String) transForm.get("t_recipient_id");
        int result = requestService.handleRequest(Integer.parseInt(transId), requesterId, recipientId);
        if (result == 0) return ResponseEntity.ok("success");
        else return ResponseEntity.status(201).body("error");
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/singleFetch")
    public ResponseEntity<?> singleFetch(@RequestBody Map<?, ?> form) {
        String userName = (String) form.get("username");
        Map<?, ?> fetchForm = (Map<?, ?>) form.get("fetchForm");
        Map fetchOutForm = requestService.submitFetch(userName, fetchForm);
        if (fetchOutForm == null) return ResponseEntity.status(201).body("user not found");
        else return ResponseEntity.ok(fetchOutForm);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/multiFetch")
    public ResponseEntity<?> multiFetch(@RequestBody Map<?, ?> form) {
        String userName = (String) form.get("username");
        Map<?, ?> fetchForm = (Map<?, ?>) form.get("fetchForm");
        Map fetchOutForm = requestService.submitFetch(userName, fetchForm);
        if (fetchOutForm == null) return ResponseEntity.status(201).body("user not found");
        else return ResponseEntity.ok(fetchOutForm);
    }

}
