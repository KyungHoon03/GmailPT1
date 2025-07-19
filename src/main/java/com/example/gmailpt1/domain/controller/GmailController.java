package com.example.gmailpt1.domain.controller;


import com.example.gmailpt1.domain.service.GmailService;
import com.google.api.services.gmail.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GmailController {

    private final GmailService gmailService;

    @GetMapping("/")
    public String index() {
        return "✅ 구글 로그인 후 /gmail 접속 시 메일 목록 확인 가능합니다.";
    }

    @GetMapping("/gmail")
    public List<Message> getGmailMessages(@RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient)
            throws IOException {
        String accessToken = authorizedClient.getAccessToken().getTokenValue();
        System.out.println("🔐 Access Token: " + accessToken);

        return gmailService.getMessages(accessToken);
    }

    // ✅ 제목 조회 API
    @GetMapping("/subjects")
    public List<String> getSubjects(@RequestParam("token") String accessToken) throws IOException {
        return gmailService.getMailSubjects(accessToken);
    }

}
