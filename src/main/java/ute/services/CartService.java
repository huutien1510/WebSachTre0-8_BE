package ute.services;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ute.dto.response.BookDetailResponse;
import ute.entity.Account;
import ute.entity.Book;
import ute.exception.AppException;
import ute.exception.ErrorCode;
import ute.repository.AccountRepository;
import ute.repository.BookRepository;
import ute.repository.CartRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class CartService {
    AccountRepository accountRepository;
    BookRepository bookRepository;
    CartRepository  cartRepository;
    public List<BookDetailResponse> getCartByAccount(Integer accountID) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        Account account = accountRepository.findById(accountID)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!account.getUsername().equals(currentUsername)) {
            throw new AppException(ErrorCode.ACCESS_DENIED);
        }

        return account.getCarts().getBooks().stream()
                .map(book -> new BookDetailResponse(
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.getDescription(),
                        book.getGenres(),
                        book.getType(),
                        book.getThumbnail(),
                        book.getPrice(),
                        book.getQuantity()
                )).toList();
    }

    public Account addBookToCart(Integer accountID,Integer bookID) {
        Account account = accountRepository.findById(accountID)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        Book book = bookRepository.findById(bookID)
                .orElseThrow(() -> new AppException(ErrorCode.BOOK_NOT_FOUND));

        boolean check = false;
        for (Book b : account.getCarts().getBooks()) {
            if (b.getId().equals(bookID)) {
                check = true;
                break;
            }
        }
        if (!check) {
            account.getCarts().getBooks().add(book);
            return accountRepository.save(account);
        }
        else {
            throw new AppException(ErrorCode.BOOK_ALREADY_IN_CART);
        }
    }

    public Account removeBookFromCart(Integer accountID, Integer bookID) {

        Account account = accountRepository.findById(accountID)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        account.getCarts().getBooks().removeIf(book -> book.getId().equals(bookID));
        return accountRepository.save(account);
    }

}
