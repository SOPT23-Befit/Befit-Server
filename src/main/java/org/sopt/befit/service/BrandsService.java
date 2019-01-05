package org.sopt.befit.service;

import lombok.extern.slf4j.Slf4j;
import org.sopt.befit.dto.Brands;
import org.sopt.befit.dto.User;
import org.sopt.befit.mapper.BrandsMapper;
import org.sopt.befit.mapper.LikesMapper;
import org.sopt.befit.mapper.ProductsMapper;
import org.sopt.befit.mapper.UserMapper;
import org.sopt.befit.model.BrandScore;
import org.sopt.befit.model.DefaultRes;
import org.sopt.befit.model.ProductReq;
import org.sopt.befit.model.ThreeProdOnBrandReq;
import org.sopt.befit.utils.ResponseMessage;
import org.sopt.befit.utils.StatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class BrandsService {

    private final BrandsMapper brandsMapper;
    private final UserMapper userMapper;
    private final ProductsMapper productsMapper;
    private final LikesMapper likesMapper;

    public BrandsService(BrandsMapper brandsMapper, UserMapper userMapper, ProductsMapper productsMapper, LikesMapper likesMapper) {
        this.brandsMapper = brandsMapper;
        this.userMapper = userMapper;
        this.productsMapper = productsMapper;
        this.likesMapper = likesMapper;
    }

    // 모든 브랜드 조회 - 이니셜 입력 안했을 경우
    public DefaultRes getBrands(final int user_idx) {
        final List<Brands> brandsList = brandsMapper.getBrands(user_idx);
        if (brandsList.isEmpty())
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BRAND, null);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BRAND, brandsList);
    }

    // 이니셜로 브랜드 조회
    public DefaultRes getBrandsByInitial(final int user_idx, final Character initial) {
        List<Brands> brandsList = null;
        if(initial == '*') brandsList = brandsMapper.getBrandsEct(user_idx, initial);
        else brandsList = brandsMapper.getBrandsByInitial(user_idx, initial);
        if (brandsList.isEmpty())
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BRAND, null);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BRAND, brandsList);
    }

    // 특정 브랜드 정보 조회
    public DefaultRes getBrandInfo(final int user_idx, final int brand_idx) {

        final Brands brandInfo = brandsMapper.getBrandInfo(user_idx, brand_idx);

        if (brandInfo == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, "없는 브랜드 입니다.");

        return DefaultRes.res(StatusCode.OK, "brand_idx : {" + brand_idx + "} 브랜드 정보 조회 성공", brandInfo);
    }

    // 해당 유저의 브랜드 랭킹 조회
    public DefaultRes getBrandsByRank(final int user_idx) {

        final User curUser = userMapper.findByUserIdx(user_idx);

        final Brands[] myBrands = new Brands[2];
        myBrands[0] = brandsMapper.getBrandInfo(user_idx, curUser.getBrand1_idx());
        myBrands[1] = brandsMapper.getBrandInfo(user_idx, curUser.getBrand2_idx());

        final List<Brands> brandList = brandsMapper.getBrandsByGender(user_idx, curUser.getGender());
        if (brandList.isEmpty()) return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUND_BRAND);

        final ArrayList<BrandScore> brandScores = new ArrayList<>();
        for (int i = 0; i < brandList.size(); ++i) {
            BrandScore br = new BrandScore((brandList.get(i).getLike_score()) * 1.0);
            brandScores.add(i, br);
        }

        double totalScore = 0;
        for (BrandScore score : brandScores) totalScore += score.getScore();

        log.info(Double.toString(totalScore));
        for (int i = 0; i < brandList.size(); ++i) {
            brandScores.get(i).setScore(brandScores.get(i).getScore() / totalScore);

            for (Brands myBrand : myBrands) {
                if (brandList.get(i).getStyle1().equals(myBrand.getStyle1()))
                    brandScores.get(i).setScore(brandScores.get(i).getScore() + 45.0);
                else if (brandList.get(i).getStyle1().equals(myBrand.getStyle2()))
                    brandScores.get(i).setScore(brandScores.get(i).getScore() + 20.0);
                if (brandList.get(i).getStyle2().equals(myBrand.getStyle1()))
                    brandScores.get(i).setScore(brandScores.get(i).getScore() + 20.0);
                else if (brandList.get(i).getStyle2().equals(myBrand.getStyle2()))
                    brandScores.get(i).setScore(brandScores.get(i).getScore() + 15.0 );
            }
        }

