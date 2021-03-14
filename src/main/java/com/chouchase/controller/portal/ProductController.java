package com.chouchase.controller.portal;

import com.chouchase.common.ServerResponse;
import com.chouchase.service.ProductService;
import com.chouchase.vo.ProductDetail;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/product")
@ResponseBody
public class ProductController {
    @Autowired
    private ProductService productService;

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ServerResponse<ProductDetail> getDetail(Integer productId) {
        //参数校验
        if (productId == null) {
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return productService.productDetail(productId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public ServerResponse<PageInfo> getList(String keyword, Integer categoryId,
                                            @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                            @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                            @RequestParam(value = "orderBy", defaultValue = "") String orderBy) {
        //参数校验，如果关键字和类别都为空，代表参数错误
        if (StringUtils.isBlank(keyword) && categoryId == null) {
            return ServerResponse.createFailResponseByMsg("参数错误");
        }

        return productService.getProductsByKeywordAndCategory(keyword, categoryId, pageNum, pageSize, orderBy);

    }
}
