package com.chouchase.service;

import com.chouchase.common.ServerResponse;
import com.chouchase.domain.Category;

import java.util.List;
import java.util.Set;

public interface CategoryService {
    public ServerResponse<String> addCategory(Integer parentId,String categoryName);
    public ServerResponse<String> changeCategoryName(Integer id, String categoryName);
    public ServerResponse<List<Category>> getCategory(Integer parentId);
    public ServerResponse<Set<Integer>> getDeepCategoryId(Integer parentId);
}
