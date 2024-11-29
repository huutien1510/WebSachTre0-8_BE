package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ute.dto.request.ApiResponse;
import ute.dto.response.OrderReponse;
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
}
