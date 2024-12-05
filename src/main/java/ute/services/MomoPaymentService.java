package ute.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ute.config.MomoConfig;

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

        // Xây dựng rawSignature
        String rawSignature = "accessKey=" + MomoConfig.ACCESS_KEY +
                "&amount=" + amount +
                "&extraData=" +
                "&ipnUrl=" + URLEncoder.encode(MomoConfig.IPN_URL, StandardCharsets.UTF_8) +
                "&orderId=" + orderId +
                "&orderInfo=" + orderInfo +
                "&partnerCode=" + MomoConfig.PARTNER_CODE +
                "&redirectUrl=" + URLEncoder.encode(MomoConfig.REDIRECT_URL, StandardCharsets.UTF_8) +
                "&requestId=" + requestId +
                "&requestType=" + MomoConfig.REQUEST_TYPE;

        // Ký rawSignature bằng HMAC SHA256
        String signature = hmacSHA256(rawSignature, MomoConfig.SECRET_KEY);

        // Xây dựng requestBody
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("partnerCode", MomoConfig.PARTNER_CODE);
        requestBody.put("partnerName", "Test");
        requestBody.put("storeId", "MomoTestStore");
        requestBody.put("requestId", requestId);
        requestBody.put("amount", amount);
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
            return (String) responseMap.get("payUrl");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error creating payment URL: " + e.getMessage());
        }
    }

    private String hmacSHA256(String data, String key) throws Exception {
        Mac mac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        mac.init(secretKeySpec);
        byte[] hash = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder hashHex = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hashHex.append('0');
            hashHex.append(hex);
        }
        return hashHex.toString();
    }
}
