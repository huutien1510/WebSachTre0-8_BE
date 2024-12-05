package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.ContestRequest;
import ute.dto.response.ApiResponse;
import ute.dto.response.ContestResponse;
import ute.entity.Contest;
import ute.services.ContestServices;

import java.util.List;

@RestController
@RequestMapping("/contests")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContestControllers {
    ContestServices contestServices;
    @GetMapping("/getAll")
    ApiResponse<Page<ContestResponse>> getAllContest(@RequestParam(defaultValue = "0") Integer page,
                                                     @RequestParam(defaultValue = "10") Integer size){
        ApiResponse<Page<ContestResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(contestServices.getAllContest(page,size));
        return apiResponse;
    }

    @GetMapping("/{contestID}")
    ApiResponse<ContestResponse> getContestByID(@PathVariable Integer contestID){
        ApiResponse<ContestResponse> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(contestServices.getContestByID(contestID));
        return apiResponse;
    }

    @PostMapping("/addContest")
    ApiResponse<Contest> getAllContest(@RequestBody ContestRequest body){
        ApiResponse<Contest> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(contestServices.addContest(body));
        return apiResponse;
    }

    @PatchMapping("/updateContest/{contestID}")
    ApiResponse<Contest> updateContest(@PathVariable Integer contestID,
                                       @RequestBody ContestRequest body){
        ApiResponse<Contest> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(contestServices.updateContest(contestID,body));
        return apiResponse;
    }

    @DeleteMapping("/deleteContest/{contestID}")
    ApiResponse<String> deleteContest(@PathVariable Integer contestID){
        ApiResponse<String> apiResponse = new ApiResponse<>();
        contestServices.deleteContest(contestID);
        apiResponse.setCode(200);
        apiResponse.setData("Xóa thành công");
        return apiResponse;
    }
}
