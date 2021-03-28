package com.chouchase.controller.portal;

import com.chouchase.common.ServerResponse;
import com.chouchase.domain.Category;
import com.chouchase.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @RequestMapping(value = "/get_category", method = RequestMethod.GET)
    public ServerResponse<List<Category>> getCategory(@RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {

        return categoryService.getCategory(parentId);
    }

    @RequestMapping(value = "/get_deep_category", method = RequestMethod.GET)
    public ServerResponse<List<Integer>> getDeepCategory(@RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        return categoryService.getDeepCategoryId(parentId);
    }
}
