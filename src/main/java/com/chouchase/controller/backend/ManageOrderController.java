package com.chouchase.controller.backend;

import com.chouchase.common.Const;
import com.chouchase.common.ServerResponse;
import com.chouchase.domain.User;
import com.chouchase.service.OrderService;
import com.chouchase.vo.OrderVo;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/manage/order")
public class ManageOrderController {
    @Autowired
    private OrderService orderService;
    @GetMapping("/list")
    public ServerResponse<PageInfo> list( @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
                                         @RequestParam(value = "pageSize",defaultValue = "10") Integer pageSize){
        return orderService.list(null,pageNum,pageSize);
    }
    @GetMapping("/order_detail")
    public ServerResponse<OrderVo> detail(Long orderNo){
        if(orderNo == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return orderService.manageOrderDetail(orderNo);

    }
    @GetMapping("/send")
    public ServerResponse<String> send(Long orderNo){
        if(orderNo == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return orderService.send(orderNo);
    }
}
