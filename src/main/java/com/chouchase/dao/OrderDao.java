package com.chouchase.dao;

import com.chouchase.domain.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

@Mapper
public interface OrderDao {
    public int insert(Order order);
    public Order selectByOrderNo(Long orderNo);
    public Order selectByUserIdAndOrderNo(@Param("userId") Integer userId, @Param("orderNo") Long orderNo);
    public int updateStatusByPrimaryKey(@Param("id") Integer id, @Param("status") Integer status);
    public List<Order> selectByUserId(Integer userId);
    public int sendOrderByPrimaryKey(@Param("id")Integer id,@Param("status")Integer status,@Param("sendTime") Date sendTime);
    public int receiveByPrimaryKey(@Param("id") Integer id,@Param("status")Integer status );

}
