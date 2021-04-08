package com.chouchase.controller.portal;

import com.chouchase.common.Const;
import com.chouchase.common.ServerResponse;
import com.chouchase.domain.PayInfo;
import com.chouchase.domain.User;
import com.chouchase.service.PayService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


@RestController
@RequestMapping("/user")
public class PayController {
    @Autowired
    private PayService payService;
    @PostMapping("pay")
    public ServerResponse<PayInfo> pay(HttpSession session,Long orderNo, String password){
        if(orderNo == null || StringUtils.isBlank(password)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return payService.pay(user.getId(),orderNo,password);
    }
}
