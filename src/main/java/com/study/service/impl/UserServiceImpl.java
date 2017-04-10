package com.study.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.study.dao.UserDao;
import com.study.model.User;
import com.study.model.UserRole;
import com.study.service.UserService;
@Service
public class UserServiceImpl implements UserService{
	
	@Resource
	private UserDao userDao;
	
	@Override
	public User FindUserWithRolesByName(String name) {
		return userDao.FindUserWithRolesByName(name);
	}

	@Override
	public PageInfo<User> selectByPage(User user,int start, int length) {
		int page = start/length+1;
        //分页查询
        PageHelper.startPage(page, length);
        List<User> userlist = userDao.queryAll(user);
        return new PageInfo<>(userlist);
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED,readOnly=false,rollbackFor={Exception.class})
	public void saveUserRoles(UserRole userRole) {
		userDao.delRolesByUserId(userRole.getUserId());
		String[] roleids = userRole.getRoleId().split(",");
		for (String roleId : roleids) {
			UserRole u = new UserRole();
			u.setUserId(userRole.getUserId());
			u.setRoleId(roleId);
			userDao.addUserRole(u);
		}
	}
	
	public void addUser(User user){
		userDao.addEntity(user);
	}

	@Override
	public void delUser(Integer id) {
		userDao.deleteEntity(id);
	}
	
}
