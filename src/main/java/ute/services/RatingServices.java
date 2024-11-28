package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.dto.response.RatingResponse;
import ute.entity.Genre;
import ute.repository.RatingRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class RatingServices {
    RatingRepository ratingRepository;
    public List<RatingResponse> getRatingByBook(Integer bookID){
        return ratingRepository.getRatingByBook(bookID);
    }
}
