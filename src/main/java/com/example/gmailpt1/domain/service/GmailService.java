package com.example.gmailpt1.domain.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePartHeader;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GmailService {
    // ✅ 제목 목록을 가져오는 메서드
    public List<String> getMailSubjects(String accessToken) throws IOException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Gmail service = new Gmail.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("GmailPT1").build();

        // 최대 10개 메일 ID 조회
        ListMessagesResponse response = service.users().messages().list("me")
                .setMaxResults(10L)
                .execute();

        List<String> subjectList = new ArrayList<>();

        // 각 메일 ID로 제목 조회
        for (Message msg : response.getMessages()) {
            Message fullMessage = service.users().messages().get("me", msg.getId()).execute();

            List<MessagePartHeader> headers = fullMessage.getPayload().getHeaders();
            for (MessagePartHeader header : headers) {
                if ("Subject".equalsIgnoreCase(header.getName())) {
                    subjectList.add(header.getValue());
                    break;
                }
            }
        }

        return subjectList;
    }

    public List<Message> getMessages(String accessToken) throws IOException {
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);

        Gmail service = new Gmail.Builder(
                new NetHttpTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("GmailPT1").build();

        ListMessagesResponse response = service.users().messages().list("me")
                .setMaxResults(10L) // 최대 10개의 메시지를 가져옴
                .execute();

        return response.getMessages();
    }
}
