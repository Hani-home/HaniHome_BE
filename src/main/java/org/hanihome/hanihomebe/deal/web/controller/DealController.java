package org.hanihome.hanihomebe.deal.web.controller;

import lombok.RequiredArgsConstructor;
import org.hanihome.hanihomebe.deal.application.service.DealService;
import org.hanihome.hanihomebe.deal.domain.DealerType;
import org.hanihome.hanihomebe.property.web.dto.response.summary.PropertySummaryDTO;
import org.hanihome.hanihomebe.security.auth.user.detail.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/api/v1/deals")
@RestController
public class DealController {

    private final DealService dealService;

    @GetMapping("/my-deals")
    public List<PropertySummaryDTO> getMyDeals(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestParam DealerType dealerType) {

        return dealService.getDealsByDealerType(userDetails.getUserId(), dealerType);
    }

}
