package com.melihkoc.controller.impl;

import com.melihkoc.controller.ICategoryController;
import com.melihkoc.controller.RestBaseController;
import com.melihkoc.controller.RootEntity;
import com.melihkoc.dto.DtoCategory;
import com.melihkoc.dto.DtoCategoryUI;
import com.melihkoc.model.Category;
import com.melihkoc.service.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/api/category")
public class CategoryController extends RestBaseController implements ICategoryController {

    @Autowired
    private ICategoryService categoryService;

    @GetMapping("/get")
    @Override
    public RootEntity<List<DtoCategory>> getCategories() {
        return ok(categoryService.getCategories());
    }


    @PostMapping("/save")
    @Override
    public RootEntity<DtoCategory> addCategory(@Valid @RequestBody DtoCategoryUI dtoCategoryUI) {
        return ok(categoryService.addCategory(dtoCategoryUI));
    }

    @DeleteMapping("/delete/{category_id}")
    @Override
    public RootEntity<DtoCategory> deleteCategory(@PathVariable(name = "category_id") Long categoryId) {
        return ok(categoryService.deleteCategory(categoryId));
    }

    @PutMapping("/update/{category_id}")
    @Override
    public RootEntity<DtoCategory> updateCategory(@PathVariable(name = "category_id") Long categoryId, @RequestBody DtoCategoryUI dtoCategoryUI) {
        return ok(categoryService.updateCategory(categoryId,dtoCategoryUI));
    }

    @GetMapping("/get/{id}")
    @Override
    public RootEntity<DtoCategory> getCategoryById(@PathVariable(name = "id") Long categoryId) {
        return ok(categoryService.getCategoryById(categoryId));
    }

}
