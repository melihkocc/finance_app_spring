package com.melihkoc.controller;

import com.melihkoc.dto.DtoCategory;
import com.melihkoc.dto.DtoCategoryUI;
import com.melihkoc.model.Category;

import java.util.List;

public interface ICategoryController {

    public RootEntity<DtoCategory> addCategory(DtoCategoryUI dtoCategoryUI);

    public RootEntity<List<DtoCategory>> getCategories();

    public RootEntity<DtoCategory> deleteCategory(Long categoryId);

    public RootEntity<DtoCategory> updateCategory(Long categoryId, DtoCategoryUI dtoCategoryUI);

    public RootEntity<DtoCategory> getCategoryById (Long categoryId);
}
