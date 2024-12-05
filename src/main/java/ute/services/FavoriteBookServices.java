package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.response.BookDetailResponse;
import ute.entity.Account;
import ute.entity.Book;
import ute.repository.AccountRepository;
import ute.repository.BookRepository;
import ute.repository.FavoriteBookRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FavoriteBookServices {
    FavoriteBookRepository favoriteBookRepository;
    AccountRepository accountRepository;
    BookRepository bookRepository;
    public List<BookDetailResponse> getFavoriteBookByAccount(Integer accountID){
        List<Book> favBooks = favoriteBookRepository.getFavoriteBookByAccount(accountID);
        return favBooks.stream()
                .map(book -> new BookDetailResponse(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getDescription(),
                book.getGenres(), // Trả về danh sách Genre
                book.getType(),
                book.getThumbnail(),
                book.getPrice(),
                book.getQuantity()
        )).collect(Collectors.toList());
    }

    public Boolean checkIsFavoritesByAccount(Integer accountID,Integer bookID){
        Optional<Account> optionalAccount = Optional.of(accountRepository.findById(accountID)
                .orElseThrow(() -> new RuntimeException("Account not found")));
        Account account = optionalAccount.get();
        return account.getFavBooks().stream().anyMatch(ch -> Objects.equals(ch.getId(), bookID));
    }

    public Account addFavorite(Integer accountID,Integer bookID) {
        Optional<Account> optionalAccount = Optional.of(accountRepository.findById(accountID)
                .orElseThrow(() -> new RuntimeException("Account not found")));
        Account account = optionalAccount.get();

        Optional<Book> optionalBook = Optional.of(bookRepository.findById(bookID)
                .orElseThrow(() -> new RuntimeException("Account not found")));
        Book book = optionalBook.get();

        boolean check = false;

        for (Book book1 : account.getFavBooks()) {
            if (Objects.equals(book1.getId(), bookID)) {
                check = true;
                break;
            }
        }
        if (!check) {
            account.getFavBooks().add(book);
            return accountRepository.save(account);
        }
        else throw new RuntimeException("Đã thêm vào danh sách yêu thích");
    }

    public Account removeFavorite(Integer accountID,Integer bookID) {
        Optional<Account> optionalAccount = Optional.of(accountRepository.findById(accountID)
                .orElseThrow(() -> new RuntimeException("Account not found")));
        Account account = optionalAccount.get();

        account.getFavBooks().removeIf(favBook -> Objects.equals(favBook.getId(),bookID));
        return accountRepository.save(account);
    }
}
