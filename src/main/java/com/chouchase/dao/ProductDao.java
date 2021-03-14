package com.chouchase.dao;

import com.chouchase.domain.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;
@Mapper
public interface ProductDao {
    //新增商品
    public int insertProduct(Product product);

    //通过商品Id选择性更新商品
    public int updateProductSelective(Product product);

    //查询商品Id是否存在
    public int checkId(Integer id);

    //通过商品Id更新商品状态
    public int updateProductStatus(@Param("id") Integer productId, @Param("status") Integer status);

    //通过商品Id获取商品信息
    public Product selectProductByPrimaryKey(Integer id);

    //查询所有商品
    public List<Product> selectProducts();

    //根据商品名或商品Id查询商品
    public List<Product> selectProductsByProductNameAndProductId(@Param("productName") String productName, @Param("productId") Integer productId);

    //根据关键字和类别id查询商品
    public List<Product> selectProductsByKeywordAndCategoryIds(@Param("keyword") String keyword, @Param("categoryIds") Set<Integer> categoryIds);
}
