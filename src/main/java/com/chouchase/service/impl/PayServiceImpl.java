package com.chouchase.service.impl;

import com.chouchase.common.OrderStatus;
import com.chouchase.common.ServerResponse;
import com.chouchase.dao.OrderDao;
import com.chouchase.dao.PayInfoDao;
import com.chouchase.dao.UserDao;
import com.chouchase.domain.Order;
import com.chouchase.domain.PayInfo;
import com.chouchase.domain.User;
import com.chouchase.service.PayService;
import com.chouchase.util.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PayInfoDao payInfoDao;
    @Override
    public ServerResponse<PayInfo> pay(Integer userId,Long orderNo, String password) {
        String encryptPass = MD5Utils.encrypt(password);
        int rowCount = userDao.checkPasswordByUserId(userId,encryptPass);
        if(rowCount != 1){
            return ServerResponse.createFailResponseByMsg("密码错误，支付失败");
        }
        Order order = orderDao.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null){
            return ServerResponse.createFailResponseByMsg("订单不存在");
        }
        if(order.getStatus() != OrderStatus.UNPAID.getCode()){
            return ServerResponse.createFailResponseByMsg("支付失败");
        }
        rowCount = orderDao.updateStatusByPrimaryKey(order.getId(), OrderStatus.PAID.getCode());
        if(rowCount != 1){
            return ServerResponse.createFailResponseByMsg("支付失败");
        }
        PayInfo payInfo = new PayInfo();
        payInfo.setOrderNo(orderNo);
        payInfo.setUserId(userId);
        payInfo.setPayment(order.getPayment());
        payInfoDao.insert(payInfo);
        payInfo = payInfoDao.selectByPrimaryKey(payInfo.getId());
        return ServerResponse.createSuccessResponseByData(payInfo);


    }
}
