package com.chouchase.service;

import com.chouchase.common.ServerResponse;
import com.chouchase.vo.CartVo;

public interface CartService {
    //往用户购物车里添加给定数量的商品
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count);

    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count);

    public ServerResponse<CartVo> deleteProducts(Integer userId, String productIds);

    public ServerResponse<CartVo> list(Integer userId);
    public ServerResponse<CartVo> checkedOrUnchecked(Integer userId, Integer productId,Integer checked);

    public ServerResponse<Integer> count(Integer userId);

}
