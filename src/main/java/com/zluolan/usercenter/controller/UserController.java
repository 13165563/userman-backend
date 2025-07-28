package com.zluolan.usercenter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zluolan.usercenter.common.BaseResponse;
import com.zluolan.usercenter.common.ErrorCode;
import com.zluolan.usercenter.common.ResultUtils;
import com.zluolan.usercenter.model.domain.User;
import com.zluolan.usercenter.model.request.DeleteRequest;
import com.zluolan.usercenter.model.request.UserLoginRequest;
import com.zluolan.usercenter.model.request.UserRegisterRequest;
import com.zluolan.usercenter.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

import static com.zluolan.usercenter.constant.UserConstant.ADMIN_ROLE;
import static com.zluolan.usercenter.constant.UserConstant.USER_LOGIN_STATE;

/**
 * 用户接口（统一响应封装版）
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        // 参数校验
        if (userRegisterRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        String planetCode = userRegisterRequest.getPlanetCode();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword, planetCode)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "参数为空");
        }

        // 业务调用
        long result = userService.userRegister(userAccount, userPassword, checkPassword, planetCode);
        log.info("用户注册成功，账号：{}", userAccount);
        return ResultUtils.success(result);
    }

    @PostMapping("/login")
    public BaseResponse<User> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "账号或密码为空");
        }

        User user = userService.userLogin(userAccount, userPassword, request);
        log.info("用户登录成功，账号：{}", userAccount);
        return ResultUtils.success(userService.getSafetyUser(user));
    }

    @GetMapping("/current")
    public BaseResponse<User> getCurrentUser(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        User currentUser = (User) userObj;
        User user = userService.getById(currentUser.getId());
        if (user == null) {
            request.getSession().removeAttribute(USER_LOGIN_STATE); // 清除无效 session
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        return ResultUtils.success(userService.getSafetyUser(user));
    }

    @GetMapping("/search")
    public BaseResponse<List<User>> searchUser(String username, HttpServletRequest request) {
        // 检查是否为管理员
        BaseResponse<Boolean> adminCheck = checkAdmin(request);
        if (adminCheck.getCode() != 0) {
            return ResultUtils.error(adminCheck.getCode(), adminCheck.getMessage(), adminCheck.getDescription());
        }

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(username)) {
            queryWrapper.like(User::getUsername, username);
        }
        List<User> userList = userService.list(queryWrapper)
                .stream()
                .map(user -> userService.getSafetyUser(user))
                .collect(Collectors.toList());
        return ResultUtils.success(userList);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "ID不合法");
        }
        // 检查是否为管理员
        BaseResponse<Boolean> adminCheck = checkAdmin(request);
        if (adminCheck.getCode() != 0) {
            return ResultUtils.error(adminCheck.getCode(), adminCheck.getMessage(), adminCheck.getDescription());
        }
        boolean result = userService.removeById(deleteRequest.getId());
        log.info("删除用户，ID：{}", deleteRequest.getId());
        return ResultUtils.success(result);
    }

    @PostMapping("/logout")
    public BaseResponse<Integer> logout(HttpServletRequest request) {
        if (request.getSession() == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR);
        }
        request.getSession().invalidate();
        return ResultUtils.success(1);
    }

    /**
     * 判断是否为管理员
     */
    private BaseResponse<Boolean> checkAdmin(HttpServletRequest request) {
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        if (userObj == null) {
            return ResultUtils.error(ErrorCode.NOT_LOGIN_ERROR, "未登录");
        }
        User user = (User) userObj;
        if (user.getRole() != ADMIN_ROLE) {
            return ResultUtils.error(ErrorCode.NO_AUTH_ERROR);
        }
        return ResultUtils.success(true);
    }
}