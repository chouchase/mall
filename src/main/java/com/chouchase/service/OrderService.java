package com.chouchase.service;

import com.chouchase.common.ServerResponse;
import com.chouchase.vo.OrderVo;

public interface OrderService {
    public ServerResponse<OrderVo> generate(Integer userId,Integer shippingId);
}
