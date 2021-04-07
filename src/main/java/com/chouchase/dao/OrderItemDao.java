package com.chouchase.dao;

import com.chouchase.domain.OrderItem;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
@Mapper
public interface OrderItemDao {
    public int insert(List<OrderItem> orderItems);
}
