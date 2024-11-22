package com.melihkoc.service.impl;

import com.melihkoc.dto.DtoCategory;
import com.melihkoc.dto.DtoCategoryUI;
import com.melihkoc.dto.DtoUser;
import com.melihkoc.exception.BaseException;
import com.melihkoc.exception.ErrorMessage;
import com.melihkoc.exception.MessageType;
import com.melihkoc.model.Category;
import com.melihkoc.model.UserApp;
import com.melihkoc.repository.CategoryRepository;
import com.melihkoc.repository.UserRepository;
import com.melihkoc.service.ICategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService implements ICategoryService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    private UserApp getCurrentUser(){
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<UserApp> optUsername = userRepository.findByUsername(username);
        if(optUsername.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.USER_NOT_FOUND,username));
        }
        return optUsername.get();
    }

    @Override
    public List<DtoCategory> getCategories() {
        UserApp currentUser = getCurrentUser();
        List<DtoCategory> dtoCategoryList = new ArrayList<>();

        List<Category> categories = categoryRepository.findByUserId(currentUser.getId());
        if(categories!=null && !categories.isEmpty()){
            for (Category category: categories){
                DtoCategory dtoCategory = new DtoCategory();
                BeanUtils.copyProperties(category,dtoCategory);
                dtoCategoryList.add(dtoCategory);
            }
        }

        return dtoCategoryList;
    }



    ////add category
    private Category createCategory(DtoCategoryUI dtoCategoryUI){
        Optional<UserApp> optUser  = userRepository.findById(dtoCategoryUI.getUserId());
        if(optUser.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,dtoCategoryUI.getUserId().toString()));
        }

        Category category = new Category();
        category.setName(dtoCategoryUI.getName());
        category.setUser(optUser.get());
        return category;

    }

    @Override
    public DtoCategory addCategory(DtoCategoryUI dtoCategoryUI) {
        DtoCategory dtoCategory = new DtoCategory();
        DtoUser dtoUser = new DtoUser();

        Category savedCategory = categoryRepository.save(createCategory(dtoCategoryUI));

        BeanUtils.copyProperties(savedCategory,dtoCategory);
        BeanUtils.copyProperties(savedCategory.getUser(),dtoUser);
        dtoCategory.setUser(dtoUser);

        return dtoCategory;
    }

    @Override
    public DtoCategory deleteCategory(Long categoryId) {
        UserApp currentUser = getCurrentUser();
        Optional<Category> optCategory = categoryRepository.findById(categoryId);
        if(optCategory.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,categoryId.toString()));
        }
        Category category = optCategory.get();
        if(!category.getUser().getId().equals(currentUser.getId())){
            throw new BaseException(new ErrorMessage(MessageType.NOT_ACCESS_PERMISSION,""));
        }
        DtoCategory dtoCategory = new DtoCategory();
        BeanUtils.copyProperties(category,dtoCategory);
        categoryRepository.deleteById(categoryId);
        return dtoCategory;
    }

    @Override
    public DtoCategory updateCategory(Long categoryId, DtoCategoryUI dtoCategoryUI) {

        UserApp currentUser = getCurrentUser();

        Optional<Category> optCategory = categoryRepository.findById(categoryId);
        if(optCategory.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,categoryId.toString()));
        }
        if(!optCategory.get().getUser().getId().equals(currentUser.getId())){
            throw new BaseException(new ErrorMessage(MessageType.NOT_ACCESS_PERMISSION,""));
        }
        DtoCategory dtoCategory = new DtoCategory();
        Category dbCategory = optCategory.get();
        dbCategory.setName(dtoCategoryUI.getName());

        Category savedCategory = categoryRepository.save(dbCategory);

        BeanUtils.copyProperties(savedCategory,dtoCategory);

        return dtoCategory;
    }

    @Override
    public DtoCategory getCategoryById(Long categoryId) {
        Optional<Category> optCategory = categoryRepository.findById((categoryId));
        if(optCategory.isEmpty()){
            throw new BaseException(new ErrorMessage(MessageType.NO_RECORD_EXIST,categoryId.toString()));
        }
        DtoCategory dtoCategory = new DtoCategory();
        BeanUtils.copyProperties(optCategory.get(),dtoCategory);
        return dtoCategory;
    }
}
