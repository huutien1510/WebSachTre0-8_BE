package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ute.dto.request.BookRequest;
import ute.dto.response.BookDetailResponse;
import ute.entity.Account;
import ute.entity.Book;
import ute.entity.Genre;
import ute.repository.BookRepository;
import ute.repository.GenreRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class BookServices {
    BookRepository bookRepository;
    GenreRepository genreRepository;

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
                        book.getPrice(),
                        book.getQuantity()
                ));
    }

    public BookDetailResponse getBookByID(Integer bookID){
        Optional<Book> optionalBook = Optional.of(bookRepository.findById(bookID)
                .orElseThrow(() -> new RuntimeException("Book not found")));
        Book book= optionalBook.get();
        return new BookDetailResponse(
                book.getId(),
                book.getName(),
                book.getAuthor(),
                book.getDescription(),
                book.getGenres(), // Trả về danh sách Genre
                book.getType(),
                book.getThumbnail(),
                book.getPrice(),
                book.getQuantity()
        );
    }

    public Long getTotal(){
        return bookRepository.countBook();
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
                        book.getPrice(),
                        book.getQuantity()
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
                        book.getPrice(),
                        book.getQuantity()
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
                        book.getPrice(),
                        book.getQuantity()
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
                        book.getPrice(),
                        book.getQuantity()
                ));
    }

    public Book addBook(BookRequest body){
        Book book = new Book();

        //Set giá trị
        book.setName(body.getName());
        book.setAuthor(body.getAuthor());
        book.setDescription(body.getDescription());
        book.setGenres(
                body.getGenreIDs().stream()
                        .map(genre -> genreRepository.findById(genre)
                                    .orElseThrow(() -> new RuntimeException("Genre not found")))
                        .collect(Collectors.toList()));
        book.setThumbnail(body.getThumbnail());
        book.setType(body.getType());
        book.setPrice(body.getPrice());
        book.setQuantity(body.getQuantity());
        book.setIs_delete(false);

        return bookRepository.save(book);
    }

    public Book updateBook(Integer bookID, BookRequest body){
        Optional<Book> optionalBook = Optional.of(bookRepository.findById(bookID)
                .orElseThrow(() -> new RuntimeException("Book not found")));
        Book book = optionalBook.get();

        //Update
        book.setName(body.getName());
        book.setAuthor(body.getAuthor());
        book.setDescription(body.getDescription());
        book.setGenres(
                body.getGenreIDs().stream()
                .map(genre -> {
            Optional<Genre> optionalGenre = Optional.of(genreRepository.findById(genre)
                    .orElseThrow(() -> new RuntimeException("Genre not found")));
            return optionalGenre.get();
        })
                .collect(Collectors.toList()));
        book.setThumbnail(body.getThumbnail());
        book.setType(body.getType());
        book.setPrice(body.getPrice());
        book.setQuantity(body.getQuantity());

        return bookRepository.save(book);
    }

    public Book deleteBook(Integer bookID){
        Optional<Book> optionalBook = Optional.of(bookRepository.findById(bookID)
                .orElseThrow(() -> new RuntimeException("Book not found")));
        Book book = optionalBook.get();
        //Xóa book_genre trước khi xóa book
        book.setIs_delete(true);
        return bookRepository.save(book);
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
