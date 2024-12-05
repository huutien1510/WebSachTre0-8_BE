package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.OrderRequest;
import ute.dto.response.ApiResponse;
import ute.dto.response.LoginResponse;
import ute.dto.response.OrderReponse;
import ute.entity.OrderDetail;
import ute.entity.Orders;
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
    ApiResponse<List<OrderReponse>> getOrderByAccount(@PathVariable Integer accountID){
        ApiResponse<List<OrderReponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderServices.getOrderByAccount(accountID));
        return apiResponse;
    }

    @GetMapping("/getAll")
    ApiResponse<List<OrderReponse>> getAllOrder(){
        ApiResponse<List<OrderReponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(orderServices.getAllOrder());
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

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<String> handleRuntimeException(RuntimeException ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(500);
        apiResponse.setMessage(ex.toString());
        apiResponse.setData("Fault data");
        return apiResponse;
    }
}
