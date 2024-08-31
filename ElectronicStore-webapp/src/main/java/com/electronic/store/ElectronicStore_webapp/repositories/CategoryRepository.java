package com.electronic.store.ElectronicStore_webapp.repositories;

import com.electronic.store.ElectronicStore_webapp.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,String>  {//entity and id pass
}
