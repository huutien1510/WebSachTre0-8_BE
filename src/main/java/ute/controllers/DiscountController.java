package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.RequestDiscount;
import ute.dto.response.ApiResponse;
import ute.entity.Discount;
import ute.services.DiscountService;

@RestController
@RequestMapping("/discounts")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DiscountController {
    DiscountService discountService;
    @GetMapping("/getAll")
    ApiResponse<Page<Discount>> getAllDiscounts(@RequestParam(defaultValue = "0") Integer page,
                                                                                @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<Discount>> apiResponse = new ApiResponse<>();
        apiResponse.setData(discountService.getAllDiscount(page,size));
        return apiResponse;
    }
    @PostMapping("/add")
    ApiResponse<Discount> addDiscount(@RequestBody RequestDiscount discount){
        ApiResponse<Discount> apiResponse = new ApiResponse<>();
        apiResponse.setData(discountService.saveOrUpdateDiscount(discount));
        return apiResponse;
    }
}
