package com.chouchase.dao;

import com.chouchase.domain.PayInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayInfoDao {
    public int insert(PayInfo payInfo);
    public PayInfo selectByPrimaryKey(Integer id);
}
