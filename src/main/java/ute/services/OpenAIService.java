package ute.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import ute.dto.request.ExtractedBookInfo;
import ute.dto.response.BookDetailResponse;
import ute.entity.Book;
import ute.repository.BookRepository;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class OpenAIService {
    private final String OPENAI_API_KEY = "Thay APIkey  vo day "; // Thay bằng key của bạn
    private final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    BookRepository bookRepository;


    private static final Pattern EXTRACTED_ANSWER_PATTERN =
            Pattern.compile("\\[ANSWER_DATA\\]\\s*\\n?(.+?)\\n?\\[/ANSWER_DATA\\]",
                    Pattern.DOTALL);

    private static final Pattern EXTRACTED_DATA_PATTERN =
            Pattern.compile("\\[EXTRACTED_DATA\\]\\s*\\n?(.+?)\\n?\\[/EXTRACTED_DATA\\]",
                    Pattern.DOTALL);

    private final RestTemplate restTemplate = new RestTemplate();

    private final Map<String, List<Map<String, String>>> chatSessions = new HashMap<>();


    /**
     * Parse GPT response để lấy thông tin trích xuất
     * @param gptResponse - Full response từ GPT
     * @return ExtractedBookInfo object hoặc null nếu không tìm thấy
     */
    public static ExtractedBookInfo parseExtractedData(String gptResponse) {
        try {
            // Tìm JSON data trong response
            Matcher matcher = EXTRACTED_DATA_PATTERN.matcher(gptResponse);

            if (matcher.find()) {
                String jsonString = matcher.group(1).trim();
                // Parse JSON thành object
                System.out.println(objectMapper.readValue(jsonString, ExtractedBookInfo.class));
                return objectMapper.readValue(jsonString, ExtractedBookInfo.class);
            } else return new ExtractedBookInfo();
        } catch (JsonMappingException e) {
            System.out.println("JSON Mapping Error: " + e.getMessage());
            return new ExtractedBookInfo();
        } catch (JsonProcessingException e) {
            System.out.println("JSON Processing Error: " + e.getMessage());
            return new ExtractedBookInfo();
        }
    }

    /**
     * Lấy phần answer/conversation (bỏ đi phần extracted data)
     * @param gptResponse - Full response từ GPT
     * @return Chỉ phần answer để hiển thị cho user
     */
    public static String getAnswer(String gptResponse) {
        Matcher matcher = EXTRACTED_ANSWER_PATTERN.matcher(gptResponse);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Xin lỗi chúng tôi hiện đang có vấn đề kỹ thuật ";
    }

    /**
     * Alias cho getAnswer() - cùng chức năng
     */
    public static String getConversationOnly(String gptResponse) {
        return getAnswer(gptResponse);
    }


    public List<Book> getBookByExtractInfo(ExtractedBookInfo bookInfo) {

        // Tìm sản phẩm liên quan theo từ khóa câu hỏi
        return bookRepository.getBookByExtractInfo(bookInfo.getPriceMin(),bookInfo.getPriceMax(),bookInfo.getCategory(),bookInfo.getBookTitle(),bookInfo.getAuthor(),bookInfo.getDescription());
    }



    public String startConversation() {

        String  prompt = "Prompt Nhân Viên Tư Vấn Cửa Hàng Sách\n" +
                "\n" +
                "[RESET] Bạn hãy quên hoàn toàn mọi cuộc trò chuyện và thông tin trước đó. Bắt đầu một phiên làm việc hoàn toàn mới.\n" +
                "\n" +
                "Vai trò của bạn:\n" +
                "\n" +
                "Bạn là nhân viên tư vấn chuyên nghiệp tại cửa hàng sách MANGACOMIC. Bạn có kiến thức sâu rộng về sách và luôn nhiệt tình hỗ trợ khách hàng.\n" +
                "\n" +
                "Nhiệm vụ chính:\n" +
                "\n" +
                "Chào hỏi thân thiện và tự nhiên như nhân viên thật\n" +
                "\n" +
                "Tư vấn sách phù hợp theo nhu cầu khách hàng với dữ liệu sách của chính cửa hàng, không lấy thông tin lan man từ bất cứ nguồn nào nếu cửa hàng không có sách đó\n" +
                "\n" +
                "Cung cấp thông tin chi tiết về sách (tác giả, nội dung, giá cả)\n" +
                "\n" +
                "Giới thiệu sách bán chạy, sách mới, khuyến mãi\n" +
                "\n" +
                "Hỗ trợ tìm kiếm sách theo thể loại, tác giả\n" +
                "\n" +
                "Tư vấn quà tặng sách phù hợp\n" +
                "\n" +
                "Cách giao tiếp:\n" +
                "\n" +
                "Giọng điệu: Thân thiện, chuyên nghiệp, nhiệt tình\n" +
                "\n" +
                "Ngôn ngữ: Tiếng Việt tự nhiên, dễ hiểu\n" +
                "\n" +
                "Phong cách: Như nhân viên bán hàng thực tế, không cứng nhắc\n" +
                "\n" +
                "Độ dài: Câu trả lời vừa phải, không quá dài dòng\n" +
                "\n" +
                "Thông tin cửa hàng:\n" +
                "\n" +
                "Tên: MANGACOMIC\n" +
                "\n" +
                "Chuyên: Sách cứng và sách mềm, đủ mọi thể loại, phục vụ đủ các nhu cầu người dùng\n" +
                "\n" +
                "Dịch vụ: Điểm danh tích điểm, tích điểm đổi voucher, tham gia cuộc thi sáng tác sách, đọc bài báo\n" +
                "\n" +
                "Khuyến mãi hiện tại: các Voucher giảm giá\n" +
                "\n" +
                "Hướng dẫn tương tác:\n" +
                "\n" +
                "Lời chào đầu tiên: \"Xin chào! Chào mừng bạn đến với MANGACOMIC. Mình có thể giúp bạn tìm cuốn sách nào hôm nay không?\"\n" +
                "\n" +
                "Khi khách hỏi về sách:\n" +
                "\n" +
                "Hỏi thêm về sở thích, mục đích đọc\n" +
                "\n" +
                "Đưa ra 2-3 gợi ý cụ thể\n" +
                "\n" +
                "Mô tả ngắn gọn về nội dung và lý do nên đọc\n" +
                "\n" +
                "Khi khách hỏi giá/khuyến mãi:\n" +
                "\n" +
                "Thông báo giá cả (nếu có thông tin)\n" +
                "\n" +
                "Giới thiệu các chương trình ưu đãi\n" +
                "\n" +
                "Đề xuất combo hoặc sách liên quan\n" +
                "\n" +
                "Khi không biết thông tin cụ thể:\n" +
                "\n" +
                "Thừa nhận một cách tự nhiên\n" +
                "\n" +
                "Đề xuất kiểm tra hoặc hỏi thêm thông tin\n" +
                "\n" +
                "Chuyển hướng sang sách tương tự\n" +
                "\n" +
                "Lưu ý quan trọng:\n" +
                "\n" +
                "Luôn bắt đầu mỗi cuộc trò chuyện như lần đầu gặp khách\n" +
                "\n" +
                "Không nhắc đến cuộc trò chuyện trước đó\n" +
                "\n" +
                "Tập trung vào việc bán sách và tư vấn\n" +
                "\n" +
                "Thể hiện sự am hiểu về sách và thị trường\n" +
                "\n" +
                "Luôn kết thúc bằng câu hỏi mở để tiếp tục tương tác\n" +
                "\n" +
                "Bắt đầu cuộc trò chuyện ngay bây giờ với lời chào thân thiện đầu tiên!";

        // Tạo body gửi tới OpenAI API
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.7
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(OPENAI_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        Map<?, ?> GPTAnswer = restTemplate.postForObject(OPENAI_API_URL, entity, Map.class);

        String prompt2 ="Vai trò và nhiệm vụ của bạn là thay thế nhân viên tư vấn sách, chăm sóc khách hàng cho một cửa hàng sách trực tuyến. Bạn cần thực hiện các nhiệm vụ sau:\n" +
                "\n" +
                "Duy trì cuộc trò chuyện tự nhiên, linh hoạt, như đang trò chuyện với một người bạn.\n" +
                "\n" +
                "Trích xuất thông tin từ các cuộc trò chuyện để gợi ý sách phù hợp mà khách hàng có thể quan tâm, nhưng không làm mất đi tính tự nhiên của cuộc trò chuyện.\n" +
                "\n" +
                "Cập nhật và lưu trữ thông tin trích xuất từ các câu hỏi trước đó và bổ sung thông tin mới một cách liên tục mà không xóa mất thông tin cũ.\n" +
                "\n" +
                "Giữ giọng điệu trò chuyện thoải mái và dễ gần, không thay đổi phong cách đã thiết lập từ trước.\n" +
                "\n" +
                "Định dạng trả lời và trích xuất thông tin:\n" +
                "\n" +
                "Phần 1: Trả lời khách hàng (hiển thị)\n" +
                "[ANSWER_DATA]  \n" +
                "**Câu trả lời tự nhiên từ nhân viên tư vấn**  \n" +
                "[/ANSWER_DATA]\n" +
                "Phần 2: Trích xuất thông tin (ẩn) - Format cho Java\n" +
                "[EXTRACTED_DATA]\n" +
                "{\n" +
                "  \"priceMin\": 0,\n" +
                "  \"priceMax\": 0,\n" +
                "  \"category\": \"Kinh doanh\",  \n" +
                "  \"bookTitle\": \"\",\n" +
                "  \"description\": \"sách về kinh doanh\",\n" +
                "  \"author\": \"\",\n" +
                "  \"hasNewInfo\": false\n" +
                "}\n" +
                "[/EXTRACTED_DATA]\n" +
                "Lưu ý quan trọng:\n" +
                "Giữ thông tin từ các câu trả lời trước và bổ sung thông tin mới vào danh sách hiện có.\n" +
                "\n" +
                "Thể loại sách chuẩn hóa: Đảm bảo thể loại được viết đúng chuẩn (ví dụ: \"kinh-doanh\", \"tiieu-thuyet\", \"self-help\", \"hoc-tap\",...).\n" +
                "\n" +
                "Chỉ trích xuất thông tin cần thiết mà không tạo cảm giác tra khảo.\n" +
                "\n";

        // Tạo body gửi tới OpenAI API
        Map<String, Object> body2 = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(Map.of("role", "user", "content", prompt2)),
                "temperature", 0.7
        );

        HttpHeaders headers2 = new HttpHeaders();
        headers2.setBearerAuth(OPENAI_API_KEY);
        headers2.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity2 = new HttpEntity<>(body2, headers2);

        Map<?, ?> GPTAnswer2 = restTemplate.postForObject(OPENAI_API_URL, entity2, Map.class);

        // Trích xuất câu trả lời
        List<?> choices = (List<?>) GPTAnswer.get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<?, ?> choice = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) choice.get("message");
            return message.get("content").toString().trim();
        }

        return  "Xin lỗi, tôi không thể trả lời câu hỏi này.";

    }


    public String getChatGPTResponse(String userMessage) {

        String prompt = "Khách hàng nhắn: "+ userMessage;

        // Tạo body gửi tới OpenAI API
        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(Map.of("role", "user", "content", prompt)),
                "temperature", 0.7
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(OPENAI_API_KEY);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        Map<?, ?> GPTAnswer = restTemplate.postForObject(OPENAI_API_URL, entity, Map.class);
        List<Book> listBookMatches = new ArrayList<>();

        // Trích xuất câu trả lời
        List<?> choices = (List<?>) GPTAnswer.get("choices");
        if (choices != null && !choices.isEmpty()) {
            Map<?, ?> choice = (Map<?, ?>) choices.get(0);
            Map<?, ?> message = (Map<?, ?>) choice.get("message");
            listBookMatches = getBookByExtractInfo(parseExtractedData(message.get("content").toString().trim()));
            System.out.println(parseExtractedData(message.get("content").toString().trim()));
            System.out.println(listBookMatches);

            if (!listBookMatches.isEmpty()) {
                prompt = "Hãy kết hợp câu trả lời trên với danh sách Book gợi ý sau: " + listBookMatches;
                // Tạo body gửi tới OpenAI API
                Map<String, Object> body2 = Map.of(
                        "model", "gpt-4o-mini",
                        "messages", List.of(Map.of("role", "user", "content", prompt)),
                        "temperature", 0.7
                );

                HttpHeaders headers2 = new HttpHeaders();
                headers2.setBearerAuth(OPENAI_API_KEY);
                headers2.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Map<String, Object>> entity2 = new HttpEntity<>(body2, headers2);

                Map<?, ?> GPTAnswer2 = restTemplate.postForObject(OPENAI_API_URL, entity2, Map.class);

                // Trích xuất câu trả lời
                List<?> choices2 = (List<?>) GPTAnswer2.get("choices");
                if (choices2 != null && !choices2.isEmpty()) {
                    Map<?, ?> choice2 = (Map<?, ?>) choices2.get(0);
                    Map<?, ?> message2 = (Map<?, ?>) choice2.get("message");
                    return getConversationOnly(message2.get("content").toString().trim());
                }
            }
            return getConversationOnly(message.get("content").toString().trim());
        }

        return "Xin lỗi, tôi không thể trả lời câu hỏi này.";

    }
}
