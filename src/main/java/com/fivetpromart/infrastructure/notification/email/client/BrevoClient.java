package com.fivetpromart.infrastructure.notification.email.client;

import com.fivetpromart.infrastructure.notification.email.dto.EmailDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "brevo-api", url = "${notification.email.brevo-url}/v3/smtp/email")
public interface BrevoClient {

    @PostMapping
    void sendEmail(
            @RequestHeader("api-key") String apiKey,
            @RequestBody EmailDto emailRequest
    );
}