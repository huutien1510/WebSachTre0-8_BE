
package ute.controllers;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ute.dto.response.PointTransactionResponse;
import ute.services.PointService;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RestController
@RequestMapping("/api/v1/points")
public class PointController {
    PointService pointService;

    @GetMapping("/history/{accountId}")
    @PreAuthorize("hasRole('ADMIN') or #accountId == authentication.principal.id")
    public ResponseEntity<List<PointTransactionResponse>> getPointHistory(@PathVariable Integer accountId) {
        return ResponseEntity.ok(pointService.getPointHistory(accountId));
    }

//    @PostMapping("/redeem")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<Void> redeemPoints(
//            @RequestParam Integer accountId,
//            @RequestParam Integer points,
//            @RequestParam String itemName) {
//        pointService.redeemPoints(accountId, points, itemName);
//        return ResponseEntity.ok().build();
//    }
}
