package com.chouchase.service;

import com.chouchase.common.ServerResponse;
import com.chouchase.domain.Product;
import com.chouchase.vo.ProductBrief;
import com.chouchase.vo.ProductDetail;
import com.github.pagehelper.PageInfo;

public interface ProductService {
    //更新或新增商品
    public ServerResponse<String> updateOrAddProduct(Product product);

    //更新商品状态
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status);

    //获取商品详细信息
    public ServerResponse<ProductDetail> manageProductDetail(Integer productId);

    //分页获取商品简要信息
    public ServerResponse<PageInfo> getProductBriefList(Integer pageNum, Integer pageSize);

    //根据商品id或者商品关键字搜索商品
    public ServerResponse<PageInfo> search(String productName, Integer productId, Integer pageNum, Integer pageSize);

    //前台通过商品id获取商品详细信息
    public ServerResponse<ProductDetail> productDetail(Integer productId);

    //根据商品类别或关键字搜索商品
    public ServerResponse<PageInfo> getProductsByKeywordAndCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy);

}
