package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import ute.dto.request.OrderRequest;
import ute.dto.request.OrderUpdaterRequest;
import ute.dto.response.OrderDetailResponse;
import ute.dto.response.OrderReponse;
import ute.dto.response.PaymentResponse;
import ute.entity.*;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class OrderServices {
    OrderRepository orderRepository;
    OrderDetailRepository orderDetailRepository;
    AccountRepository accountRepository;
    DiscountRepository discountRepository;
    BookRepository bookRepository;
    ItemExchangeHistoryRepository itemExchangeHistoryRepository;
    PointService pointService;
    MomoPaymentService momoPaymentService;

    public Page<OrderReponse> getOrderByAccount(Integer accountID, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> orders = orderRepository.getOrderByAccount(accountID, pageable);

        return orders.map(order -> new OrderReponse(
                order.getId(),
                order.getTotalPrice(),
                order.getAddress(),
                order.getDate(),
                order.getPaymentMethod(),
                order.getStatus(),
                order.getOrderDetails()
                        .stream()
                        .map(orderDetail -> new OrderDetailResponse(
                                orderDetail.getBook().getId(),
                                orderDetail.getBook().getName(),
                                orderDetail.getBook().getType(),
                                orderDetail.getBook().getThumbnail(),
                                orderDetail.getQuantity()))
                        .collect(Collectors.toList()),
                order.getAccount().getId(),
                order.getAccount().getName(),
                order.getDiscount() != null ? order.getDiscount().getId() : null,
                null));
    }

    public Page<OrderReponse> getAllOrder(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Orders> orders = orderRepository.findAll(pageable);
        return orders.map(order -> new OrderReponse(
                order.getId(),
                order.getTotalPrice(),
                order.getAddress(),
                order.getDate(),
                order.getPaymentMethod(),
                order.getStatus(),
                order.getOrderDetails()
                        .stream()
                        .map(orderDetail -> new OrderDetailResponse(
                                orderDetail.getBook().getId(),
                                orderDetail.getBook().getName(),
                                orderDetail.getBook().getType(),
                                orderDetail.getBook().getThumbnail(),
                                orderDetail.getQuantity()))
                        .collect(Collectors.toList()),
                order.getAccount().getId(),
                order.getAccount().getName(),
                order.getDiscount() != null ? order.getDiscount().getId() : null,
                null));
    }

    @PostAuthorize("returnObject.accountName == authentication.name")
    public OrderReponse createOrder(OrderRequest orderRequest) {
        Account account = accountRepository.findById(orderRequest.getAccount())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        ItemExchangeHistory voucherHistory = null;
        if (orderRequest.getItemExchangeHistoryId() != null) {
            voucherHistory = itemExchangeHistoryRepository.findById(orderRequest.getItemExchangeHistoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.VOUCHER_NOT_FOUND));

            // Kiểm tra voucher thuộc về user
            if (!voucherHistory.getAccount().getId().equals(account.getId())) {
                throw new AppException(ErrorCode.VOUCHER_INVALID);
            }
            // Kiểm tra đã dùng chưa
            if (Boolean.TRUE.equals(voucherHistory.getUsed())) {
                throw new AppException(ErrorCode.VOUCHER_ALREADY_USED);
            }
            // Kiểm tra hạn sử dụng (nếu có)
            if (voucherHistory.getItem().getDiscount() != null &&
                    voucherHistory.getItem().getDiscount().getEndDate() != null &&
                    voucherHistory.getItem().getDiscount().getEndDate().isBefore(LocalDate.now())) {
                throw new AppException(ErrorCode.VOUCHER_EXPIRED);
            }

            // Đánh dấu đã dùng
            voucherHistory.setUsed(true);
            itemExchangeHistoryRepository.save(voucherHistory);
        }

        // Áp dụng mã giảm giá (nếu có)
        Discount discount = null;
        if (!orderRequest.getDiscountCode().equals("")) {
            discount = discountRepository.findByCode(orderRequest.getDiscountCode())
                    .orElseThrow(() -> new AppException(ErrorCode.DISCOUNT_NOT_FOUND));

            if (discount.getQuantity() <= 0) {
                throw new AppException(ErrorCode.DISCOUNT_INVALID);
            }

            // Giảm số lượng mã giảm giá ngay khi áp dụng
            discount.setQuantity(discount.getQuantity() - 1);
            discountRepository.save(discount);
        }

        // Tạo đơn hàng mới
        Orders newOrder = Orders.builder()
                .totalPrice(orderRequest.getTotalPrice())
                .address(orderRequest.getAddress())
                .date(orderRequest.getDate())
                .paymentMethod(orderRequest.getPaymentMethod())
                .account(account)
                .discount(discount) // Gán mã giảm giá cho đơn hàng
                .status("Chờ thanh toán")
                .build();

        if (newOrder.getPaymentMethod().equalsIgnoreCase("cod")) {
            newOrder.setStatus("Chờ giao hàng");
        }

        List<OrderDetail> orderDetails = orderRequest.getOrderDetails().stream()
                .map(orderDetailResponse -> {
                    Book book = bookRepository.findById(orderDetailResponse.getBookID())
                            .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

                    return OrderDetail.builder()
                            .book(book)
                            .quantity(orderDetailResponse.getQuantity())
                            .orders(newOrder)
                            .build();
                })
                .collect(Collectors.toList());

        newOrder.setOrderDetails(orderDetails);
        account.getOrders().add(newOrder);

        orderRepository.save(newOrder);
        orderDetailRepository.saveAll(orderDetails);

        // Xử lý thanh toán MoMo
        String momoPayUrl = null;
        if (newOrder.getPaymentMethod().equalsIgnoreCase("momo")) {
            try {
                momoPayUrl = momoPaymentService.createPaymentUrl(
                        newOrder.getId().toString(),
                        newOrder.getTotalPrice(),
                        "Thanh toán đơn hàng #" + newOrder.getId());
            } catch (Exception e) {
                throw new AppException(ErrorCode.PAYMENT_ERROR);
            }
        }

        return mapToOrderResponse(newOrder, momoPayUrl);
    }

    // Thanh toán lại
    public OrderReponse retryPayment(Integer orderId) {
        // Tìm đơn hàng theo ID và kiểm tra trạng thái
        Orders existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));

        if (!existingOrder.getStatus().equalsIgnoreCase("Chờ thanh toán")) {
            throw new AppException(ErrorCode.ORDER_INVALID_STATUS);
        }

        // Xử lý thanh toán MoMo
        String momoPayUrl;
        if (existingOrder.getPaymentMethod().equalsIgnoreCase("momo")) {
            try {
                momoPayUrl = momoPaymentService.createPaymentUrl(
                        existingOrder.getId().toString(),
                        existingOrder.getTotalPrice(),
                        "Thanh toán lại đơn hàng #" + existingOrder.getId());
            } catch (Exception e) {
                throw new AppException(ErrorCode.PAYMENT_ERROR);
            }
        } else {
            throw new AppException(ErrorCode.PAYMENT_METHOD_INVALID);
        }

        // Trả về thông tin đơn hàng với link thanh toán mới
        return mapToOrderResponse(existingOrder, momoPayUrl);
    }

    private OrderReponse mapToOrderResponse(Orders order, String momoPayUrl) {
        return new OrderReponse(
                order.getId(),
                order.getTotalPrice(),
                order.getAddress(),
                order.getDate(),
                order.getPaymentMethod(),
                order.getStatus(),
                order.getOrderDetails().stream()
                        .map(orderDetail -> new OrderDetailResponse(
                                orderDetail.getBook().getId(),
                                orderDetail.getBook().getName(),
                                orderDetail.getBook().getType(),
                                orderDetail.getBook().getThumbnail(),
                                orderDetail.getQuantity()))
                        .collect(Collectors.toList()),
                order.getAccount().getId(),
                order.getAccount().getName(),
                order.getDiscount() != null ? order.getDiscount().getId() : null,
                momoPayUrl);
    }

    public Orders updateOrderState(Integer orderID, String state) {
        Orders order = orderRepository.findById(orderID)
                .orElseThrow(() -> new AppException(ErrorCode.ORDER_NOT_FOUND));
        order.setStatus(state);
        order.getOrderDetails().forEach(orderDetail -> {
            Book book = orderDetail.getBook();
            if (book.getType().equals("Sach cung")) {
                book.setQuantity(book.getQuantity() - orderDetail.getQuantity());
                bookRepository.save(book);
            }
        });
        if (state.equals("Đã thanh toán")) {
            pointService.addPointsFromOrder(order.getAccount(), order);
            pointService.addFirstPurchasePoints(order.getAccount());
        }
        return orderRepository.save(order);
    }

    public Long totalPrice() {
        return orderRepository.totalPrice();
    }

    public boolean checkBuySoftBook(Integer accountID, Integer bookID) {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Orders> orders = orderRepository.getOrderByAccount(accountID, pageable);
        for (Orders order : orders) {
            for (OrderDetail orderDetail : order.getOrderDetails()) {
                if (orderDetail.getBook().getType().equals("Sach mem") && orderDetail.getBook().getId().equals(bookID)
                        && order.getStatus().equals("Đã thanh toán")) {
                    return true;
                }
            }
        }
        return false;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Orders updateOrder(Integer orderID, OrderUpdaterRequest body) {
        Orders orders = orderRepository.findById(orderID)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orders.setTotalPrice(body.getTotalPrice());
        orders.setStatus(body.getStatus());
        orders.setAddress(body.getAddress());
        orders.setPaymentMethod(body.getPaymentMethod());
        if (body.getStatus().equals("Đã giao hàng")) {
            orders.getOrderDetails().forEach(orderDetail -> {
                Book book = orderDetail.getBook();
                book.setQuantity(book.getQuantity() - orderDetail.getQuantity());
                bookRepository.save(book);
            });
            pointService.addPointsFromOrder(orders.getAccount(), orders);
            pointService.addFirstPurchasePoints(orders.getAccount());
        }

        return orderRepository.save(orders);
    }

    public void updateCancel(Integer orderID) {
        Orders orders = orderRepository.findById(orderID)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orders.setStatus("Đã hủy");
        orderRepository.save(orders);
    }

    public void deleteOrder(Integer orderID) {
        orderRepository.deleteById(orderID);
    }

    // Lấy doanh thu theo tháng
    public List<Map<String, Object>> getMonthlyRevenue(int year) {
        List<Object[]> results = orderRepository.getMonthlyRevenue(year);
        return results.stream()
                .map(row -> {
                    Map<String, Object> monthlyData = new HashMap<>();
                    monthlyData.put("month", row[0]);
                    monthlyData.put("total", row[1]);
                    return monthlyData;
                })
                .collect(Collectors.toList());
    }

    public Double getDailyRevenue(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        Double total = orderRepository.getDailyRevenue(start, end);
        return total != null ? total : 0.0;
    }

    public List<Map<String, Object>> getYearlyRevenue() {
        List<Object[]> results = orderRepository.getYearlyRevenue();
        return results.stream()
                .map(row -> {
                    Map<String, Object> yearlyData = new HashMap<>();
                    yearlyData.put("year", row[0]);
                    yearlyData.put("total", row[1]);
                    return yearlyData;
                })
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getDailyRevenueByMonth(int year, int month) {
        List<Map<String, Object>> result = new java.util.ArrayList<>();
        int daysInMonth = java.time.YearMonth.of(year, month).lengthOfMonth();
        for (int day = 1; day <= daysInMonth; day++) {
            java.time.LocalDate date = java.time.LocalDate.of(year, month, day);
            Double total = getDailyRevenue(date);
            java.util.Map<String, Object> dailyData = new java.util.HashMap<>();
            dailyData.put("day", day);
            dailyData.put("total", total);
            result.add(dailyData);
        }
        return result;
    }
}
