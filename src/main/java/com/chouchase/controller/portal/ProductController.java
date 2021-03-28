package com.chouchase.controller.portal;

import com.chouchase.common.ServerResponse;
import com.chouchase.service.ProductService;
import com.chouchase.util.DateUtil;
import com.chouchase.vo.ProductDetail;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
@RequestMapping("/product")
@ResponseBody
public class ProductController {
    @Autowired
    private ProductService productService;
    @GetMapping("/test")
    public Date getDate(){
        System.out.println(new Date());
        System.out.println(DateUtil.dateToStr(new Date()));
        return new Date();
    }
    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ServerResponse<ProductDetail> getDetail(Integer productId) {
        //参数校验
        if (productId == null) {
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return productService.productDetail(productId);
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ServerResponse<PageInfo> getList(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        System.out.println(pageNum+" "+pageSize);
        return productService.getProductBriefList(pageNum,pageSize);
    }
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ServerResponse<PageInfo> search(String keyword, Integer categoryId,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {

        return productService.getProductsByKeywordAndCategory(keyword, categoryId, pageNum, pageSize, orderBy);

    }
}
