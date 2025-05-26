package ute.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import ute.dto.request.ItemRequest;
import ute.dto.response.ApiResponse;
import ute.dto.response.ItemResponse;
import ute.entity.Discount;
import ute.entity.Item;
import ute.repository.DiscountRepository;
import ute.repository.ItemRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ItemService {
    private final ItemRepository itemRepository;
    private final DiscountRepository discountRepository;

//    public ApiResponse<List<ItemResponse>> getAllItems() {
//        List<Item> items = itemRepository.findAll();
//        List<ItemResponse> responseList = items.stream().map(this::toResponse).collect(Collectors.toList());
//        return ApiResponse.<List<ItemResponse>>builder().code(200).data(responseList).build();
//    }
    // Get all của admin có phân trang
    public ApiResponse<Page<ItemResponse>> getAllItems(Integer page, Integer size) {
        Page<Item> items = itemRepository.findAll(PageRequest.of(page, size));
        ApiResponse<Page<ItemResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(items.map(this::toResponse));
        apiResponse.setCode(200);
        return apiResponse;
    }

    // Get all cua user
    public ApiResponse<Page<ItemResponse>> getAllItemsForUser(Integer page, Integer size) {
        Page<Item> items = itemRepository.findAllByActiveTrue(PageRequest.of(page, size));
        ApiResponse<Page<ItemResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(items.map(this::toResponse));
        apiResponse.setCode(200);
        return apiResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ItemResponse> addItem(ItemRequest request) {
        Item item = new Item();
        item.setType(request.getType());
        item.setName(request.getName());
        item.setPoint(request.getPoint());
        item.setLink(request.getLink());
        item.setQuantity(request.getQuantity());
        item.setActive(request.getActive());
        if ("voucher".equalsIgnoreCase(request.getType()) && request.getDiscountId() != null) {
            Discount discount = discountRepository.findById(request.getDiscountId())
                .orElseThrow(() -> new RuntimeException("Discount không tồn tại"));
            item.setDiscount(discount);
        }
        itemRepository.save(item);
        return ApiResponse.<ItemResponse>builder().code(200).data(toResponse(item)).build();
    }


    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ItemResponse> updateItem(Integer id, ItemRequest request) {
        Item item = itemRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Item không tồn tại"));
        item.setType(request.getType());
        item.setName(request.getName());
        item.setPoint(request.getPoint());
        item.setLink(request.getLink());
        item.setQuantity(request.getQuantity());
        item.setActive(request.getActive());
        if ("voucher".equalsIgnoreCase(request.getType()) && request.getDiscountId() != null) {
            Discount discount = discountRepository.findById(request.getDiscountId())
                .orElseThrow(() -> new RuntimeException("Discount không tồn tại"));
            item.setDiscount(discount);
        } else {
            item.setDiscount(null);
        }
        itemRepository.save(item);
        return ApiResponse.<ItemResponse>builder().code(200).data(toResponse(item)).build();
    }

    // Xóa item
    public ApiResponse<String> deleteItem(Integer id) {
        itemRepository.deleteById(id);
        return ApiResponse.<String>builder().code(200).message("Xóa thành công").build();
    }

    // Convert entity sang response
    private ItemResponse toResponse(Item item) {
        ItemResponse resp = new ItemResponse();
        resp.setId(item.getId());
        resp.setType(item.getType());
        resp.setName(item.getName());
        resp.setPoint(item.getPoint());
        resp.setLink(item.getLink());
        resp.setQuantity(item.getQuantity());
        resp.setActive(item.getActive());
        if ("voucher".equalsIgnoreCase(item.getType()) && item.getDiscount() != null) {
            resp.setDiscountId(item.getDiscount().getId());
            resp.setVoucherCode(item.getDiscount().getCode());
            resp.setVoucherType(item.getDiscount().getType());
            resp.setVoucherValue(item.getDiscount().getValue());
            resp.setVoucherEndDate(item.getDiscount().getEndDate());
        }
        return resp;
    }
}