//        for(Brands brand : brandList)   totalScore += brand.getScore();
//
//        for(Brands brand : brandList){
//            brand.setScore(brand.getScore()/totalScore);
//
//            for(Brands select : selectBrands){
//                if(brand.getStyle1().equals(select.getStyle1())) brand.setScore(brand.getScore()+0.7*0.7);
//                else if(brand.getStyle2().equals(select.getStyle1())) brand.setScore(brand.getScore()+0.7*0.5);
//                if(brand.getStyle1().equals(select.getStyle2())) brand.setScore(brand.getScore()+ 0.5*0.3);
//                else if(brand.getStyle2().equals(select.getStyle2())) brand.setScore(brand.getScore()+0.3*0.3);
//            }
//        }

        for (int i = 0; i < brandList.size() - 1; ++i)
            for (int j = i + 1; j < brandList.size(); ++j)
                if (brandScores.get(i).getScore() < brandScores.get(j).getScore()) {
                    Brands temp = brandList.get(j); BrandScore temp1 = brandScores.get(j);
                    brandList.set(j, brandList.get(i)); brandScores.set(j, brandScores.get(i));
                    brandList.set(i, temp); brandScores.set(i, temp1);
                }

        //Collections.sort(brandList, new CompareSeqDesc());
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_BRAND, brandList.subList(0, 10));
    }

    // 해당 유저의 선택 브랜드와 스타일이 동일한 모든 브랜드 조회
    public List<String> getBrandsBySelectBrandStyle(final int user_idx) {
        final User curUser = userMapper.findByUserIdx(user_idx);

        final String stylesByResisBrand = brandsMapper.getStylesByResisBrand(user_idx);
        String[] rawStyles = stylesByResisBrand.split(",");

        List<String> resultStyles = new ArrayList<>();

        for(String s : rawStyles) {
            if (!resultStyles.contains(s)) {
                resultStyles.add(s);
            } else {
                continue;
            }
        }

        return resultStyles;
    }

    //해당 유저의 랜덤 3개 브랜드의 선호 점수가 가장 높은 세 개의 상품들 조회
    public DefaultRes getProductsByThreeBrands(final int user_idx) {
        final User curUser = userMapper.findByUserIdx(user_idx);
        final String curUserGender = curUser.getGender();
        final List<ThreeProdOnBrandReq> randomThreeBrands = brandsMapper.getBrandsByRandomThree(curUserGender);
        final List<ThreeProdOnBrandReq> finalData = new ArrayList<>();

        for(ThreeProdOnBrandReq b : randomThreeBrands) {
            b.setBrand_like(likesMapper.isLikeBrand(user_idx, b.getIdx()));
        }

        List<ProductReq> getThreeProductsOnThreeBrands = new ArrayList<>();

        for(int i=0; i<=randomThreeBrands.size()-1; i++) {
            ThreeProdOnBrandReq b = randomThreeBrands.get(i);
            getThreeProductsOnThreeBrands.addAll(productsMapper.getThreeProductByOneBrand(b.getIdx()));
            List<ProductReq> ps = new ArrayList<>();
            for(int j=0; j<=getThreeProductsOnThreeBrands.size()-1; j++) {
                ProductReq p = getThreeProductsOnThreeBrands.get(j);
                p.setMeasure(ProductsService.parseJson(p.getMeasure().toString()));
                p.setProduct_like(likesMapper.isLikeProduct(user_idx, p.getIdx()));
                ps.add(p);
            }
            b.setProducts(ps);
            finalData.add(b);
            getThreeProductsOnThreeBrands.clear();
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_THREE_TO_RANDOM_THREE_BRANDS_PRODUCTS, finalData);
    }

//    // score 내림차순 정렬
//    static class CompareSeqDesc implements Comparator<Brands> {
//        @Override
//        public int compare(Brands o1, Brands o2) {
//            // TODO Auto-generated method stub
//            return o1.getScore() > o2.getScore() ? -1 : o1.getScore() < o2.getScore() ? 1 : 0;
//        }
//    }
}
