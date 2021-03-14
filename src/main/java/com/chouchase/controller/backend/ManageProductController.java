package com.chouchase.controller.backend;

import com.chouchase.common.ServerResponse;
import com.chouchase.domain.Product;
import com.chouchase.service.FileService;
import com.chouchase.service.ProductService;
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
    private ProductService productService;
    @Autowired
    private FileService fileService;
    @RequestMapping(value = "/save",method = RequestMethod.GET)
    public ServerResponse<String> save(Product product){
        //参数校验
        if(StringUtils.isAnyBlank(product.getName()) || product.getCategoryId() == null){
            System.out.println("我");
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return productService.updateOrAddProduct(product);
    }
    @RequestMapping(value = "/set_sale_status",method = RequestMethod.GET)
    public ServerResponse<String> setSaleStatus(Integer productId,Integer status){
        //参数校验
        if(productId == null || status == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return productService.setSaleStatus(productId,status);
    }
    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    public ServerResponse<ProductDetail> getDetail(Integer productId){
        //参数校验
        if(productId == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return productService.manageProductDetail(productId);
    }
    @RequestMapping(value = "/list",method = RequestMethod.GET)
    public ServerResponse<PageInfo> getList(@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        return productService.getProductBriefList(pageNum,pageSize);
    }
    @RequestMapping(value = "/search",method = RequestMethod.GET)
    public ServerResponse<PageInfo> search(String productName,Integer productId,@RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "10")Integer pageSize){
        return productService.search(productName,productId,pageNum,pageSize);
    }
    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public ServerResponse test(HttpServletRequest request){
        System.out.println(request.getServletContext().getRealPath("upload"));
        return null;
    }
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ServerResponse<Map<String,String>> upload(MultipartFile file, HttpServletRequest request){
        if(file == null){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //获取绝对目录的绝对路径
        String path = request.getServletContext().getRealPath("images");
        //上传并获取最终文件名
        String fileName = fileService.upload(file, path);
        //如果文件名为空，表示上传失败
        if(StringUtils.isBlank(fileName)){
            return ServerResponse.createFailResponseByMsg("上传失败");
        }
        //上传成功
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("uri",fileName);
        resultMap.put("url", PropertiesUtil.getProperty("ftp.server.http.prefix") + fileName);
        return ServerResponse.createSuccessResponseByMsgAndData("上传成功",resultMap);
    }
    @RequestMapping(value = "/richtext_img_upload",method = RequestMethod.POST)
    public Map<String,Object> richtextImgUpload(MultipartFile file, HttpServletRequest request, HttpServletResponse response){
        Map<String,Object> resultMap = new HashMap<>();
        if(file == null){
            resultMap.put("success",false);
            resultMap.put("msg","参数错误");
            return resultMap;
        }
        //获取绝对目录的绝对路径
        String path = request.getServletContext().getRealPath("images");
        //上传并获取最终文件名
        String fileName = fileService.upload(file, path);
        //如果文件名为空，表示上传失败
        if(fileName == null){
            resultMap.put("success",false);
            resultMap.put("msg","上传失败");
            return resultMap;
        }
        response.addHeader("Access-Control-Allow-Headers","X-File-Name");
        //上传成功
        resultMap.put("success",true);
        resultMap.put("msg","上传成功");
        resultMap.put("file_path",PropertiesUtil.getProperty("ftp.server.http.prefix")+fileName);
        return resultMap;
    }
}
