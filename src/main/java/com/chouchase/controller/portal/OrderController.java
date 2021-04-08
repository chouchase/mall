package com.chouchase.controller.portal;

import com.chouchase.common.Const;
import com.chouchase.common.ServerResponse;
import com.chouchase.domain.User;
import com.chouchase.service.OrderService;
import com.chouchase.vo.OrderVo;
import com.chouchase.vo.PreOrderVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/generate")
    public ServerResponse<OrderVo> generate(HttpSession session, Integer shippingId){
        if(shippingId == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return orderService.generate(user.getId(), shippingId);
    }
    @GetMapping("/cancel")
    public ServerResponse<String> cancel(HttpSession session, Long orderNo){
        if(orderNo == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return orderService.cancel(user.getId(),orderNo);
    }
    @GetMapping("/pre_order")
    public ServerResponse<PreOrderVo> preOrder(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return orderService.preOrder(user.getId());
    }
    @GetMapping("/order_detail")
    public ServerResponse<OrderVo> getOrderDetail(HttpSession session,Long orderNo){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return orderService.getOrderDetail(user.getId(),orderNo);
    }
    @GetMapping("/list")
    public ServerResponse<PageInfo> list(HttpSession session, @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return orderService.list(user.getId(),pageNum,pageSize);
    }
    @GetMapping("/receive")
    public ServerResponse<String> receive(HttpSession session,Long orderNo){
        if(orderNo == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return orderService.receive(user.getId(),orderNo);
    }

}
