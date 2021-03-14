package com.chouchase.controller.portal;

import com.chouchase.common.Const;
import com.chouchase.common.ServerResponse;
import com.chouchase.domain.Shipping;
import com.chouchase.domain.User;
import com.chouchase.service.ShippingService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RequestMapping("/shipping")
@ResponseBody
@Controller
public class ShippingController {
    @Autowired
    private ShippingService shippingService;
    @RequestMapping(value = "/add",method = RequestMethod.GET)
    public ServerResponse<Map<String,Integer>> add(HttpSession session, Shipping shipping){
        //获取用户id
        Integer userId = ((User) session.getAttribute(Const.CURRENT_USER)).getId();
        shipping.setUserId(userId);
        return shippingService.add(shipping);
    }
    @RequestMapping(value = "/update",method = RequestMethod.GET)
    public ServerResponse<String> update(HttpSession session,Shipping shipping){
        //获取用户id
        Integer userId = ((User) session.getAttribute(Const.CURRENT_USER)).getId();
        shipping.setUserId(userId);
        return shippingService.update(shipping);
    }
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ServerResponse<String> delete(HttpSession session,Integer shippingId){
        //获取用户id
        Integer userId = ((User) session.getAttribute(Const.CURRENT_USER)).getId();
        return shippingService.delete(userId,shippingId);
    }
    @RequestMapping(value = "/select",method = RequestMethod.GET)
    public ServerResponse<Shipping> select(HttpSession session,Integer shippingId){
        //参数校验
        if(shippingId == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //获取用户id
        Integer userId = ((User) session.getAttribute(Const.CURRENT_USER)).getId();
        return shippingService.select(userId,shippingId);
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ServerResponse<PageInfo> list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        //获取用户id
        Integer userId = ((User) session.getAttribute(Const.CURRENT_USER)).getId();
        return shippingService.list(userId,pageNum,pageSize);
    }

}
