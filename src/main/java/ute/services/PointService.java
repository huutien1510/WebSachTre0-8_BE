package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.response.PointTransactionResponse;
import ute.entity.Account;
import ute.entity.Orders;
import ute.entity.PointTransaction;
import ute.enums.PointType;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.repository.AccountRepository;
import ute.repository.PointTransactionRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class PointService {
    AccountRepository accountRepository;
    PointTransactionRepository pointTransactionRepository;

    public void addPointsFromOrder(Account account, Orders order) {
        int points = (int) (order.getTotalPrice() / 10000); 
        addPoints(account, points, PointType.ORDER_VALUE, "Tích điểm từ đơn hàng");
    }

    public void addFirstPurchasePoints(Account account) {
        if (account.getOrders().size() == 1) {
            addPoints(account, 100, PointType.FIRST_PURCHASE, "Thưởng điểm lần mua đầu");
        }
    }

    public void addRegisterPoints(Account account) {
        addPoints(account, 50, PointType.REGISTER, "Thưởng điểm đăng ký tài khoản");
    }

    public void addBirthdayPoints(Account account) {
        LocalDate today = LocalDate.now();
        if (account.getBirthday() != null) {
            LocalDate birthday = account.getBirthday().toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();
                    
            if (today.getMonth() == birthday.getMonth() &&
                today.getDayOfMonth() == birthday.getDayOfMonth()) {
                addPoints(account, 50, PointType.BIRTHDAY, "Thưởng điểm sinh nhật");
            }
        }
    }

    public void addContestPoints(Account account, Integer points) {
        addPoints(account, points, PointType.CONTEST, "Thưởng điểm từ cuộc thi");
    }

    public void addLoyaltyPoints(Account account) {
        int orderCount = account.getOrders().size();
        if (orderCount >= 10) {
            addPoints(account, 200, PointType.LOYALTY, "Thưởng điểm khách hàng thân thiết");
        }
    }

    public void redeemPoints(Account account, int points, String itemName) {
        if (account.getBonusPoint() < points) {
            throw new AppException(ErrorCode.POINT_NOT_ENOUGH);
        }
        
        account.setBonusPoint(account.getBonusPoint() - points);
        accountRepository.save(account);
        
        addPoints(account, -points, PointType.REDEEM, "Đổi điểm lấy " + itemName);
    }


    private void addPoints(Account account, int points, PointType type, String description) {
        account.setBonusPoint(account.getBonusPoint() + points);
        accountRepository.save(account);
        
        PointTransaction transaction = PointTransaction.builder()
                .account(account)
                .points(points)
                .type(type)
                .description(description)
                .createdAt(LocalDateTime.now())
                .build();
        pointTransactionRepository.save(transaction);
    }

    public List<PointTransactionResponse> getPointHistory(Integer accountId) {
        return pointTransactionRepository.findByAccountIdOrderByCreatedAtDesc(accountId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private PointTransactionResponse mapToResponse(PointTransaction transaction) {
        return PointTransactionResponse.builder()
                .id(transaction.getId())
                .accountId(transaction.getAccount().getId())
                .points(transaction.getPoints())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}
