package org.sopt.befit.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.service.BrandsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("closet")
public class ClosetController {
    private final BrandsService brandsService;

    public ClosetController(BrandsService brandsService) {
        this.brandsService = brandsService;
    }


}
