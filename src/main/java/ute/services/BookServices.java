package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ute.dto.response.BookDetailResponse;
import ute.entity.Book;
import ute.repository.BookRepository;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookServices {
    BookRepository bookRepository;

    public Page<BookDetailResponse> getAllBook(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books= bookRepository.findAll(pageable);
        return books.map(book -> new BookDetailResponse(
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.getDescription(),
                        book.getGenres(), // Trả về danh sách Genre
                        book.getType(),
                        book.getThumbnail(),
                        book.getPrice()
                ));
    }

    public Page<BookDetailResponse> getBookByKeyWord(String keyword,Integer page, Integer size){
        keyword = normalizeKeyword(keyword);
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.getBookByKeyWord(keyword,pageable);
        return books.map(book -> new BookDetailResponse(
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.getDescription(),
                        book.getGenres(), // Trả về danh sách Genre
                        book.getType(),
                        book.getThumbnail(),
                        book.getPrice()
                ));
    }

    public Page<BookDetailResponse> getFreeBook(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Book> books = bookRepository.getFreeBook(pageable);
        return books.map(book -> new BookDetailResponse(
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.getDescription(),
                        book.getGenres(), // Trả về danh sách Genre
                        book.getType(),
                        book.getThumbnail(),
                        book.getPrice()
                ));
    }

    public Page<BookDetailResponse> getFeeBook(Integer page, Integer size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Book> books = bookRepository.getFeeBook(pageable);
        return books.map(book -> new BookDetailResponse(
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.getDescription(),
                        book.getGenres(), // Trả về danh sách Genre
                        book.getType(),
                        book.getThumbnail(),
                        book.getPrice()
                ));
    }

    public Page<BookDetailResponse> getBookByGenre(Integer genreID, Integer page, Integer size){
        Pageable pageable = PageRequest.of(page, size);
        Page<Book> books = bookRepository.getBookByGenres(genreID,pageable);
        return books.map(book -> new BookDetailResponse(
                        book.getId(),
                        book.getName(),
                        book.getAuthor(),
                        book.getDescription(),
                        book.getGenres(), // Trả về danh sách Genre
                        book.getType(),
                        book.getThumbnail(),
                        book.getPrice()
                ));
    }

    public String normalizeKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return "";
        }

        // Normalize to NFD form and remove diacritical marks
        String normalized = java.text.Normalizer.normalize(keyword, java.text.Normalizer.Form.NFD)
                .replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        // Convert to lowercase, split by spaces, filter out empty strings, and join back with spaces
        return Arrays.stream(normalized.toLowerCase().split(" "))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.joining(" "));
    }
}
