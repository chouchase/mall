package com.chouchase.controller.backend;

import com.chouchase.common.ServerResponse;
import com.chouchase.domain.Product;
import com.chouchase.service.FileService;
import com.chouchase.service.ProductService;
import com.chouchase.util.FileProperty;
import com.chouchase.util.PropertiesUtil;
import com.chouchase.vo.ProductBrief;
import com.chouchase.vo.ProductDetail;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@ResponseBody
@RequestMapping("/manage/product")
public class ManageProductController {
    @Autowired
    private FileProperty fileProperty;
    @Autowired
    private ProductService productService;
    @Autowired
    private FileService fileService;

    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ServerResponse<String> save(Product product){
        return productService.updateOrAddProduct(product);
    }
    @RequestMapping(value = "/set_sale_status",method = RequestMethod.GET)
    public ServerResponse<String> setSaleStatus(Integer productId,Integer status){
        //System.out.println(productId + " " + status);
        //参数校验
        if(productId == null || status == null || status > 1 || status < 0){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return productService.setSaleStatus(productId,status);
    }

}
