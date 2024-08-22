package com.lhc.lhcusercenter.db.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lhc.lhcusercenter.db.UserInfo;
import com.lhc.lhcusercenter.db.service.UserInfoService;
import com.lhc.lhcusercenter.db.mapper.UserInfoMapper;
import org.springframework.stereotype.Service;

/**
* @author 82129
* @description 针对表【user_info】的数据库操作Service实现
* @createDate 2024-08-22 23:40:53
*/
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo>
    implements UserInfoService{

}




