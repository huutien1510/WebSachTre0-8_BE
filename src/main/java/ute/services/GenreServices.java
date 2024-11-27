package ute.services;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ute.entity.Genre;
import ute.repository.GenreRepository;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Service
public class GenreServices {
    GenreRepository genreRepository;

    public List<Genre> getAll(){
        return genreRepository.findAll();
    }
}
