package org.sopt.befit.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.service.SearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.sopt.befit.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("search")
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    // 브랜드 이름으로 검색 - u 라고만하면 'u'스펠링 있는거 전부 줘야하나?
    @GetMapping("/brands")
    public ResponseEntity getBrandsList(@RequestParam("name") final Optional<String> name) {
        try {
//
            String up_name = name.get().toUpperCase();
            log.info(up_name);
            if(name.isPresent()) return new ResponseEntity<>(searchService.findBrandsByName(up_name), HttpStatus.OK);
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
