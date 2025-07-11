package org.hanihome.hanihomebe.wishlist.web;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.web.dto.response.PropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.hanihome.hanihomebe.wishlist.application.service.WishItemService;
import org.hanihome.hanihomebe.wishlist.domain.enums.WishTargetType;
import org.hanihome.hanihomebe.wishlist.web.dto.WishItemRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/wish-items")
@RequiredArgsConstructor
public class WishItemController {

    private final WishItemService wishItemService;

    @PostMapping
    public ResponseEntity<Void> addWishItem(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody WishItemRequestDTO dto) {
        wishItemService.addWishItem(userDetails.getUserId(), dto.getTargetType(), dto.getTargetId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/properties")
    public ResponseEntity<List<PropertySummaryDTO>> getWishProperty(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestParam(defaultValue = "latest") String sort ) { //sort latest, popular

        List<PropertySummaryDTO> response = wishItemService.getWishProperties(userDetails.getUserId(), sort);

        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Map<WishTargetType, List<?>>> getAllWishItems(@AuthenticationPrincipal CustomUserDetails userDetails) {

        Long memberId = userDetails.getUserId();
        Map<WishTargetType, List<?>> wishMap = wishItemService.getAllWishItems(memberId);
        return ResponseEntity.ok(wishMap);
    }

}
