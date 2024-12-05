package ute.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ute.config.MomoConfig;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MomoPaymentService {

    public String createPaymentUrl(String orderID, float amount, String orderInfo) throws Exception {
        // Tạo requestId và orderId
        long currentTime = System.currentTimeMillis();
        String requestId = orderID + "_" + currentTime;
        String orderId = orderID + "_" + currentTime;
        Integer amount1 = (int) amount;

        // Xây dựng rawSignature
        String rawSignature = "accessKey=" + MomoConfig.ACCESS_KEY +
                "&amount=" + amount1 +
                "&extraData=" +
                "&ipnUrl=" + MomoConfig.IPN_URL +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + MomoConfig.PARTNER_CODE +
                "&redirectUrl=" + MomoConfig.REDIRECT_URL+
                "&requestId=" + requestId +
                "&requestType=" + MomoConfig.REQUEST_TYPE;

        // Ký rawSignature bằng HMAC SHA256
        String signature = createSignature(MomoConfig.SECRET_KEY,rawSignature);

        // Xây dựng requestBody
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", MomoConfig.PARTNER_CODE);
        requestBody.put("partnerName", "Test");
        requestBody.put("storeId", "MomoTestStore");
        requestBody.put("requestId", requestId);
        requestBody.put("amount", amount1);
        requestBody.put("orderId", orderId);
        requestBody.put("orderInfo", orderInfo);
        requestBody.put("redirectUrl", MomoConfig.REDIRECT_URL);
        requestBody.put("ipnUrl", MomoConfig.IPN_URL);
        requestBody.put("lang", "vi");
        requestBody.put("requestType", MomoConfig.REQUEST_TYPE);
        requestBody.put("autoCapture", true);
        requestBody.put("extraData", "");
        requestBody.put("orderGroupId", "");
        requestBody.put("signature", signature);

        // Gửi yêu cầu HTTP POST tới MoMo API
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            String response = restTemplate.postForObject(
                    MomoConfig.ENDPOINT,
                    requestBody,
                    String.class
            );
            // Parse JSON response để lấy payUrl
            Map<String, Object> responseMap = objectMapper.readValue(response, Map.class);
            System.out.println(responseMap);
            return (String) responseMap.get("payUrl");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating payment URL: " + e.getMessage());
        }
    }

    public String createSignature(String secretKey, String rawSignature) {
        try {
            // Tạo key từ secretKey
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");

            // Khởi tạo HMAC với thuật toán SHA-256
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKeySpec);

            // Cập nhật dữ liệu (rawSignature) và tính toán chữ ký
            byte[] hash = mac.doFinal(rawSignature.getBytes());

            // Chuyển hash sang dạng Hex hoặc Base64
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error while creating HMAC SHA-256 signature", e);
        }
    }
}
