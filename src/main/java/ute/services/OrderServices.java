package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
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

import java.util.List;
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
    MomoPaymentService momoPaymentService;
    public List<OrderReponse> getOrderByAccount(Integer accountID){
        List<Orders> orders = orderRepository.getOrderByAccount(accountID);
        return orders.stream()
                .map(order -> new OrderReponse(
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
                                        orderDetail.getQuantity()
                                ))
                                        .collect(Collectors.toList()),
                        order.getAccount().getId(),
                        order.getAccount().getName(),
                        order.getDiscount() != null ? order.getDiscount().getId() : null,
                        null

                ))
                .collect(Collectors.toList());
    }
    public List<OrderReponse> getAllOrder(){
        List<Orders> orders = orderRepository.getAllOrder();
        return orders.stream()
                .map(order -> new OrderReponse(
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
                                        orderDetail.getQuantity()
                                ))
                                .collect(Collectors.toList()),
                        order.getAccount().getId(),
                        order.getAccount().getName(),
                        order.getDiscount() != null ? order.getDiscount().getId() : null,
                        null
                ))
                .collect(Collectors.toList());
    }

    @PostAuthorize("returnObject.accountName == authentication.name")
    public OrderReponse createOrder(OrderRequest orderRequest) {
        Account account = accountRepository.findById(orderRequest.getAccount())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // Kiểm tra nếu có đơn hàng "Chờ thanh toán" cho "Sách mềm"
        for (OrderDetailResponse orderDetailResponse : orderRequest.getOrderDetails()) {
            Book book = bookRepository.findById(orderDetailResponse.getBookID())
                    .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

            if (book.getType().equals("Sach mem")) {
                List<Orders> pendingOrders = orderRepository.getOrderByAccount(account.getId())
                        .stream()
                        .filter(order -> order.getStatus().equals("Chờ thanh toán"))
                        .filter(order -> order.getOrderDetails()
                                .stream()
                                .anyMatch(orderDetail -> orderDetail.getBook().getId().equals(book.getId())))
                        .collect(Collectors.toList());

                if (!pendingOrders.isEmpty()) {
                    Orders existingOrder = pendingOrders.get(0);
                    String paymentUrl = null;
                    try {
                        paymentUrl = momoPaymentService.createPaymentUrl(
                                existingOrder.getId().toString(),
                                existingOrder.getTotalPrice(),
                                "Thanh toán đơn hàng #" + existingOrder.getId()
                        );
                    } catch (Exception e) {
                        throw new AppException(ErrorCode.PAYMENT_ERROR);
                    }
                    return mapToOrderResponse(existingOrder, paymentUrl);
                }
            }
        }

        // Tạo đơn hàng mới
        Orders newOrder = Orders.builder()
                .totalPrice(orderRequest.getTotalPrice())
                .address(orderRequest.getAddress())
                .date(orderRequest.getDate())
                .paymentMethod(orderRequest.getPaymentMethod())
                .account(account)
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
                        "Thanh toán đơn hàng #" + newOrder.getId()
                );
            } catch (Exception e) {
                throw new AppException(ErrorCode.PAYMENT_ERROR);
            }
        }

        return mapToOrderResponse(newOrder, momoPayUrl);
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
                                orderDetail.getBook().getThumbnail(),
                                orderDetail.getQuantity()
                        ))
                        .collect(Collectors.toList()),
                order.getAccount().getId(),
                order.getAccount().getName(),
                order.getDiscount() != null ? order.getDiscount().getId() : null,
                momoPayUrl
        );
    }

    public boolean checkBuySoftBook(Integer accountID, Integer bookID){
        List<Orders> orders = orderRepository.getOrderByAccount(accountID);
        for (Orders order : orders){
            for (OrderDetail orderDetail : order.getOrderDetails()){
                if (orderDetail.getBook().getType().equals("Sach mem") && orderDetail.getBook().getId().equals(bookID) && order.getStatus().equals("Đã thanh toán")){
                    return true;
                }
            }
        }
        return false;

    public Orders updateOrder(Integer orderID, OrderUpdaterRequest body){
        Orders orders = orderRepository.findById(orderID)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        orders.setTotalPrice(body.getTotalPrice());
        orders.setStatus(body.getStatus());
        orders.setAddress(body.getAddress());
        orders.setPaymentMethod(body.getPaymentMethod());

        return orderRepository.save(orders);
    }

    public void deleteOrder(Integer orderID){
        orderRepository.deleteById(orderID);
    }
}
