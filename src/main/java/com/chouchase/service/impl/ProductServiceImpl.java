package com.chouchase.service.impl;

import com.chouchase.common.Const;
import com.chouchase.common.ServerResponse;
import com.chouchase.dao.CategoryDao;
import com.chouchase.dao.ProductDao;
import com.chouchase.domain.Category;
import com.chouchase.domain.Product;
import com.chouchase.service.CategoryService;
import com.chouchase.service.ProductService;
import com.chouchase.util.DateUtil;
import com.chouchase.util.PropertiesUtil;
import com.chouchase.vo.ProductBrief;
import com.chouchase.vo.ProductDetail;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductDao productDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CategoryService categoryService;

    @Override
    public ServerResponse<String> updateOrAddProduct(Product product) {
        //校验商品id的合法性、所属类别的合法性
        if ((product.getId() != null && productDao.checkId(product.getId()) < 1) || product.getCategoryId() == null || categoryDao.checkCategoryId(product.getCategoryId()) < 1) {
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //选取subImage的第一张图片作为子图
        if (!StringUtils.isBlank(product.getSubImages())) {
            String[] images = product.getSubImages().split(",");
            product.setMainImage(images[0]);
        }
        //如果id不等于空，代表更新商品
        if (product.getId() != null) {
            //更新商品信息
            int cnt = productDao.updateProductSelective(product);
            if (cnt > 0) {
                return ServerResponse.createSuccessResponseByMsg("商品更新成功");
            }
            return ServerResponse.createFailResponseByMsg("商品更新失败");
        } else {//否则添加新商品
            int cnt = productDao.insertProduct(product);
            if (cnt > 0) {
                return ServerResponse.createSuccessResponseByMsg("商品添加成功");
            }
            return ServerResponse.createFailResponseByMsg("商品添加失败");
        }
    }

    @Override
    public ServerResponse<String> setSaleStatus(Integer productId, Integer status) {
        //查询商品是否存在
        if (productDao.checkId(productId) < 1) {
            return ServerResponse.createFailResponseByMsg("商品不存在");
        }
        //更新商品状态
        if (productDao.updateProductStatus(productId, status) > 0) {
            return ServerResponse.createSuccessResponseByMsg("商品状态更新成功");
        }
        return ServerResponse.createSuccessResponseByMsg("商品状态更新失败");
    }

    @Override
    public ServerResponse<ProductDetail> manageProductDetail(Integer productId) {
        //从数据库获取product
        Product product = productDao.selectProductByPrimaryKey(productId);
        //如果商品不存在
        if (product == null) {
            return ServerResponse.createFailResponseByMsg("商品不存在");
        }
        //组装成ProductDetail对象，并将其放入响应消息对象中
        return ServerResponse.createSuccessResponseByMsgAndData("获取成功", assembleProductDetail(product));

    }

    //将Product对象组装成ProductDetail对象
    private ProductDetail assembleProductDetail(Product product) {
        ProductDetail productDetail = new ProductDetail();
        productDetail.setId(product.getId());
        productDetail.setCategoryId(product.getCategoryId());
        productDetail.setName(product.getName());
        productDetail.setSubtitle(product.getSubtitle());
        productDetail.setMainImage(product.getMainImage());
        productDetail.setSubImages(product.getSubImages());
        productDetail.setDetail(product.getDetail());
        productDetail.setPrice(product.getPrice());
        productDetail.setStatus(product.getStatus());
        productDetail.setStock(product.getStock());

        productDetail.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        Category category = categoryDao.selectCategoryById(product.getCategoryId());
        if (category == null) {
            productDetail.setParentCategoryId(0);
        } else {
            productDetail.setParentCategoryId(category.getParentId());
        }
        productDetail.setCreateTime(DateUtil.dateToStr(product.getCreateTime()));
        productDetail.setUpdateTime(DateUtil.dateToStr(product.getUpdateTime()));
        return productDetail;
    }

    @Override
    public ServerResponse<PageInfo> getProductBriefList(Integer pageNum, Integer pageSize) {
        //参数校验
        if (pageNum < 1 || pageSize < 1) {
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //设置分页信息
        PageHelper.startPage(pageNum, pageSize);
        //查询分页数据
        List<Product> productList = productDao.selectProducts();
        //组装成商品简介对象
        return getPageInfoServerResponse(productList);
    }

    //将Product对象组装成ProductDetail对象
    private ProductBrief assembleProductBrief(Product product) {
        ProductBrief productBrief = new ProductBrief();
        productBrief.setId(product.getId());
        productBrief.setCategoryId(product.getCategoryId());
        productBrief.setName(product.getName());
        productBrief.setSubtitle(product.getSubtitle());
        productBrief.setMainImage(product.getMainImage());
        productBrief.setStatus(product.getStatus());
        productBrief.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        productBrief.setPrice(product.getPrice());
        return productBrief;
    }

    @Override
    public ServerResponse<PageInfo> search(String productName, Integer productId, Integer pageNum, Integer pageSize) {
        //参数校验
        if (pageNum < 1 || pageSize < 1) {
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        //如果关键字不为空，生成数据库搜索参数
        if (StringUtils.isNotBlank(productName)) {
            productName = new StringBuilder().append("%").append(productName).append("%").toString();
        } else {
            productName = null;
        }
        //设置分页参数
        PageHelper.startPage(pageNum, pageSize);
        //从数据库查询商品
        List<Product> productList = productDao.selectProductsByProductNameAndProductId(productName, productId);
        //组装成商品简要对象
        return getPageInfoServerResponse(productList);


    }

    private ServerResponse<PageInfo> getPageInfoServerResponse(List<Product> productList) {
        List<ProductBrief> productBriefList = new ArrayList<>();
        for (Product product : productList) {
            productBriefList.add(assembleProductBrief(product));
        }
        PageInfo pageInfo = new PageInfo(productList);
        pageInfo.setList(productBriefList);
        return ServerResponse.createSuccessResponseByMsgAndData("获取成功", pageInfo);
    }

    @Override
    public ServerResponse<ProductDetail> productDetail(Integer productId) {
        //从数据库获取product
        Product product = productDao.selectProductByPrimaryKey(productId);
        //如果商品不存在
        if (product == null || product.getStatus() != Const.ProductStatus.ON_SALE) {
            return ServerResponse.createFailResponseByMsg("商品不存在");
        }

        //组装成ProductDetail对象，并将其放入响应消息对象中
        return ServerResponse.createSuccessResponseByMsgAndData("获取成功", assembleProductDetail(product));
    }

    @Override
    public ServerResponse<PageInfo> getProductsByKeywordAndCategory(String keyword, Integer categoryId, Integer pageNum, Integer pageSize, String orderBy) {
        //如果关键字不为空，生成数据库搜变量
        if (StringUtils.isNotBlank(keyword)) {
            keyword = new StringBuilder().append("%").append(keyword).append("%").toString();
        } else {
            keyword = null;
        }
        //获取该类别及其子类别的类别Id
        Set<Integer> categoryIds = null;
        if (categoryId != null) {
            categoryIds = categoryService.getDeepCategoryId(categoryId).getData();
        }
        //如果传来的类别无效并且关键字为空，返回空的结果集
        if (keyword == null && categoryIds == null) {
            PageHelper.startPage(pageNum, pageSize);
            List<ProductBrief> productBriefList = new ArrayList<>();
            PageInfo pageInfo = new PageInfo(productBriefList);
            return ServerResponse.createSuccessResponseByMsgAndData("获取成功", pageInfo);
        }
        //分页设置
        PageHelper.startPage(pageNum, pageSize);
        //如果排序参数不为空且有效的话,设置排序参数


        if (StringUtils.isNotBlank(orderBy) && Const.ProductListOrderBy.PRICE_ASC_DESC.contains(orderBy)) {

            String[] orderByArr = orderBy.split("_");
            PageHelper.orderBy(orderByArr[0] + " " + orderByArr[1]);
        }
        System.out.println(keyword);
        //从数据库查询数据
        List<Product> productList = productDao.selectProductsByKeywordAndCategoryIds(keyword, categoryIds);
        //返回结果
        return getPageInfoServerResponse(productList);
    }
}
