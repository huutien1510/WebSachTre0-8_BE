package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.ApiResponse;
import ute.dto.response.RatingResponse;
import ute.entity.Genre;
import ute.services.RatingServices;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingControllers {
    RatingServices ratingServices;
    @GetMapping("book/{bookID}")
    public ApiResponse<List<RatingResponse>>getRatingByBook(@PathVariable int bookID) {
        ApiResponse<List<RatingResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setData(ratingServices.getRatingByBook(bookID));
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
}
