package com.chouchase.service;

import com.chouchase.common.ServerResponse;
import com.chouchase.vo.OrderVo;
import com.chouchase.vo.PreOrderVo;
import com.github.pagehelper.PageInfo;

import javax.servlet.http.HttpSession;

public interface OrderService {
    public ServerResponse<OrderVo> generate(Integer userId,Integer shippingId);
    public ServerResponse<String> cancel(Integer userId, Long orderNo);
    public ServerResponse<PreOrderVo> preOrder(Integer userId);
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo);
    public ServerResponse<PageInfo> list(Integer userId,Integer pageNum,Integer pageSize);
    public ServerResponse<OrderVo> manageOrderDetail(Long orderNo);
    public ServerResponse<String> send(Long orderNo);
    public ServerResponse<String> receive(Integer userId,Long orderNo);
}
