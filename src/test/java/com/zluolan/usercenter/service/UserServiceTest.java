package com.zluolan.usercenter.service;

import com.zluolan.usercenter.model.domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zluolan
 * @version 1.0
 * @description: TODO
 * @date 2024/1/28 16:52
 */
@SpringBootTest
class UserServiceTest {
    @Autowired
    private UserService userService;
    @Test
    void testAdduser() {
        User user = new User();

        user.setUsername("zluolan");
        user.setUserAccount("123");
        user.setAvatarUrl("https://thirdwx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKhOqyiahbWybPMINia0hJbTDoZbs1ACTb9ldJS99dIE3ckBSjT3z7icVJzXol7BpFoyibESEWqYFagXg/132");
        user.setGender(0);
        user.setUserPassword("xxx");
        user.setPhone("123");
        user.setEmail("566@qq.com");
        boolean result = userService.save(user);
        System.out.println(user.toString());
        Assertions.assertTrue(result);
    }

//    @Test
//    void userRegister() {
//        String userAccount = "zluolan";
//        String userPassword = "123456";
//        String checkPassword = "123456";
//        long result = userService.userRegister(userAccount, userPassword, checkPassword);
//        System.out.println(result);
//        Assertions.assertEquals(-1, result);
//        userAccount = "zluolan";
//        userPassword = "123456789";
//        checkPassword = "123456789";
//        result = userService.userRegister(userAccount, userPassword, checkPassword);
//    }
//



}