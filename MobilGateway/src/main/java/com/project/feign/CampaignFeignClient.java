package com.project.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "CampaignService")
@Service
public interface CampaignFeignClient {

    @GetMapping("/campaign/getOffer")
    String getOffer(@RequestParam Long offerId);
}
