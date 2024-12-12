package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import ute.dto.request.RequestDiscount;
import ute.dto.request.UseDiscountRequest;
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
    @PreAuthorize("hasRole('ADMIN')")
    public Page<Discount> getAllDiscount(Integer page, Integer size){
       return discountRepository.findAll(PageRequest.of(page, size));
    }
    @PreAuthorize("hasRole('ADMIN')")
    public Discount saveOrUpdateDiscount(RequestDiscount requestDiscount) {
        if (discountRepository.existsByCode(requestDiscount.getCode())) {
            throw new AppException(ErrorCode.DISCOUNT_CODE_EXIST);
        }
        if (requestDiscount.getStartDate().isAfter(requestDiscount.getEndDate())) {
            throw new AppException(ErrorCode.INVALID_REQUEST);
        }
        if (requestDiscount.getCode().length() >10){
            throw new AppException(ErrorCode.CODE_TOO_LONG);
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
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDiscount(Integer id) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));
        if (!discount.getOrders().isEmpty()) {
            throw new AppException(ErrorCode.SOFT_BOOK);
        }
        discountRepository.delete(discount);
        return "Xóa thành công";
    }
    @PreAuthorize("hasRole('ADMIN')")
    public Discount updateDiscount(Integer id, RequestDiscount requestDiscount) {
        Discount discount = discountRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));
        discount.setCode(requestDiscount.getCode());
        discount.setType(requestDiscount.getType());
        discount.setValue(requestDiscount.getValue());
        discount.setStartDate(requestDiscount.getStartDate());
        discount.setEndDate(requestDiscount.getEndDate());
        discount.setQuantity(requestDiscount.getQuantity());
        return discountRepository.save(discount);
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public Discount getDiscountByCode(String code) {
        return discountRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));
    }
    public Discount checkDiscount(UseDiscountRequest code) {
        Discount discount =  discountRepository.findByCode(code.getCode())
                .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));
        if (discount.getQuantity() <= 0) {
            throw new AppException(ErrorCode.DISCOUNT_INVALID);
        }
        if (discount.getStartDate().isAfter(java.time.LocalDate.now()) || discount.getEndDate().isBefore(java.time.LocalDate.now())) {
            throw new AppException(ErrorCode.DISCOUNT_INVALID);
        }
        discountRepository.save(discount);
        return discount;
    }

    public void useDiscount(String code) {
        Discount discount = discountRepository.findByCode(code)
                .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));
        discount.setQuantity(discount.getQuantity() - 1);
        discountRepository.save(discount);
    }
}
