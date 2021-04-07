package com.chouchase.service.impl;

import com.chouchase.common.Const;
import com.chouchase.common.ServerResponse;
import com.chouchase.dao.*;
import com.chouchase.domain.*;
import com.chouchase.service.CartService;
import com.chouchase.service.OrderService;
import com.chouchase.vo.CartProduct;
import com.chouchase.vo.CartVo;
import com.chouchase.vo.OrderVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private ShippingDao shippingDao;
    @Autowired
    private CartDao cartDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderItemDao orderItemDao;
    @Autowired
    private OrderDao orderDao;
    @Override
    public ServerResponse<OrderVo> generate(Integer userId, Integer shippingId) {
        if(shippingDao.checkId(shippingId) != 1){
            return ServerResponse.createFailResponseByMsg("收货地址不存在");
        }
        CartVo cartVoLimit = cartService.getCartVoLimit(userId);
        List<CartProduct> cartProductList = cartVoLimit.getCartProductList();

        if(cartProductList == null || cartProductList.size() == 0){
            return ServerResponse.createFailResponseByMsg("购物车为空，无法生成订单");
        }
        Order order = new Order();
        BigInteger orderNo = orderNo();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setShippingId(shippingId);
        order.setPostage(0);
        order.setStatus(Const.OrderStatus.unpaid);
        BigDecimal payment = new BigDecimal(0);
        List<OrderItem> orderItemList = new ArrayList<>();
        for(CartProduct cartProduct : cartProductList){
            if(cartProduct.getProductChecked() == Const.Cart.unChecked){
                continue;
            }
            OrderItem item = new OrderItem();
            item.setUserId(userId);
            item.setOrderNo(orderNo);
            item.setProductId(cartProduct.getProductId());
            item.setProductName(cartProduct.getProductName());
            item.setProductImage(cartProduct.getProductMainImage());
            item.setCurrentUnitPrice(cartProduct.getProductPrice());
            item.setQuantity(cartProduct.getQuantity());
            item.setTotalPrice(cartProduct.getProductTotalPrice());
            payment = payment.add(item.getTotalPrice());
            orderItemList.add(item);
        }
        order.setPayment(payment);
        orderDao.insert(order);
        orderItemDao.insert(orderItemList);




    }
    private BigInteger orderNo(){
        return BigInteger.valueOf((System.currentTimeMillis() + new Random().nextInt()) % 10);
    }
}
