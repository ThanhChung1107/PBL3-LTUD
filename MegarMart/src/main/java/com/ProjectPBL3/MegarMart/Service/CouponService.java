package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Category;
import com.ProjectPBL3.MegarMart.Entity.Coupon;
import com.ProjectPBL3.MegarMart.Repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {
    private final CouponRepository couponRepository;

    public boolean save(Coupon coupon) {
        try{
            Coupon existed = couponRepository.findByCode(coupon.getCode());
            if (existed != null && existed.getId() != coupon.getId()) {
                // CODE đã được dùng bởi coupon khác
                return false;
            }
            coupon.setCount(coupon.getCount());
            couponRepository.save(coupon);
            return true;
        }catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean update(Coupon coupon) {
        try {
            // Kiểm tra có tồn tại ID không
            if (couponRepository.existsById(coupon.getId())) {

                // Kiểm tra trùng CODE với coupon khác (khác ID)
                Coupon existingCoupon = couponRepository.findByCode(coupon.getCode());
                if (existingCoupon != null && existingCoupon.getId() != coupon.getId()) {
                    // CODE đã được dùng bởi coupon khác
                    return false;
                }
                couponRepository.save(coupon);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    public List<Coupon> findAll(){return couponRepository.findAll();}

    public Coupon findById(int id) {return couponRepository.findById(id).orElse(null);}

    public Coupon findByCode(String voucher) {return couponRepository.findByCode(voucher);}
}
