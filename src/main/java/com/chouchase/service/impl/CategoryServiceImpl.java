package com.chouchase.service.impl;

import com.chouchase.common.ServerResponse;
import com.chouchase.dao.CategoryDao;
import com.chouchase.domain.Category;
import com.chouchase.service.CategoryService;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryDao categoryDao;
    @Override
    public ServerResponse<String> addCategory(Integer parentId, String categoryName) {
        //如果不是根类别，校验父类别是否存在
        if(parentId != 0){
            int cnt = categoryDao.checkCategoryId(parentId);
            if(cnt == 0){
                return ServerResponse.createFailResponseByMsg("参数错误");
            }
        }
        //设置领域对象的值
        Category category = new Category();
        category.setParentId(parentId);
        category.setStatus(true);
        category.setName(categoryName);

        int cnt = categoryDao.insertCategory(category);

        if(cnt > 0){
            return ServerResponse.createSuccessResponseByMsg("添加成功");
        }
        return ServerResponse.createFailResponseByMsg("添加失败");
    }

    @Override
    public ServerResponse<String> changeCategoryName(Integer id, String categoryName) {
        //创建领域对象
        Category category = new Category();
        category.setId(id);
        category.setName(categoryName);
        //更新类别名
        int cnt = categoryDao.updateCategorySelectiveById(category);

        if(cnt > 0){
            return ServerResponse.createSuccessResponseByMsg("更新成功");
        }
        return ServerResponse.createFailResponseByMsg("修改失败");
    }

    @Override
    public ServerResponse<List<Category>> getCategory(Integer parentId) {
        //校验品类是否存在
        if(parentId != 0){
            int cnt = categoryDao.checkCategoryId(parentId);
            if(cnt < 1){
                return ServerResponse.createFailResponseByMsg("品类未找到");
            }
        }
        //获取子类别
        List<Category> list = categoryDao.selectCategoryByParentId(parentId);
        //将子类别加入响应消息对象
        return ServerResponse.createSuccessResponseByMsgAndData("获取成功",list);
    }

    @Override
    public ServerResponse<Set<Integer>> getDeepCategoryId(Integer parentId) {
        //查询品类是否存在
        if(parentId != 0){
            int cnt = categoryDao.checkCategoryId(parentId);
            if(cnt < 1){
                return ServerResponse.createFailResponseByMsg("品类不存在");
            }
        }
        //层序遍历类别树，获取结果
        List<Integer> list = null;
        Queue<Integer> queue = new ArrayDeque<>();
        queue.add(parentId);
        Set<Integer> set = new HashSet<>();
        set.add(parentId);
        while(!queue.isEmpty()){
            Integer t = queue.poll();
            list = categoryDao.selectCategoryIdByParentId(t);
            for(Integer next : list){
                if(!set.contains(next)){
                    queue.add(next);
                    set.add(next);
                }
            }
        }
        return ServerResponse.createSuccessResponseByMsgAndData("获取成功",set);
    }

}
