package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.response.ApiResponse;
import ute.dto.request.RatingRequest;
import ute.dto.response.RatingResponse;
import ute.entity.Rating;
import ute.services.RatingServices;

import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingControllers {
    RatingServices ratingServices;

    @GetMapping("/getAll")
    public ApiResponse<List<RatingResponse>> getAllRatings() {
        ApiResponse<List<RatingResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(ratingServices.getAllRatings());
        return apiResponse;
    }

    @GetMapping("book/{bookID}")
    public ApiResponse<List<RatingResponse>>getRatingByBook(@PathVariable int bookID) {
        ApiResponse<List<RatingResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(ratingServices.getRatingByBook(bookID));
        return apiResponse;
    }

    @PostMapping("/add")
    public ApiResponse<Rating> addRating(@RequestBody RatingRequest body){
        ApiResponse<Rating> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(ratingServices.addRating(body));
        return apiResponse;
    }

    @PatchMapping("/update/{ratingID}")
    public ApiResponse<Rating> updateRating(@PathVariable Integer ratingID,
                                            @RequestBody RatingRequest body){
        ApiResponse<Rating> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(ratingServices.updateRating(ratingID, body));
        return apiResponse;
    }

    @DeleteMapping("/delete/{ratingID}")
    public ApiResponse<String> deleteRating(@PathVariable Integer ratingID){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        ratingServices.deleteRating(ratingID);
        apiResponse.setCode(200);
        apiResponse.setData("Xóa thành công");
        return apiResponse;
    }

//    @PutMapping("/danhgia/{idchapter}/{idtaikhoan}/{sosao}")
//    public void updateDanhGia(@PathVariable Integer idchapter, @PathVariable Integer idtaikhoan, @PathVariable Double sosao){
//        Danhgia danhgia = repo.findByIdchapterAndIdtaikhoan(idchapter, idtaikhoan);
//        if (danhgia != null) {
//            danhgia.setSosao(sosao);
//            repo.save(danhgia);
//
//        }
//    }
//    @PostMapping("/adddanhgia")
//    public ResponseEntity<DanhGiaDto> adgetidbychapterandtkdBinhLuan(@RequestBody DanhGiaDto binhLuanDto) {
//
//
//        // Insert using custom query
//        Integer newId = repo.addDanhGia(
//                binhLuanDto.getIdchapter(),
//                binhLuanDto.getIdtaikhoan(),
//                binhLuanDto.getSosao(),
//                LocalDate.now()
//        );
//
//        // Prepare response DTO
//        DanhGiaDto responseDto = new DanhGiaDto();
//        responseDto.setId(newId);
//        responseDto.setIdchapter(binhLuanDto.getIdchapter());
//        responseDto.setIdtaikhoan(binhLuanDto.getIdtaikhoan());
//        responseDto.setSosao(binhLuanDto.getSosao());
//        responseDto.setNgaydanhgia(String.valueOf(LocalDate.now()));
//
//        return ResponseEntity.ok(responseDto);
//    }

    @ExceptionHandler(RuntimeException.class)
    public ApiResponse<String> handleRuntimeException(RuntimeException ex) {
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setCode(500);
        apiResponse.setMessage(ex.toString());
        apiResponse.setData("Fault data");
        return apiResponse;
    }
}
