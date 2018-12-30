package org.sopt.befit.api;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.mapper.TestMapper;
import org.sopt.befit.model.TestReq;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static org.sopt.befit.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("")
public class TestController {

    private  final TestMapper testMapper;

    public TestController(final TestMapper testMapper){
        this.testMapper = testMapper;
    }

    @GetMapping("/TestJson")
    public ResponseEntity getBrandsByInitial() {
        try{
            TestReq data = testMapper.find23();

            String DBstr = data.getMeasure();
            log.warn(DBstr);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode actualObj = mapper.readTree(DBstr);
            log.warn(actualObj.toString());

            JsonNode jsonNode1 = actualObj.get("L");
            log.warn(jsonNode1.toString());

            JsonNode jsonNode2 = actualObj.get("L").get("총장");
            log.warn(jsonNode2.toString());


            return new ResponseEntity(actualObj,HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
