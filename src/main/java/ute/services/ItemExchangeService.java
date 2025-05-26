package ute.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ute.dto.response.AccountItemResponse;
import ute.dto.response.ItemExchangeHistoryResponse;
import ute.entity.*;
import ute.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemExchangeService {
    private final AccountRepository accountRepository;
    private final ItemRepository itemRepository;
    private final ItemExchangeHistoryRepository itemExchangeHistoryRepository;

    @Transactional
    public String exchangeItem(Integer accountId, Integer itemId) {
        Optional<Account> accountOpt = accountRepository.findById(accountId);
        Optional<Item> itemOpt = itemRepository.findById(itemId);
        if (accountOpt.isEmpty() || itemOpt.isEmpty()) {
            return "Account hoặc Item không tồn tại";
        }
        Account account = accountOpt.get();
        Item item = itemOpt.get();
        if (!Boolean.TRUE.equals(item.getActive())) {
            return "Vật phẩm không hoạt động";
        }
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            return "Vật phẩm đã hết";
        }
        if (account.getBonusPoint() == null || account.getBonusPoint() < item.getPoint()) {
            return "Bạn không đủ điểm để đổi vật phẩm này";
        }
        // Trừ điểm và giảm số lượng
        account.setBonusPoint(account.getBonusPoint() - item.getPoint());
        item.setQuantity(item.getQuantity() - 1);
        accountRepository.save(account);
        itemRepository.save(item);
        // Lưu lịch sử
        ItemExchangeHistory history = ItemExchangeHistory.builder()
        .account(account)
        .item(item)
        .pointUsed(item.getPoint())
        .exchangeDate(LocalDateTime.now()) 
        .status("SUCCESS")
        .build();
        itemExchangeHistoryRepository.save(history);
        return "Đổi vật phẩm thành công";
    }

    public List<ItemExchangeHistoryResponse> getExchangeHistory(Integer accountId) {
        return itemExchangeHistoryRepository.findByAccountIdOrderByExchangeDateDesc(accountId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<AccountItemResponse> getAccountItemInventory(Integer accountId) {
        List<ItemExchangeHistory> histories = itemExchangeHistoryRepository
            .findByAccountIdOrderByExchangeDateDesc(accountId)
            .stream()
            .filter(h -> "SUCCESS".equals(h.getStatus())&& !Boolean.TRUE.equals(h.getUsed()))
            .collect(Collectors.toList());
    
        return histories.stream()
            .collect(Collectors.groupingBy(h -> h.getItem().getId()))
            .values().stream()
            .map(list -> {
                ItemExchangeHistory first = list.get(0);
                Item item = first.getItem();
                return AccountItemResponse.builder()
                        .id(first.getId())
                    .itemId(item.getId())
                    .itemName(item.getName())
                    .itemType(item.getType())
                    .itemImage(item.getLink())
                    .pointUsed(item.getPoint())
                    .quantity(list.size())
                    .codeVoucher(item.getDiscount() != null ? item.getDiscount().getCode() : null)
                    .voucherType(item.getDiscount() != null ? item.getDiscount().getType() : null)
                    .voucherValue(item.getDiscount() != null ? item.getDiscount().getValue() : null)
                    .voucherEndDate(item.getDiscount() != null ? item.getDiscount().getEndDate() : null)
                    .build();
            })
            .collect(Collectors.toList());
    }
    
    private ItemExchangeHistoryResponse mapToResponse(ItemExchangeHistory history) {
        return ItemExchangeHistoryResponse.builder()
                .id(history.getId())
                .itemName(history.getItem().getName())
                .itemType(history.getItem().getType())
                .pointUsed(history.getPointUsed())
                .exchangeDate(history.getExchangeDate())
                .status(history.getStatus())
                .itemImage(history.getItem().getLink())
                .build();
    }

}