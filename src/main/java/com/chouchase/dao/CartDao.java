package com.chouchase.dao;

import com.chouchase.common.ServerResponse;
import com.chouchase.domain.Cart;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface CartDao {
    //选择性更新记录
    public int updateByPrimaryKeySelective(Cart cart);

    //根据用户Id和产品Id查询记录
    public Cart selectByUserIdAndProductId(@Param("userId") Integer userId, @Param("productId") Integer productId);

    //根据用户Id查询记录
    public List<Cart> selectByUserId(Integer userId);

    //检查用户的购物车商品是否全选
    public int checkAllChecked(Integer userId);

    //插入购物车记录
    public int insert(Cart cart);

    public int deleteProductByUserIdProductIds(@Param("userId") Integer userId, @Param("productIdList") List<String> productIdList);

    public int updateChecked(@Param("userId") Integer userId, @Param("productId") Integer productId, @Param("checked") Integer checked);

    public Integer countCartQuantity(Integer userId);

    public List<Cart> selectByUserIdAndChecked(@Param("userId") Integer userId, @Param("checked") Integer checked);

}
