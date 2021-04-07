package com.chouchase.dao;

import com.chouchase.domain.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDao {
    public int insert(Order order);
}
