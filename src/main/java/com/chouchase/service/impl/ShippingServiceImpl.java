package com.chouchase.service.impl;

import com.chouchase.common.ServerResponse;
import com.chouchase.dao.ShippingDao;
import com.chouchase.domain.Shipping;
import com.chouchase.service.ShippingService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class ShippingServiceImpl implements ShippingService {
    @Autowired
    private ShippingDao shippingDao;
    @Override
    public ServerResponse<Map<String, Integer>> add(Shipping shipping) {
        int rowCount = shippingDao.insert(shipping);
        if(rowCount > 0){
            Map<String,Integer> map = new HashMap<>();
            map.put("shippingId",shipping.getId());
            return ServerResponse.createSuccessResponseByMsgAndData("添加成功",map);
        }
        return ServerResponse.createFailResponseByMsg("添加失败");
    }

    @Override
    public ServerResponse<String> update(Shipping shipping) {
        int rowCount = shippingDao.updateByPrimaryKeySelective(shipping);
        if(rowCount > 0){
            return ServerResponse.createSuccessResponseByMsg("更新成功");
        }
        return ServerResponse.createFailResponseByMsg("更新失败");
    }

    @Override
    public ServerResponse<String> delete(Integer userId, Integer shippingId) {
        int rowCount = shippingDao.delete(userId,shippingId);
        if(rowCount > 0){
            return ServerResponse.createSuccessResponseByMsg("删除成功");
        }
        return ServerResponse.createFailResponseByMsg("删除失败");
    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        List<Shipping> shipping = shippingDao.select(userId,shippingId);
        if(shipping != null && shipping.size() != 0){
            return ServerResponse.createSuccessResponseByData(shipping.get(0));
        }
        return ServerResponse.createFailResponseByMsg("地址不存在");
    }

    @Override
    public ServerResponse<PageInfo> list(Integer userId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Shipping> shippings = shippingDao.select(userId,null);
        PageInfo pageInfo = new PageInfo(shippings);
        return ServerResponse.createSuccessResponseByData(pageInfo);
    }
}
