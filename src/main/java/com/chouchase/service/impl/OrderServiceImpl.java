package com.chouchase.service.impl;

import com.chouchase.common.Const;
import com.chouchase.common.OrderStatus;
import com.chouchase.common.PaymentType;
import com.chouchase.common.ServerResponse;
import com.chouchase.dao.*;
import com.chouchase.domain.*;
import com.chouchase.service.CartService;
import com.chouchase.service.OrderService;
import com.chouchase.vo.OrderItemVo;
import com.chouchase.vo.OrderVo;
import com.chouchase.vo.PreOrderVo;
import com.chouchase.vo.ShippingVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
    public ServerResponse<String> receive(Integer userId, Long orderNo) {
        Order order = orderDao.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null){
            return ServerResponse.createFailResponseByMsg("订单不存在");
        }
        if(order.getStatus() != OrderStatus.SENT.getCode()){
            return ServerResponse.createFailResponseByMsg("收货失败");
        }
        int rowCount = orderDao.receiveByPrimaryKey(order.getId(),OrderStatus.SUCCESS.getCode());
        if(rowCount != 1){
            return ServerResponse.createFailResponseByMsg("收货失败");
        }
        return ServerResponse.createSuccessResponseByData("收货成功");

    }

    @Override
    public ServerResponse<String> send(Long orderNo) {
        Order order = orderDao.selectByOrderNo(orderNo);
        if(order == null){
            return ServerResponse.createFailResponseByMsg("订单不存在");
        }
        if(order.getStatus() != OrderStatus.PAID.getCode()){
            return ServerResponse.createFailResponseByMsg("发货失败");
        }
        int rowCount = orderDao.sendOrderByPrimaryKey(order.getId(),OrderStatus.SENT.getCode(), new Date());
        if(rowCount < 1){
            return ServerResponse.createFailResponseByMsg("发货失败");
        }
        return ServerResponse.createSuccessResponseByData("发货成功");

    }

    @Override
    public ServerResponse<OrderVo> manageOrderDetail(Long orderNo) {
        Order order = orderDao.selectByOrderNo(orderNo);
        if(order == null){
            return ServerResponse.createFailResponseByMsg("订单不存在");
        }
        List<OrderItem> orderItemList = orderItemDao.selectByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        return ServerResponse.createSuccessResponseByData(orderVo);
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderDao.selectByUserId(userId);
        List<OrderVo> orderVoList = assembleOrderVoList(orderList);
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return ServerResponse.createSuccessResponseByData(pageInfo);
    }
    private List<OrderVo> assembleOrderVoList(List<Order> orderList){
        List<OrderVo> orderVoList = new ArrayList<>();
        for(Order order : orderList){
            List<OrderItem> orderItemList = orderItemDao.selectByOrderNo(order.getOrderNo());
            OrderVo orderVo = assembleOrderVo(order, orderItemList);
            orderVoList.add(orderVo);
        }
        return orderVoList;
    }

    @Override
    public ServerResponse<OrderVo> getOrderDetail(Integer userId, Long orderNo) {
        Order order = orderDao.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null){
            return ServerResponse.createFailResponseByMsg("用户没有此订单");
        }
        List<OrderItem> orderItemList = orderItemDao.selectByOrderNo(orderNo);
        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        return ServerResponse.createSuccessResponseByData(orderVo);

    }

    @Override
    public ServerResponse<PreOrderVo> preOrder(Integer userId) {
        List<Cart> cartList = cartDao.selectCheckedCartByUserId(userId);
        ServerResponse<List<OrderItem>> response = getOrderItemList(cartList);
        if(!response.isSuccess()){
            return ServerResponse.createFailResponseByMsg(response.getMsg());
        }
        List<OrderItem> orderItemList = response.getData();
        BigDecimal totalPrice = new BigDecimal("0");
        List<OrderItemVo> orderItemVoList = new ArrayList<>();
        for(OrderItem orderItem : orderItemList){
            orderItemVoList.add(assembleOrderItemVo(orderItem));
            totalPrice = totalPrice.add(orderItem.getTotalPrice());
        }
        PreOrderVo preOrderVo = new PreOrderVo();
        preOrderVo.setOrderItemVoList(orderItemVoList);
        preOrderVo.setTotalPrice(totalPrice);
        return ServerResponse.createSuccessResponseByData(preOrderVo);

    }

    @Override
    public ServerResponse<String> cancel(Integer userId, Long orderNo) {
        Order order = orderDao.selectByUserIdAndOrderNo(userId,orderNo);
        if(order == null){
            return ServerResponse.createFailResponseByMsg("此用户不存在该订单");
        }
        if(order.getStatus() != OrderStatus.UNPAID.getCode()){
            return ServerResponse.createFailResponseByMsg("无法取消");
        }
        int rowCount = orderDao.updateStatusByPrimaryKey(order.getId(),OrderStatus.CANCELED.getCode());
        if(rowCount < 1){
            return ServerResponse.createFailResponseByMsg("取消失败");
        }
        return ServerResponse.createSuccessResponseByData("取消订单成功");

    }

    @Override
    public ServerResponse<OrderVo> generate(Integer userId, Integer shippingId) {
        if(shippingDao.check(userId,shippingId) != 1){
            return ServerResponse.createFailResponseByMsg("收货地址不存在");
        }
        //从购物车中选出已勾选的商品
        List<Cart> cartList = cartDao.selectCheckedCartByUserId(userId);
        //把购物车商品组装成订单项
        ServerResponse<List<OrderItem>> serverResponse = getOrderItemList(cartList);
        if(!serverResponse.isSuccess()){
            return ServerResponse.createFailResponseByMsg(serverResponse.getMsg());
        }
        List<OrderItem> orderItemList = serverResponse.getData();
        //计算订单的总价格
        BigDecimal payment = getOrderTotalPrice(orderItemList);
        //生成订单对象
        Order order = assembleOrder(userId,shippingId,payment);
        //把订单对象插入数据库中
        int rowCount = orderDao.insert(order);
        if(rowCount < 1){
            return ServerResponse.createFailResponseByMsg("订单生成失败");
        }
        //为订单项设置订单号
        for(OrderItem orderItem : orderItemList){
            orderItem.setOrderNo(order.getOrderNo());
        }
        //把订单项批量插入数据库中
        orderItemDao.batchInsert(orderItemList);
        //减少商品库存
        reduceProductStock(orderItemList);
        //清空购物车
        cleanCart(cartList);
        //组装视图对象返回给前端
        Long orderNo = order.getOrderNo();
        order = orderDao.selectByOrderNo(orderNo);
        orderItemList = orderItemDao.selectByOrderNo(orderNo);

        OrderVo orderVo = assembleOrderVo(order, orderItemList);
        if(orderVo == null){
            return ServerResponse.createFailResponseByMsg("发生未知错误");
        }
        return ServerResponse.createSuccessResponseByData(orderVo);

    }
    private ShippingVo assembleShippingVo(Shipping shipping){
        ShippingVo shippingVo = new ShippingVo();
        shippingVo.setReceiverName(shipping.getReceiverName());
        shippingVo.setReceiverPhone(shipping.getReceiverPhone());
        shippingVo.setReceiverMobile(shipping.getReceiverMobile());
        shippingVo.setReceiverProvince(shipping.getReceiverProvince());
        shippingVo.setReceiverCity(shipping.getReceiverCity());
        shippingVo.setReceiverDistrict(shipping.getReceiverDistrict());
        shippingVo.setReceiverAddress(shipping.getReceiverAddress());
        shippingVo.setReceiverZip(shipping.getReceiverZip());
        return shippingVo;
    }
    private OrderItemVo assembleOrderItemVo(OrderItem orderItem){
        OrderItemVo orderItemVo = new OrderItemVo();
        orderItemVo.setOrderNo(orderItem.getOrderNo());
        orderItemVo.setProductId(orderItem.getProductId());
        orderItemVo.setProductName(orderItem.getProductName());
        orderItemVo.setProductImage(orderItem.getProductImage());
        orderItemVo.setCurrentUnitPrice(orderItem.getCurrentUnitPrice());
        orderItemVo.setQuantity(orderItem.getQuantity());
        orderItemVo.setTotalPrice(orderItem.getTotalPrice());
        orderItemVo.setCreateTime(orderItem.getCreateTime());
        return orderItemVo;
    }
    private OrderVo assembleOrderVo(Order order,List<OrderItem> orderItemList){
        OrderVo orderVo = new OrderVo();
        orderVo.setOrderNo(order.getOrderNo());
        orderVo.setPayment(order.getPayment());
        orderVo.setPaymentTypeDesc(PaymentType.codeOf(order.getPaymentType()));
        orderVo.setPostage(order.getPostage());
        orderVo.setStatus(order.getStatus());
        orderVo.setStatusDesc(OrderStatus.codeOf(order.getStatus()));
        orderVo.setPaymentTime(order.getPaymentTime());
        orderVo.setSendTime(order.getSendTime());
        orderVo.setEndTime(order.getEndTime());
        orderVo.setCloseTime(order.getCloseTime());
        orderVo.setUpdateTime(order.getUpdateTime());
        List<OrderItemVo> orderItemVoList = new ArrayList<>();
        for(OrderItem orderItem : orderItemList){
            orderItemVoList.add(assembleOrderItemVo(orderItem));
        }
        orderVo.setOrderItemVoList(orderItemVoList);
        Shipping shipping = shippingDao.selectByPrimaryKey(order.getShippingId());
        if(shipping == null){
            return null;
        }
        orderVo.setShippingId(shipping.getId());
        orderVo.setShippingVo(assembleShippingVo(shipping));
        orderVo.setReceiverName(shipping.getReceiverName());
        return orderVo;
    }
    private void reduceProductStock(List<OrderItem> orderItemList){
        for(OrderItem orderItem : orderItemList){
            Product product = productDao.selectProductByPrimaryKey(orderItem.getProductId());
            product.setStock(product.getStock() - orderItem.getQuantity());
            productDao.updateProductSelective(product);
        }
    }
    private void cleanCart(List<Cart> cartList){
        for(Cart cart : cartList){
            cartDao.deleteByPrimaryKey(cart.getId());
        }

    }
    private Order assembleOrder(Integer userId, Integer shippingId, BigDecimal payment){
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setPayment(payment);
        order.setPostage(0);
        order.setShippingId(shippingId);
        order.setUserId(userId);
        order.setPaymentType(PaymentType.ONLINE_PAY.getCode());
        order.setStatus(OrderStatus.UNPAID.getCode());
        return order;

    }
    private BigDecimal getOrderTotalPrice(List<OrderItem> orderItemList){
        BigDecimal totalPrice = new BigDecimal("0");
        for(OrderItem orderItem : orderItemList){
            totalPrice = totalPrice.add(orderItem.getTotalPrice());
        }
        return totalPrice;
    }
    private ServerResponse<List<OrderItem>> getOrderItemList(List<Cart> cartList){
        if(CollectionUtils.isEmpty(cartList)){
            return ServerResponse.createFailResponseByMsg("购物车为空");
        }
        List<OrderItem> orderItemList = new ArrayList<>();
        for(Cart cart: cartList){
            Product product = productDao.selectProductByPrimaryKey(cart.getProductId());
            if(product.getStatus() == Const.ProductStatus.OFF_SALE){
                return ServerResponse.createFailResponseByMsg(product.getName() + "已下架");
            }
            if(product.getStock() < cart.getQuantity()){
                return ServerResponse.createFailResponseByMsg(product.getName() + "库存不足");
            }
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cart.getProductId());
            orderItem.setQuantity(cart.getQuantity());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.setCurrentUnitPrice(product.getPrice());
            orderItem.setUserId(cart.getUserId());
            orderItem.setTotalPrice(orderItem.getCurrentUnitPrice().multiply(new BigDecimal(orderItem.getQuantity())));
            orderItemList.add(orderItem);
        }
        return ServerResponse.createSuccessResponseByData(orderItemList);
    }
    private Long generateOrderNo(){
        return System.currentTimeMillis() + new Random().nextInt(100);
    }

}
