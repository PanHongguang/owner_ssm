package com.cross.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cross.lucene.LuceneIndex;
import com.cross.pojo.User;
import com.cross.service.UserService;
import com.cross.util.JsonUtil;
import com.cross.util.PageEntity;
import com.cross.util.PageUtil;

@Controller
@RequestMapping(value="/")
public class IndexController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value="/index")
	public String index(@RequestParam(value="page", required=false, defaultValue="1") int page,
			HttpServletRequest request, Model model) {
		PageEntity pageEntity = new PageEntity(page, 10);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("start", pageEntity.getStart());
		map.put("size", pageEntity.getPageSize());
		
		List<User> list = userService.list(map);
		model.addAttribute("users", list);
		
		String targetUrl = request.getContextPath() + "/index";
		Long totalNum = userService.getTotal(map);
		Integer currentPage = page;
		Integer pageSize = pageEntity.getPageSize();
		String param = new StringBuffer().toString();
		String pageHtml = PageUtil.genPagination(targetUrl, totalNum, currentPage, pageSize, param);
		
		model.addAttribute("pageHtml", pageHtml);
		
		return "index";
	}
	
	@RequestMapping(value="/createLuceneIndex")
	public void createLuceneIndex(HttpServletResponse response) {
		JSONObject jsonObject = new JSONObject();
		
		Map<String, Object> map = new HashMap<String, Object>();
		List<User> userList = userService.list(map);
		
		// 1. 删除索引
		for (User user : userList) {
			LuceneIndex luceneIndex = new LuceneIndex();
			try {
				luceneIndex.deleteIndex(String.valueOf(user.getUserId()));
			} catch (IOException e) {
				e.printStackTrace();
				jsonObject = JsonUtil.parseJson("2", "操作异常", "");
			}
		}
		// 2. 创建索引
		for (User user : userList) {
			LuceneIndex luceneIndex = new LuceneIndex();
			try {
				luceneIndex.addIndex(user);
			} catch (IOException e) {
				e.printStackTrace();
				jsonObject = JsonUtil.parseJson("2", "操作异常", "");
			}
		}
		JSONObject jo = new JSONObject();
		jsonObject = JsonUtil.parseJson("1", "操作成功", jo);
		
		JsonUtil.responseBuildJson(response, jsonObject);
	}
	
	@RequestMapping(value="/luceneQuery")
	public String luceneQuery(@RequestParam(value="q", required=false, defaultValue="") String q,
							  @RequestParam(value="page", required=false, defaultValue="1") String page,
							  HttpServletRequest request, Model model) throws IOException, ParseException, InvalidTokenOffsetsException{
		LuceneIndex luceneIndex = new LuceneIndex();
		List<User> userList = luceneIndex.searchBlog(q);
		
		Integer pg = Integer.parseInt(page);
		Integer toIndex = userList.size() >=  pg * 5 ? pg * 5 : userList.size();
	    List<User> newList = userList.subList((pg - 1) * 5, toIndex);
	    
	    model.addAttribute("userList",newList) ;
	    
	    String s = this.lucenePage(pg, userList.size(), q, 5, request.getServletContext().getContextPath());
	    
	    model.addAttribute("pageHtml",s) ;
        model.addAttribute("q",q) ;
        model.addAttribute("resultTotal",userList.size()) ;
        model.addAttribute("pageTitle","搜索关键字'" + q + "'结果页面") ;
        
		return "queryResult";
	}
	
	/**
     * 查询之后的分页
     * @param page
     * @param totalNum
     * @param q
     * @param pageSize
     * @param projectContext
     * @return
     */
	private String lucenePage(int page, Integer totalNum, String q, Integer pageSize, String projectContext) {
		long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
		StringBuffer pageCode = new StringBuffer();
		if (totalPage == 0) {
			return "";
		} else {
			pageCode.append("<nav>");
			pageCode.append("<ul class='pager' >");
			if (page > 1) {
				pageCode.append("<li><a href='" + projectContext + "/luceneQuery?page=" + (page - 1) + "&q=" + q + "'>上一页</a></li>");
			} else {
				pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
			}
			if (page < totalPage) {
				pageCode.append("<li><a href='" + projectContext + "/luceneQuery?page=" + (page + 1) + "&q=" + q + "'>下一页</a></li>");
			} else {
				pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
			}
			pageCode.append("</ul>");
			pageCode.append("</nav>");
		}
		return pageCode.toString();
	}

}
