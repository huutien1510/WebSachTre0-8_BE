package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.request.OrderUpdaterRequest;
import ute.dto.response.OrderDetailResponse;
import ute.dto.response.OrderReponse;
import ute.entity.Orders;
import ute.repository.OrderRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class OrderServices {
    OrderRepository orderRepository;
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
                        order.getDiscount() != null ? order.getDiscount().getId() : null
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
                        order.getDiscount() != null ? order.getDiscount().getId() : null
                ))
                .collect(Collectors.toList());
    }

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
