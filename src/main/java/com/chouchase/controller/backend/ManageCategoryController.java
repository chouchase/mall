package com.chouchase.controller.backend;

import com.chouchase.common.ServerResponse;
import com.chouchase.domain.Category;
import com.chouchase.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/manage/category")
@ResponseBody
public class ManageCategoryController {
    @Autowired
    private CategoryService categoryService;

    @RequestMapping(value = "/add_category", method = RequestMethod.POST)
    public ServerResponse<String> addCategory(@RequestParam(value = "parentId", defaultValue = "0") Integer parentId, String name) {
        //参数校验
        if (StringUtils.isBlank(name)) {
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return categoryService.addCategory(parentId, name);
    }

    @RequestMapping(value = "/update_category_name", method = RequestMethod.POST)
    public ServerResponse<String> updateCategoryName(Integer id, String name) {
        //参数校验
        if (id == null || StringUtils.isBlank(name)) {
            return ServerResponse.createFailResponseByMsg("参数错误");
        }
        return categoryService.changeCategoryName(id, name);
    }

    @RequestMapping(value = "/get_category", method = RequestMethod.GET)
    public ServerResponse<List<Category>> getCategory(@RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {

        return categoryService.getCategory(parentId);
    }

    @RequestMapping(value = "/get_deep_category", method = RequestMethod.GET)
    public ServerResponse<Set<Integer>> getDeepCategory(@RequestParam(value = "parentId", defaultValue = "0") Integer parentId) {
        return categoryService.getDeepCategoryId(parentId);
    }
}
