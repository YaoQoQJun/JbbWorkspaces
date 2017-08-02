package com.jybb.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jybb.mapper.AdminMapper;
import com.jybb.pojo.Admin;

public class PrivilegeUtil {
	
	public static void checkPriviege(
			HttpSession session,
			HttpServletRequest request,
			HttpServletResponse response,
			String privilege_name,
			String model_name){
		
		List<Map<String,String>> privileges=(List<Map<String, String>>) session.getAttribute("privileges");
		
		for (int i = 0; i < privileges.size(); i++) {
			Map<String,String> map=privileges.get(i);
			if(privilege_name.equals(map.get("privilege_name"))&&model_name.equals(map.get("model_name"))){
				return;
			}
		}
		
		try {
			response.sendRedirect(request.getContextPath()+"/index.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Map<String,String>> getPrivileges(HttpSession session,
			AdminMapper adminMapper) {
		Admin admin=(Admin)session.getAttribute("admin");
		List<Map<String,String>> privileges=adminMapper.getPrivileges(admin.getId());
		return privileges;
	}

}
