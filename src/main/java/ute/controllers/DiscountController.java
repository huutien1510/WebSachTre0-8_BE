package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.RequestDiscount;
import ute.dto.request.UseDiscountRequest;
import ute.dto.response.ApiResponse;
import ute.dto.response.DiscountResponse;
import ute.entity.Discount;
import ute.services.DiscountService;

import java.util.List;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiscountController {
    DiscountService discountService;
    // lấy tất cả discount có phân trang
    @GetMapping("/getAll")
    ApiResponse<Page<DiscountResponse>> getAllDiscounts(@RequestParam(defaultValue = "0") Integer page,
                                                        @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<DiscountResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(discountService.getAllDiscount(page,size));
        return apiResponse;
    }
    // lấy tát cả discount không phân trang
    @GetMapping("/getAllByGift")
    ApiResponse<List<DiscountResponse>> getAllDiscountByGift(){
        ApiResponse<List<DiscountResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(discountService.getAllDiscountByGift());
        apiResponse.setCode(200);
        return apiResponse;
    }
    @PostMapping("/add")
    ApiResponse<Discount> addDiscount(@RequestBody RequestDiscount discount){
        ApiResponse<Discount> apiResponse = new ApiResponse<>();
        apiResponse.setData(discountService.saveOrUpdateDiscount(discount));
        return apiResponse;
    }
    @DeleteMapping("/delete/{id}")
    ApiResponse deleteDiscount(@PathVariable Integer id){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setMessage(discountService.deleteDiscount(id));
        return apiResponse;
    }
    @PatchMapping("/update/{id}")
    ApiResponse<Discount> updateDiscount(@PathVariable Integer id, @RequestBody RequestDiscount discount){
        ApiResponse<Discount> apiResponse = new ApiResponse<>();
        apiResponse.setData(discountService.updateDiscount(id, discount));
        return apiResponse;
    }
    @GetMapping("/getDiscount")
    ApiResponse<Discount> getDiscount(@RequestParam String code){
        ApiResponse<Discount> apiResponse = new ApiResponse<>();
        apiResponse.setData(discountService.getDiscountByCode(code));
        return apiResponse;
    }
    @PostMapping("/checkDiscount")
    ApiResponse<Discount> useDiscount(@RequestBody UseDiscountRequest code){
        ApiResponse<Discount> apiResponse = new ApiResponse<>();
        apiResponse.setData(discountService.checkDiscount(code));
        return apiResponse;
    }
    @GetMapping("/useDiscount")
    ApiResponse useDiscount(@RequestParam String code){
        ApiResponse apiResponse = new ApiResponse<>();
        discountService.useDiscount(code);
        apiResponse.setMessage("Discount đã được sử dụng");
        return apiResponse;
    }
}
