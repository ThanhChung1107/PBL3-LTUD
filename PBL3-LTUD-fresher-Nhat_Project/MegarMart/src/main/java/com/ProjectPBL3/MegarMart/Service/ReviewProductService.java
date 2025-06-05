package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.RatingLevel;
import com.ProjectPBL3.MegarMart.Entity.ReviewProduct;
import com.ProjectPBL3.MegarMart.Repository.ReviewProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewProductService {
    @Autowired
    private final ReviewProductRepository reviewProductRepository;


    public List<ReviewProduct> getAll(){
        return this.reviewProductRepository.findAll();
    }

    public ReviewProduct save(ReviewProduct reviewProduct) {
        return reviewProductRepository.save(reviewProduct);
    }

    public List<ReviewProduct> getReviewsByShopId(Integer shopId) {
        return reviewProductRepository.findAllByShopId(shopId);
    }
    public Page<ReviewProduct> getReviewsByShopId(Integer shopId, Pageable pageable) {
        return reviewProductRepository.findAllByShopId(shopId, pageable);
    }
    public Page<ReviewProduct> filterReviews(Integer shopId, RatingLevel rating, LocalDate startDate, LocalDate endDate, String keyword, Pageable pageable) {
        return reviewProductRepository.filterReviews(shopId, rating, startDate, endDate, keyword,pageable);
    }
    public List<ReviewProduct> findByProductId(int productId) {
        return reviewProductRepository.findByProductId(productId);
    }
    public ReviewProduct findById(Integer Id){
        return this.reviewProductRepository.findById(Id).get();
    }
    public List<ReviewProduct> filterReviews(
            Integer shopId,
            RatingLevel rating,
            LocalDate startDate,
            LocalDate endDate) {

        Specification<ReviewProduct> spec = Specification.where(null);

        // Lọc theo shop
        spec = spec.and((root, query, cb) ->
                cb.equal(root.get("product").get("shop").get("id"), shopId));

        // Lọc theo rating nếu có
        if (rating != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("rating"), rating));
        }

        // Lọc theo khoảng thời gian
        if (startDate != null && endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.between(root.get("createdAt"), startDate, endDate));
        } else if (startDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.greaterThanOrEqualTo(root.get("createdAt"), startDate));
        } else if (endDate != null) {
            spec = spec.and((root, query, cb) ->
                    cb.lessThanOrEqualTo(root.get("createdAt"), endDate));
        }

        // Sửa thành:
        return reviewProductRepository.findAll(spec);
    }
    public Double getAverageRatingByShop(Integer shopId) {
        return reviewProductRepository.averageRatingByShop(shopId);
    }

    public List<ReviewProduct> getRecentReviewsByShop(Integer shopId, int limit) {
        return reviewProductRepository.findRecentByShop(shopId, PageRequest.of(0, limit, Sort.by("createdAt").descending()));
    }
}
