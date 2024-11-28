package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.response.BookDetailResponse;
import ute.entity.Book;
import ute.repository.FavoriteBookRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FavoriteBookServices {
    FavoriteBookRepository favoriteBookRepository;
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
                book.getPrice()
        )).collect(Collectors.toList());
    }
}
