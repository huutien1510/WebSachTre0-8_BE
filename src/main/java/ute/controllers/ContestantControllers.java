package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ute.dto.request.ContestantSubmitRequest;
import ute.dto.response.ApiResponse;
import ute.dto.response.ContestResponse;
import ute.dto.response.ContestantResponse;
import ute.entity.Contestant;
import ute.services.ContestantServices;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/contestants")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ContestantControllers {
    ContestantServices contestantServices;

    @GetMapping("/getContestant/{accountID}/{contestID}")
    ApiResponse<ContestantResponse> getContestantByAccountAndContest(@PathVariable Integer accountID,
                                                @PathVariable Integer contestID){
        ApiResponse<ContestantResponse> apiResponse = new ApiResponse<>();
        apiResponse.setData(contestantServices.isRegister(accountID,contestID));
        if (apiResponse.getData()!=null) apiResponse.setCode(200);
        else apiResponse.setCode(400);
        return apiResponse;
    }

    @GetMapping("/getContestantByContest/{contestID}")
    ApiResponse<List<ContestantResponse>> getContestantByContest(@PathVariable Integer contestID){
        ApiResponse<List<ContestantResponse>> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(contestantServices.getContestantByContest(contestID));
        return apiResponse;
    }

    @PostMapping("/register/{accountID}/{contestID}")
    ApiResponse<Contestant> register(@PathVariable Integer accountID,
                                     @PathVariable Integer contestID){
        ApiResponse<Contestant> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(contestantServices.register(accountID,contestID));
        return apiResponse;
    }

    @PatchMapping("/submitFile")
    ApiResponse<Contestant> submitFile(@RequestBody ContestantSubmitRequest body){
        ApiResponse<Contestant> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(contestantServices.submitFile(body));
        return apiResponse;
    }

    @PatchMapping("/scoreContestant/{contestantID}")
    ApiResponse<Contestant> scoreContestant(@PathVariable Integer contestantID,
                                            @RequestBody Map<String,Integer> body){
        ApiResponse<Contestant> apiResponse = new ApiResponse<>();
        apiResponse.setCode(200);
        apiResponse.setData(contestantServices.scoreContestant(contestantID,body));
        return apiResponse;
    }
}
