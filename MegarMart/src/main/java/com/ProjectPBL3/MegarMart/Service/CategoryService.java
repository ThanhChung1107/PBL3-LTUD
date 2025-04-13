package com.ProjectPBL3.MegarMart.Service;

import com.ProjectPBL3.MegarMart.Entity.Category;
import com.ProjectPBL3.MegarMart.Repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final FileSystemStorageService storageService;

    public Category findById(int id) {return categoryRepository.findById(id).get();}

    public Boolean create(Category category, MultipartFile file){
        try{
            storageService.store(file);
            String filename = file.getOriginalFilename();
            category.setImageurl(filename);
            categoryRepository.save(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Boolean update(Category category){
        try{
            if(categoryRepository.existsById(category.getId())) categoryRepository.save(category);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void deleteById(int id){
        categoryRepository.deleteById(id);
    }

    public List<Category> findAll() {return categoryRepository.findAll();}
}
