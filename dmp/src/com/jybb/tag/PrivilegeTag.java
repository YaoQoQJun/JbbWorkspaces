package com.jybb.tag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;


public class PrivilegeTag implements Tag{
	/**
	 *
	 Tag.SKIP_BODY：表示?>…之间的内容被忽略；
	 Tag.EVAL_BODY_INCLUDE：表示标签之间的内容被正常执行。
	 */
	private List<Map<String,String>> privileges;
	private String privilege;
	
	public int doStartTag() throws JspException {
		
		String[] strs=privilege.split(",");
		for (int i = 0; i < privileges.size(); i++) {
			Map<String,String> map=privileges.get(i);
			if(map.get("privilege_name").equals(strs[0])&&map.get("model_name").equals(strs[1])){
				return Tag.EVAL_BODY_INCLUDE;
			}
		}
		
		return Tag.SKIP_BODY;
	}


	public List<Map<String, String>> getPrivileges() {
		return privileges;
	}
	public void setPrivileges(List<Map<String, String>> privileges) {
		this.privileges = privileges;
	}
	
	public String getPrivilege() {
		return privilege;
	}
	public void setPrivilege(String privilege) {
		this.privilege = privilege;
	}

	public int doEndTag() throws JspException {
		// TODO Auto-generated method stub
		return 0;
	}

	public Tag getParent() {
		// TODO Auto-generated method stub
		return null;
	}

	public void release() {
		// TODO Auto-generated method stub
		
	}

	public void setPageContext(PageContext arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setParent(Tag arg0) {
		// TODO Auto-generated method stub
		
	}

	
}
