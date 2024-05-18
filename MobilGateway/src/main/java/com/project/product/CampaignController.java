package com.project.product;

import com.project.service.CampaignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/campaign")
@RequiredArgsConstructor
public class CampaignController {


    private final CampaignService campaignService;

    @GetMapping("/getOffer")
    public void save(@RequestParam Long offerId, @RequestHeader String accessToken) {
        campaignService.getOffer(offerId, accessToken);
    }
}
