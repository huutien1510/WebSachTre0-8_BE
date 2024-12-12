package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.OrderRequest;
import ute.dto.request.OrderUpdaterRequest;
import ute.dto.response.ApiResponse;
import ute.dto.response.LoginResponse;
import ute.dto.response.OrderReponse;
import ute.entity.OrderDetail;
import ute.entity.Orders;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.services.OrderServices;

import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderControllers {
    OrderServices orderServices;

    @GetMapping("/account/{accountID}")
    ApiResponse<Page<OrderReponse>> getOrderByAccount(@PathVariable Integer accountID, @RequestParam(defaultValue = "0") Integer page,
                                                      @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<OrderReponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(orderServices.getOrderByAccount(accountID, page, size));
        return apiResponse;
    }

    @GetMapping("/getAll")
    ApiResponse<Page<OrderReponse>> getAllOrder(@RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<OrderReponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(orderServices.getAllOrder(page, size));
        return apiResponse;
    }
    @GetMapping("/totalPrice")
    ApiResponse<Long> totalPrice(){
        ApiResponse<Long> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(orderServices.totalPrice());
        return apiResponse;
    }
    @PostMapping
    public ApiResponse<OrderReponse> createOrder(@RequestBody OrderRequest request){
        ApiResponse<OrderReponse> apiResponse = new ApiResponse<>();
        var result = orderServices.createOrder(request);
        apiResponse.setData(result);
        apiResponse.setMessage("Create order successfully");
        return apiResponse;
    }
    @GetMapping("/checkSoftBookBought/{accountID}/{bookID}")
    public ApiResponse<Boolean> checkSoftBookBought(@PathVariable Integer accountID,
                                                    @PathVariable Integer bookID) {
        ApiResponse<Boolean> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderServices.checkBuySoftBook(accountID,bookID));
        return apiResponse;
    }

    @PatchMapping("/updateOrder/{orderID}")
    ApiResponse<Orders> updateOrder(@PathVariable Integer orderID,
                                    @RequestBody OrderUpdaterRequest body)
    {
        ApiResponse<Orders> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(orderServices.updateOrder(orderID,body));
        return apiResponse;
    }
    @GetMapping("/cancelOrder/{orderID}")
    ApiResponse cancelOrder(@PathVariable Integer orderID)
    {
        ApiResponse apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        orderServices.updateCancel(orderID);
        apiResponse.setMessage("Hủy đơn hàng thành công");
        return apiResponse;
    }
      
    @GetMapping("/momo-return")
    public ApiResponse<Orders> handlePaymentReturn(@RequestParam String orderId, @RequestParam String resultCode) {
        try {
            String orderID = orderId.split("_")[0];
            String paymentStatus = "0".equals(resultCode) ? "success" : "failed";

            if ("success".equals(paymentStatus)) {
                Orders result = orderServices.updateOrderState(Integer.parseInt(orderID), "Đã thanh toán");
                return ApiResponse.<Orders>builder().code(200).message("Thanh toán thành công").data(result).build();
            } else {
                throw new AppException(ErrorCode.PAYMENT_FAILED);
            }
        } catch (Exception e) {
            throw new AppException(ErrorCode.PAYMENT_FAILED);
        }
    }

    @DeleteMapping("/deleteOrder/{orderID}")
    ApiResponse<String> deleteOrder(@PathVariable Integer orderID)
    {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        orderServices.deleteOrder(orderID);
        apiResponse.setCode(200);
        apiResponse.setData("Xóa thành công");
        return apiResponse;
    }
    @GetMapping("/retryOrder/{orderID}")
    ApiResponse<OrderReponse> getOrderDetail(@PathVariable Integer orderID)
    {
        ApiResponse<OrderReponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(orderServices.retryPayment(orderID));
        return apiResponse;
    }

}
