package org.sopt.befit.api;

import com.auth0.jwt.JWT;
import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.service.JwtService;
import org.sopt.befit.service.ProductsService;
import org.sopt.befit.utils.Auth.Auth;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.sopt.befit.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("products")
public class ProductsController {

    final JwtService jwtService;

    final ProductsService productsService;

    ProductsController(final JwtService jwtService, final ProductsService productsService){
        this.jwtService = jwtService;
        this.productsService = productsService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity findAllProducts(@RequestHeader("Authorization") final String header){
        try {
            if(header != null){
                int curIdx = jwtService.decode(header).getIdx();
                return new ResponseEntity(productsService.findAllProducts(curIdx), HttpStatus.OK);
            }
            return new ResponseEntity<>(new DefaultRes(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER), HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
