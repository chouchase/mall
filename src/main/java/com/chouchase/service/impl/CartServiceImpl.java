package com.chouchase.service.impl;

import com.chouchase.common.Const;
import com.chouchase.common.ServerResponse;
import com.chouchase.dao.CartDao;
import com.chouchase.dao.ProductDao;
import com.chouchase.domain.Cart;
import com.chouchase.domain.Product;
import com.chouchase.service.CartService;
import com.chouchase.util.PropertiesUtil;
import com.chouchase.vo.CartProduct;
import com.chouchase.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartDao cartDao;
    @Autowired
    private ProductDao productDao;

    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        //校验ProductId的的合法性
        if (productDao.checkId(productId) < 1) {
            return ServerResponse.createFailResponseByMsg("商品不存在");
        }
        //查询该商品是否已经存在购物车里
        Cart cartItem = cartDao.selectByUserIdAndProductId(userId, productId);
        //如果存在购物车，则只需要更新数量
        if (cartItem != null) {
            Cart newItem = new Cart();
            newItem.setId(cartItem.getId());
            newItem.setQuantity(cartItem.getQuantity() + count);
            cartDao.updateByPrimaryKeySelective(newItem);
        } else {//如果不存在于购物车，则需要新建一条记录
            cartItem = new Cart();
            cartItem.setQuantity(count);
            cartItem.setProductId(productId);
            cartItem.setChecked(Const.Cart.checked);
            cartItem.setUserId(userId);
            cartDao.insert(cartItem);
        }
        CartVo cartVo = getCartVoLimit(userId);
        return ServerResponse.createSuccessResponseByData(cartVo);
    }

    //获取用户的CartVo对象,如果商品的数量超过库存限制，修改为库存限制
    private CartVo getCartVoLimit(Integer userId) {
        //结果对象
        CartVo cartVo = new CartVo();
        //从数据库中查出该用户的购物车记录
        List<Cart> cartList = cartDao.selectByUserId(userId);
        //购物车商品List
        List<CartProduct> cartProductList = new ArrayList<>();
        //购物车商品价
        BigDecimal cartTotalPrice = new BigDecimal("0");
        //遍历每一条购物车记录
        for (Cart cart : cartList) {
            //组装CartProduct对象
            CartProduct cartProduct = new CartProduct();
            cartProduct.setId(cart.getId());
            cartProduct.setProductId(cart.getProductId());
            cartProduct.setUserId(cart.getUserId());
            cartProduct.setProductChecked(cart.getChecked());
            //从数据库中查询商品对象
            Product product = productDao.selectProductByPrimaryKey(cart.getProductId());
            //实际可以购买的数量
            Integer buyLimit = 0;
            //商品总价
            BigDecimal productTotalPrice = new BigDecimal("0");
            //如果商品没有被删除
            if (product != null) {
                //进一步组装CartProduct对象
                cartProduct.setProductName(product.getName());
                cartProduct.setProductSubtitle(product.getSubtitle());
                cartProduct.setProductPrice(product.getPrice());
                cartProduct.setProductStatus(product.getStatus());
                cartProduct.setProductStock(product.getStock());
                cartProduct.setProductMainImage(product.getMainImage());
                //如果购物车中的数量少于等于商品库存
                if (cart.getQuantity() <= product.getStock()) {
                    //设置LimitQuantity字段
                    cartProduct.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    //实际购买数量就是购物车中的数量
                    buyLimit = cart.getQuantity();
                } else {//否则
                    //设置LimitQuantity字段
                    cartProduct.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                    //实际购买数量就是库存中的数量
                    buyLimit = product.getStock();
                    //更新购物车记录中的购买数量
                    Cart t = new Cart();
                    t.setId(cart.getId());
                    t.setQuantity(buyLimit);
                    cartDao.updateByPrimaryKeySelective(t);
                }
                //计算该件商品的总价格
                productTotalPrice = new BigDecimal(buyLimit.toString()).multiply(product.getPrice());
            }
            //设置可购买数量
            cartProduct.setQuantity(buyLimit);
            //设置商品总价格
            cartProduct.setProductTotalPrice(productTotalPrice);

            cartProductList.add(cartProduct);
            cartTotalPrice = cartTotalPrice.add(cartProduct.getProductTotalPrice());
        }
        cartVo.setCartProductList(cartProductList);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        cartVo.setAllChecked(getAllChecked(userId));
        return cartVo;
    }

    private Boolean getAllChecked(Integer userId) {
        return cartDao.checkAllChecked(userId) == 0;
    }

    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        Cart cart = cartDao.selectByUserIdAndProductId(userId,productId);
        if(cart != null){
            cart.setQuantity(count);
        }else{
            return ServerResponse.createFailResponseByMsg("商品不存在，更新错误");
        }
        cartDao.updateByPrimaryKeySelective(cart);
        return ServerResponse.createSuccessResponseByData(getCartVoLimit(userId));
    }

    @Override
    public ServerResponse<CartVo> deleteProducts(Integer userId, String productIds) {
        String[] productId = productIds.split(",");
        if(productId.length == 0){
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        List<String> productIdList = new ArrayList<>(productId.length);
        Collections.addAll(productIdList,productId);
        cartDao.deleteProductByUserIdProductIds(userId,productIdList);
        return ServerResponse.createSuccessResponseByData(getCartVoLimit(userId));
    }

    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        return ServerResponse.createSuccessResponseByData(getCartVoLimit(userId));
    }

    @Override
    public ServerResponse<CartVo> checkedOrUnchecked(Integer userId, Integer productId, Integer checked) {
        cartDao.updateChecked(userId,productId,checked);
        return ServerResponse.createSuccessResponseByData(getCartVoLimit(userId));
    }

    @Override
    public ServerResponse<Integer> count(Integer userId) {
        return ServerResponse.createSuccessResponseByData(cartDao.countCartQuantity(userId));
    }
}
