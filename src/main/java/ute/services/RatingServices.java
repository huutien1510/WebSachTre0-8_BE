package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.request.RatingRequest;
import ute.dto.response.RatingResponse;
import ute.entity.Account;
import ute.entity.Book;
import ute.entity.Genre;
import ute.entity.Rating;
import ute.repository.AccountRepository;
import ute.repository.BookRepository;
import ute.repository.RatingRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RatingServices {
    RatingRepository ratingRepository;
    AccountRepository accountRepository;
    BookRepository bookRepository;

    public List<RatingResponse> getAllRatings(){
        return ratingRepository.getAllRatings();
    }

    public List<RatingResponse> getRatingByBook(Integer bookID){
        return ratingRepository.getRatingByBook(bookID);
    }

    public Rating addRating(RatingRequest body){
        Optional<Account> optionalAccount = Optional.of(accountRepository.findById(body.getAccountID())
                .orElseThrow(() -> new RuntimeException("Account not found")));
        Account account = optionalAccount.get();

        Optional<Book> optionalBook = Optional.of(bookRepository.findById(body.getBookID())
                .orElseThrow(() -> new RuntimeException("Account not found")));
        Book book = optionalBook.get();

        Rating rating = new Rating(null, body.getStar(), body.getContent(), body.getPostDate(), book, account);

        return ratingRepository.save(rating);

    }

    public Rating updateRating(Integer ratingID, RatingRequest body){
        Optional<Rating> optionalRating = Optional.of(ratingRepository.findById(ratingID)
                .orElseThrow(() -> new RuntimeException("Account not found")));
        Rating rating = optionalRating.get();

        rating.setStar(body.getStar());
        rating.setContent(body.getContent());
        rating.setPostDate(body.getPostDate());

        return ratingRepository.save(rating);

    }

    public void deleteRating(Integer ratingID){
        ratingRepository.deleteById(ratingID);
    }
}
