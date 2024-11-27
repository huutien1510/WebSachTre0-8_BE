package ute.controllers;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ute.dto.request.ApiResponse;
import ute.entity.Genre;
import ute.services.GenreServices;

import java.util.List;


@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class GenreControllers {
    GenreServices genreServices;

    @GetMapping("/getAll")
    ApiResponse<List<Genre>> getAll(){
        ApiResponse<List<Genre>> apiResponse = new ApiResponse<>();
        apiResponse.setData(genreServices.getAll());
        return apiResponse;
    }

}
