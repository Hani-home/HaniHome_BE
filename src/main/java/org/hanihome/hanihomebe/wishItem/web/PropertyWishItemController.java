package org.hanihome.hanihomebe.wishItem.web;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.wishItem.service.PropertyWishItemService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/property-wish")
@RequiredArgsConstructor
public class PropertyWishItemController {

    final PropertyWishItemService propertyWishItemService;

    @PostMapping("/{propertyId}")
    public ResponseEntity<Void> addPropertyWishItem(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable("propertyId") Long propertyId) {
        Long memberId = userDetails.getUserId();
        propertyWishItemService.addPropertyWishItem(memberId, propertyId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<PropertySummaryDTO>> getPropertyWishItems(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "latest") String sort) {
        Long memberId = userDetails.getUserId();
        List<PropertySummaryDTO> response = propertyWishItemService.getPropertyWishItems(memberId, sort);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<Void> deletePropertyWishItem(@AuthenticationPrincipal CustomUserDetails userDetails ,@PathVariable("propertyId") Long propertyId) {
        Long memberId = userDetails.getUserId();
        propertyWishItemService.deletePropertyWishItem(memberId, propertyId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build(); //NO contetn 변경해야함
    }

    
}
