package com.electronic.store.ElectronicStore_webapp.helper;

import com.electronic.store.ElectronicStore_webapp.dtos.PageableResponse;
import com.electronic.store.ElectronicStore_webapp.dtos.UserDto;
import com.electronic.store.ElectronicStore_webapp.entities.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class Helper {
    public static <U,V> PageableResponse<V> getPageableResponse(Page<U> page, Class<V> type){
        List<U> allUser = page.getContent();
        List<V> userDtos = allUser.stream().map(object -> new ModelMapper().map(object,type)).collect(Collectors.toList());

        PageableResponse<V> response = new PageableResponse<>();
        response.setContent(userDtos);
        response.setPageNumber(page.getNumber());
        response.setTotalPages(page.getTotalPages());
        response.setPageSize(page.getSize());
        response.setLastPage(page.isLast());

        return response;

    }
}
