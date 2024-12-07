package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ute.dto.request.RequestDiscount;
import ute.entity.Discount;
import ute.entity.Orders;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.repository.DiscountRepository;

import java.util.List;


@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class DiscountService {
    DiscountRepository discountRepository;

    public Page<Discount> getAllDiscount(Integer page, Integer size){
       return discountRepository.findAll(PageRequest.of(page, size));
    }
    public Discount saveOrUpdateDiscount(RequestDiscount requestDiscount) {
        if (discountRepository.existsByCode(requestDiscount.getCode())) {
            throw new AppException(ErrorCode.DISCOUNT_CODE_EXIST);
        }
        if (requestDiscount.getStartDate().isAfter(requestDiscount.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }

        Discount newDiscount = Discount.builder()
                .code(requestDiscount.getCode())
                .type(requestDiscount.getType())
                .value(requestDiscount.getValue())
                .startDate(requestDiscount.getStartDate())
                .endDate(requestDiscount.getEndDate())
                .quantity(requestDiscount.getQuantity())
                .orders(List.of())
                .build();

        // Lưu Discount
        return discountRepository.save(newDiscount);
    }
}
