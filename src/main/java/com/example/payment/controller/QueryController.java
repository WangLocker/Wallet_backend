package com.example.payment.controller;

import com.example.payment.entity.Account;
import com.example.payment.entity.Payment;
import com.example.payment.entity.User;
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
     * @param userName 用户名
     * @return 用户账户
     */
    @CrossOrigin(origins = "*")
    @PostMapping("/getCardData")
    public ResponseEntity<?> queryUserAccounts(@RequestBody String userName) {
        Integer userId = queryService.getUserId(userName);
        if (userId == null) return ResponseEntity.status(202).body("No such user");
        List<Account> cardList = queryService.getUserCard(userId);
        List list = new ArrayList();
        for (Account account : cardList) {
            if (account.getPrimary()) {
                Map mainCard = new HashMap<>();
                mainCard.put("card_prio", 0);
                mainCard.put("card_num", account.getAccountNumber());
                mainCard.put("card_status", account.getVerified() ? 0 : 1);
                mainCard.put("money", account.getMoney());
                list.add(mainCard);
                break;
            }
        }
        if (list.isEmpty()) {
            return ResponseEntity.status(201).body("No main card");
        }
        List<Map> tableList = new ArrayList();
        for (Account account : cardList) {
            Map tempMap = new HashMap<>();
            tempMap.put("card_num", account.getAccountNumber());
            tempMap.put("status", account.getVerified() ? "状态正常" : "点此验证");
            tempMap.put("bank", account.getBankId());
            tempMap.put("money", account.getMoney());
            tableList.add(tempMap);
        }
        list.add(tableList);
        list.add(new HashMap<>());
        return ResponseEntity.ok(list);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getMonthlyStats")
    public ResponseEntity<?> queryMonthlyStats(@RequestBody String userName) {
        userName = userName.substring(1, userName.length() - 1);
        Integer userId = queryService.getUserId(userName);
        if (userId == null) return ResponseEntity.status(202).body("No such user");
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
    public ResponseEntity<?> queryTransactionData(@RequestBody String userName) {
        Integer userId = queryService.getUserId(userName);
        if (userId == null) return ResponseEntity.status(202).body("No such user");
        List<Map<String, String>> result = queryService.getTransactionData(userId);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin(origins =  "*")
    @PostMapping("/getRequest")
    public ResponseEntity<?> queryRequest(@RequestBody String userName) {
        userName = userName.substring(1, userName.length() - 1);
        Integer userId = queryService.getUserId(userName);
        if (userId == null) return ResponseEntity.status(202).body("No such user");
        List<Map<String, String>> result = queryService.getPendingRequestsAndPayments(userId);
        return ResponseEntity.ok(result);
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/getDetail")
    public ResponseEntity<?> queryDetail(@RequestBody Map<?, ?> info) {
        String transNum = (String) info.get("transNum");
        Map<String, String> result = queryService.getTransDetail(transNum);
        if (result == null) return ResponseEntity.status(201).body("Error");
        else return ResponseEntity.ok(result);
    }


    @CrossOrigin(origins = "*")
    @PostMapping("/search_trans")
    public ResponseEntity<?> searchTrans(@RequestBody Map<?, ?> info) {
        String userName = (String) info.get("username");
        Map map = (Map) info.get("searchForm");
        String keyword = (String) map.get("keyword");
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");

        if (startDate.isEmpty() && (!endDate.isEmpty())) return ResponseEntity.status(202).body("no start date");
        if (endDate.isEmpty() && (!startDate.isEmpty())) return ResponseEntity.status(202).body("no end date");

        Map<String, String> result = queryService.searchTrans(userName, keyword, startDate, endDate);
        if (result == null) return ResponseEntity.status(201).body("Error");
        else return ResponseEntity.ok(result);
    }




}
