package com.publisher.mvc;

import com.publisher.mvc.MessageService.MService;
import com.publisher.mvc.MessageTemplate.Payment;
import com.publisher.mvc.MessageTemplate.Topup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageServiceController {

    @Autowired
    MService messageService;

    @PostMapping(path = "/payments")
    public ResponseEntity<String> messagePayments(@RequestBody Payment paymentData) {
        if (messageService.sendMessageRequest(paymentData)) {
            System.out.println("[*SENDING*] Sending Payment message to Broker!");
            return new ResponseEntity<String>("Published Payments Message!", HttpStatus.CREATED);
        } else {
            return new ResponseEntity<String>("Unable to Publish Payments Message!", HttpStatus.NOT_FOUND);
        }

    }

    @PostMapping(path = "/topup")
    public ResponseEntity<String> messageTopUp(@RequestBody Topup topupData) {
        if (messageService.sendMessageRequest(topupData)) {
            System.out.println("[*SENDING*] Sending topup message to Broker!");
            return new ResponseEntity<String>("Published topup Message!", HttpStatus.CREATED);
        }
        return new ResponseEntity<String>("Unable to Publish Topup Message!", HttpStatus.NOT_FOUND);
    }
}
