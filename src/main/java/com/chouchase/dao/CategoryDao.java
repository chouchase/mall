package com.chouchase.dao;

import com.chouchase.domain.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
@Mapper
public interface CategoryDao {
    //通过Id查询类别是否存在
    public int checkCategoryId(Integer id);
    //插入新的类别
    public int insertCategory(Category category);
    //通过Id选择性的更新类别
    public int updateCategorySelectiveById(Category category);
    //获取下一级子类别
    public List<Category> selectCategoryByParentId(Integer parentId);
    //通过父类Id获取所有子类Id
    public List<Integer> selectCategoryIdByParentId(Integer parentId);
    //通过Id获取类别
    public Category selectCategoryById(Integer id);

}
