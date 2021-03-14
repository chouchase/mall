package com.chouchase.controller.portal;

import com.chouchase.common.Const;
import com.chouchase.common.ServerResponse;
import com.chouchase.domain.User;
import com.chouchase.service.CartService;
import com.chouchase.vo.CartVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
@ResponseBody
public class CartController {
    @Autowired
    private CartService cartService;
    private Integer getUserId(HttpSession session){
        return ((User) session.getAttribute(Const.CURRENT_USER)).getId();
    }
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ServerResponse<CartVo> add(HttpSession session, Integer productId, Integer count) {

        //参数校验
        if (productId == null || count == null) {
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //获取登录用户的id
        Integer userId = getUserId(session);
        return cartService.add(userId, productId, count);
    }
    @RequestMapping(value = "/update",method = RequestMethod.GET)
    public ServerResponse<CartVo> update(HttpSession session,Integer productId,Integer count){
        //参数校验
        if (productId == null || count == null) {
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //获取登录用户的id
        Integer userId = getUserId(session);
        return cartService.update(userId, productId, count);
    }
    @RequestMapping(value = "/delete",method = RequestMethod.GET)
    public ServerResponse<CartVo> delete(HttpSession session,String productIds){
        //参数校验
        if(StringUtils.isBlank(productIds)){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //获取登录用户的id
        Integer userId = getUserId(session);
        return cartService.deleteProducts(userId,productIds);
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ServerResponse<CartVo> list(HttpSession session){
        //获取登录用户的id
        Integer userId = getUserId(session);
        return cartService.list(userId);
    }
    @RequestMapping(value = "/all_checked",method = RequestMethod.GET)
    public ServerResponse<CartVo> allChecked(HttpSession session){
        //获取登录用户的id
        Integer userId = getUserId(session);
        return cartService.checkedOrUnchecked(userId,null,Const.Cart.checked);
    }

    @RequestMapping(value = "/all_unchecked",method = RequestMethod.GET)
    public ServerResponse<CartVo> allUnchecked(HttpSession session){
        //获取登录用户的id
        Integer userId = getUserId(session);
        return cartService.checkedOrUnchecked(userId,null,Const.Cart.unChecked);
    }
    @RequestMapping(value = "/single_checked",method = RequestMethod.GET)
    public ServerResponse<CartVo> singleChecked(HttpSession session,Integer productId){
        //参数校验
        if(productId == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //获取登录用户的id
        Integer userId = getUserId(session);
        return cartService.checkedOrUnchecked(userId,productId,Const.Cart.checked);
    }
    @RequestMapping(value = "/single_unchecked",method = RequestMethod.GET)
    public ServerResponse<CartVo> singleUnchecked(HttpSession session,Integer productId){
        //参数校验
        if(productId == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //获取登录用户的id
        Integer userId = getUserId(session);
        return cartService.checkedOrUnchecked(userId,productId,Const.Cart.unChecked);
    }
    @RequestMapping(value = "/count",method = RequestMethod.GET)
    public ServerResponse<Integer> count(HttpSession session){
        //获取登录用户的id
        Integer userId = getUserId(session);
        return cartService.count(userId);
    }


}
