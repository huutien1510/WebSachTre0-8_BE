package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ute.dto.response.BookDetailResponse;
import ute.entity.Book;
import ute.services.BookServices;
import ute.services.OpenAIService;

import java.awt.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/chatai")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OpenAIControllers {
    BookServices bookServices;
    OpenAIService openAIService;

    @PostMapping("/start")
    public ResponseEntity<?> startConversation() {
        // Gọi OpenAI để trả lời dựa trên sản phẩm
        String answer = openAIService.startConversation();
        System.out.println(answer);

        return ResponseEntity.ok(Map.of("answer", answer));
    }

    @PostMapping("/ask")
    public ResponseEntity<?> ask(@RequestBody(required = false) Map<String,String> body) {
        String question = body.get("question");


        // Tìm sản phẩm liên quan theo từ khóa câu hỏi
        Page<BookDetailResponse> matchedBooks = bookServices.getBookByKeyWord(question,0,10);
        List<BookDetailResponse> listMatchedBooks = matchedBooks.getContent();

        // Gọi OpenAI để trả lời dựa trên sản phẩm
        String answer = openAIService.getChatGPTResponse(question, listMatchedBooks);
        System.out.println((answer));

        return ResponseEntity.ok(Map.of("answer", answer));
    }
}
