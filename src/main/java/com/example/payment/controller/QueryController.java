package com.example.payment.controller;

import com.example.payment.entity.Account;
import com.example.payment.service.QueryService;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/qry")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * 处理查询用户账户请求
     * @param userId 用户 ID
     * @return 用户账户
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/getCardData")
    public ResponseEntity<?> queryUserAccounts(@RequestBody Integer userId) {
        List<Account> cardList = queryService.getUserCard(userId);
        List list = new ArrayList();
        for (Account account : cardList) {
            if (account.getPrimary()) {
                Map<String, Object> mainCard = new HashMap<>();
                mainCard.put("card_prio", 0);
                mainCard.put("card_num", account.getAccountNumber());
                mainCard.put("card_status", account.getVerified() ? 0 : 1);
                list.add(mainCard);
                break;
            }
        }
        if (list.isEmpty()) {
            return ResponseEntity.status(201).body("No main card");
        }
        List<Map<String, String>> tableList = new ArrayList();
        for (Account account : cardList) {
            Map<String, String> tempMap = new HashMap<>();
            tempMap.put("card_num", account.getAccountNumber());
            tempMap.put("status", account.getVerified() ? "状态正常" : "点此验证");
            tempMap.put("bank", account.getBankId());
            tableList.add(tempMap);
        }
        list.add(tableList);
        list.add(new HashMap<>());
        return ResponseEntity.ok(list);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getMonthlyStats")
    public ResponseEntity<?> queryMonthlyStats(@RequestBody Integer userId) {
        List<Double> result = queryService.getUserMonthlyData(userId);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/userofemail")
    public ResponseEntity<?> queryUserOfEmail(@RequestBody String email) {
        String userName = queryService.getUserNameOfEmail(email);
        if (userName == null) {
            return ResponseEntity.status(201).body("No such user found");
        }
        else {
            return ResponseEntity.ok(userName);
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getTransactionData")
    public ResponseEntity<?> queryTransactionData(@RequestBody Integer userId) {
        List<Map<String, String>> result = queryService.getTransactionData(userId);
        return ResponseEntity.ok(result);
    }




}
