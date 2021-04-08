package com.chouchase.dao;

import com.chouchase.domain.OrderItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface OrderItemDao {
    public int batchInsert(@Param("orderItems") List<OrderItem> orderItems);
    public List<OrderItem> selectByOrderNo(Long orderNo);
}
