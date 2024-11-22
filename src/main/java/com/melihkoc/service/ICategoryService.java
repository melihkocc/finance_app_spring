package com.melihkoc.service;

import com.melihkoc.dto.DtoCategory;
import com.melihkoc.dto.DtoCategoryUI;

import java.util.List;

public interface ICategoryService {

    public List<DtoCategory> getCategories();

    public DtoCategory addCategory(DtoCategoryUI dtoCategoryUI);

    public DtoCategory deleteCategory(Long categoryId);

    public DtoCategory updateCategory(Long categoryId, DtoCategoryUI dtoCategoryUI);

    public DtoCategory getCategoryById(Long categoryId);
}
