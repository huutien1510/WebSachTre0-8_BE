package ute.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ute.entity.*;
import ute.repository.*;

import java.util.Date;
import java.util.Optional;

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
                .exchangeDate(new Date())
                .status("SUCCESS")
                .build();
        itemExchangeHistoryRepository.save(history);
        return "Đổi vật phẩm thành công";
    }
}