package org.hanihome.hanihomebe.property.web.controller;

import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.property.application.service.PropertyService;
import org.hanihome.hanihomebe.property.domain.enums.DisplayStatus;
import org.hanihome.hanihomebe.property.domain.enums.TradeStatus;
import org.hanihome.hanihomebe.property.web.dto.enums.PropertyViewType;
import org.hanihome.hanihomebe.property.web.dto.request.PropertyCompleteTradeDTO;
import org.hanihome.hanihomebe.property.web.dto.request.create.PropertyCreateRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.request.patch.PropertyPatchRequestDTO;
import org.hanihome.hanihomebe.property.web.dto.response.basic.PropertyResponseDTO;
import org.hanihome.hanihomebe.property.web.dto.response.TimeWithReserved;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class PropertyController {
    private final PropertyService propertyService;


    //create
    @PostMapping("/properties")
    public PropertyResponseDTO createProperty(@RequestBody @Valid PropertyCreateRequestDTO dto) {
        return propertyService.createProperty(dto);
    }


    //read
    @GetMapping("/properties")
    public List<?> getAll(
            @RequestParam(required = false) PropertyViewType view) {

        return propertyService.getAllProperties(view);
    }

    @GetMapping("/properties/{propertyId}")
    public PropertyResponseDTO getPropertyById(@PathVariable Long propertyId) {
        return propertyService.getPropertyById(propertyId);
    }

    // 내 매물 조회
    @GetMapping("/properties/my-properties")
    public List<?> getMyProperties(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @RequestParam(required = false) TradeStatus tradeStatus,
                                                     @RequestParam(required = false) DisplayStatus displayStatus,
                                                     @RequestParam(required = false) PropertyViewType view) {
        return propertyService.getMyProperty(userDetails.getUserId(), tradeStatus, displayStatus, view);
    }
/*
    @GetMapping("/properties/hidden")
    public List<? extends PropertyDTOByView> getMyHiddenProperties(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                                   @RequestParam(required = false) PropertyViewType view) {
        return propertyService.findByMemberIdAndDisplayStatus(userDetails.getUserId(), DisplayStatus.INACTIVE, view);
    }*/

    // 회원의 매물 조회
    @GetMapping("/members/{memberId}/properties")
    public List<?> getPropertiesByMember(@PathVariable Long memberId, @AuthenticationPrincipal CustomUserDetails userDetails,
                                                           @RequestParam(required = false) TradeStatus tradeStatus,
                                                           @RequestParam(required = false) PropertyViewType view) {
        return propertyService.getPropertiesByMemberId(memberId, userDetails, tradeStatus, view);
    }

    // 매물 별 뷰잉 가능 시각 조회(예약됨, 예약안됨 모두 포함)
    @GetMapping("/properties/{propertyId}/viewing-available-dates")
    public Map<LocalDate, List<TimeWithReserved>> getViewingAvailableDateTimes(@PathVariable Long propertyId) {
        return propertyService.getViewingAvailableDateTimes(propertyId);
    }


    //update

    /**
     * contentType: application/json 아님. !! application/json-patch+json
     */
   /*
   @PatchMapping(
            path = "/properties/{propertyId}",
            consumes = "application/json-patch+json",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PropertyResponseDTO> patchProperty(@PathVariable("propertyId") Long propertyId, @RequestBody JsonNode patchDocument) throws JsonPatchException, IOException {
        PropertyResponseDTO updated = propertyService.patch(propertyId, patchDocument);
        return ResponseEntity.ok(updated);
    }
*/
    @PatchMapping("/properties/{propertyId}")
    public PropertyResponseDTO patch(@PathVariable("propertyId") Long propertyId,
                                     @RequestBody @Valid PropertyPatchRequestDTO dto) throws JsonPatchException, IOException {
        return propertyService.patch(propertyId, dto);
    }

    // 거래 완료
    @PostMapping("/properties/{propertyId}/complete")
    public void completeTrade(@AuthenticationPrincipal CustomUserDetails userDetails,
                              @PathVariable Long propertyId,
                              @RequestParam Long viewingId) {
        PropertyCompleteTradeDTO dto = PropertyCompleteTradeDTO.create(userDetails.getUserId(), viewingId, propertyId);
        propertyService.completeTrade(dto);
    }


    //delete
    @DeleteMapping("/properties/{propertyId}")
    public void delete(@PathVariable("propertyId") Long propertyId) {
        propertyService.deletePropertyById(propertyId);
    }
}
