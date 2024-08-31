package com.electronic.store.ElectronicStore_webapp.controllers;

import com.electronic.store.ElectronicStore_webapp.dtos.ApiResponseMessages;
import com.electronic.store.ElectronicStore_webapp.dtos.ImageResponse;
import com.electronic.store.ElectronicStore_webapp.dtos.PageableResponse;
import com.electronic.store.ElectronicStore_webapp.dtos.UserDto;
import com.electronic.store.ElectronicStore_webapp.services.FileService;
import com.electronic.store.ElectronicStore_webapp.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Value("${user.profile.image.path}")
    private String imagePath;

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    //create
    @PostMapping("/create")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto){
        UserDto user = userService.createUser(userDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    //update
    @PutMapping("/update/{userId}")
    public ResponseEntity<UserDto> updateUser(@Valid @RequestBody UserDto userDto,
                                              @PathVariable("userId") String id){
        UserDto updatedUserDto = userService.updateUser(userDto,id);
        return new ResponseEntity<>(updatedUserDto, HttpStatus.OK);
    }
    //delete
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<ApiResponseMessages> deleteUser(@PathVariable String userId) throws IOException {
        userService.deleteUser(userId);
        ApiResponseMessages responseMessages = ApiResponseMessages.builder()
                .message("Given id has been deleted")
                .success(true)
                .status(HttpStatus.OK)
                .build();
        return new ResponseEntity<>(responseMessages,HttpStatus.OK);
    }
    //get all
    @GetMapping("/getAll")
    public ResponseEntity<PageableResponse<UserDto>> getAllUser(
            @RequestParam(value = "pageNumber",defaultValue = "0",required = false) int pageNumber,
            @RequestParam(value = "pageSize",defaultValue = "10",required = false) int pageSize,
            @RequestParam(value = "sortBy",defaultValue = "name",required = false) String sortBy,
            @RequestParam(value = "sortDir",defaultValue = "asc",required = false) String sortDir
    ){
        return new ResponseEntity<>(userService.getAllUser(pageNumber,pageSize,sortBy,sortDir),HttpStatus.OK);
    }
    //get single
    @GetMapping("/id/{userId}")
    public ResponseEntity<UserDto> getUser(@PathVariable String userId){
        UserDto userById = userService.getUserById(userId);
        return new ResponseEntity<>(userById, HttpStatus.OK);
    }
    //get by email
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email){
        UserDto userByEmail = userService.getUserByEmail(email);
        return new ResponseEntity<>(userByEmail, HttpStatus.OK);
    }
    //get by keyword
    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<UserDto>> getUserByKeyword(@PathVariable String keyword){
        List<UserDto> userByKeyword = userService.searchUser(keyword);
        return new ResponseEntity<>(userByKeyword, HttpStatus.OK);
    }


    //upload file
    @PostMapping("/image/{userId}")
    public ResponseEntity<ImageResponse> uploadImage(
            @RequestParam("userImage") MultipartFile image, @PathVariable("userId") String userId)
            throws IOException {
        String imageName = fileService.uploadFile(image, imagePath);
        UserDto userById = userService.getUserById(userId);
        userById.setImageName(imageName);
        userService.updateUser(userById,userId);
        ImageResponse imageResponse = ImageResponse.builder()
                .fileName(imageName)
                .status(HttpStatus.CREATED)
                .success(true)
                .build();
        return new ResponseEntity<>(imageResponse,HttpStatus.CREATED);

    }

    //serve file
    @GetMapping("/image/{userId}")
    public void serveUserImage(@PathVariable("userId") String userId, HttpServletResponse response)
            throws IOException {

        UserDto userById = userService.getUserById(userId);
        logger.info("User Image name is {}", userById.getImageName());

        InputStream resource = fileService.getResource(imagePath, userById.getImageName());

        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resource,response.getOutputStream());

    }

}
