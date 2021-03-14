package com.chouchase.service;

import com.chouchase.common.ServerResponse;
import com.chouchase.domain.Shipping;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface ShippingService {

    public ServerResponse<Map<String, Integer>> add(Shipping shipping);

    public ServerResponse<String> update(Shipping shipping);

    public ServerResponse<String> delete(Integer userId, Integer shippingId);

    public ServerResponse<Shipping> select(Integer userId, Integer shippingId);

    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize);

}
