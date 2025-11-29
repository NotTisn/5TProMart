package com.fivetpromart.application.port.out;

import com.fivetpromart.domain.model.Category;

public interface ICategoryRepository {
    Category save(Category category);
}
