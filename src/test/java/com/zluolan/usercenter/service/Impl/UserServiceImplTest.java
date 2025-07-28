package com.zluolan.usercenter.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zluolan.usercenter.mapper.UserMapper;
import com.zluolan.usercenter.model.domain.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UserServiceImpl 单元测试类
 */
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // 使用反射设置 baseMapper
        try {
            java.lang.reflect.Field field = ServiceImpl.class.getDeclaredField("baseMapper");
            field.setAccessible(true);
            field.set(userService, userMapper);
        } catch (Exception e) {
            throw new RuntimeException("Failed to set baseMapper via reflection", e);
        }
    }

    /**
     * 测试用例：星球编号长度超过5
     * 预期结果：返回 -1
     */
    @Test
    void userRegister_withPlanetCodeTooLong_shouldReturnMinusOne() {
        // Act
        long result = userService.userRegister("tom123", "password123", "password123", "123456");

        // Assert
        assertEquals(-1, result);
    }

    /**
     * 测试用例：密码与确认密码不一致
     * 预期结果：返回 -1
     */
    @Test
    void userRegister_withPasswordNotMatchCheckPassword_shouldReturnMinusOne() {
        // Act
        long result = userService.userRegister("tom123", "password123", "different", "planet");

        // Assert
        assertEquals(-1, result);
    }

    /**
     * 测试用例：账户已存在
     * 预期结果：返回 -1
     */
    @Test
    void userRegister_withExistingUserAccount_shouldReturnMinusOne() {
        // Arrange
        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // Act
        long result = userService.userRegister("tom123", "password123", "password123", "planet");

        // Assert
        assertEquals(-1, result);
    }

    /**
     * 测试用例：星球编号已存在
     * 预期结果：返回 -1
     */
    @Test
    void userRegister_withExistingPlanetCode_shouldReturnMinusOne() {
        // Arrange
        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);

        // Act
        long result = userService.userRegister("tom123", "password123", "password123", "planet");

        // Assert
        assertEquals(-1, result);
    }

    /**
     * 测试用例：插入失败
     * 预期结果：返回 -1
     */
    @Test
    void userRegister_withInsertFailure_shouldReturnMinusOne() {
        // Arrange
        when(userMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(0L);
        when(userMapper.insert(any(User.class))).thenReturn(0);

        // Act
        long result = userService.userRegister("tom123", "password123", "password123", "planet");

        // Assert
        assertEquals(-1, result);
    }

    /**
     * 测试用例：正常注册
     * 预期结果：返回用户 ID（如 1）
     */
    @Test
    void userRegister_withValidParams_shouldReturnUserId() {
        // Arrange
        // 使用 any() 匹配所有查询，根据调用顺序返回不同的值
        when(userMapper.selectCount(any(LambdaQueryWrapper.class)))
                .thenReturn(0L) // 第一次调用返回0 (检查用户名)
                .thenReturn(0L); // 第二次调用返回0 (检查星球编号)

        // 模拟 insert 行为并设置 ID
        when(userMapper.insert(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(12); // 设置为期望的ID值
            return 1; // 表示插入成功
        });

        // Act
        long result = userService.userRegister("tom123", "password123", "password123", "plaet");

        // Assert
        assertEquals(12, result);
    }





}
