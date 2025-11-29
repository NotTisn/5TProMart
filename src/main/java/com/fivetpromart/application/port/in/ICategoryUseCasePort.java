package com.fivetpromart.application.port.in;

import com.fivetpromart.application.dto.CategoryDto;

public interface ICategoryUseCasePort {
    CategoryDto addNewCategory(String categoryName);
}
