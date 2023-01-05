package com.lt.gulimall.search.web;

import com.lt.gulimall.search.service.MallSearchService;
import com.lt.gulimall.search.vo.SearchParam;
import com.lt.gulimall.search.vo.SearchResult;
import org.apache.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName IndexController
 * @Description:
 * @Author lite
 * @Date 2022/12/31
 * @Version V1.0
 **/
@Controller
public class IndexController {

    @Resource
    private MallSearchService mallSearchService;

    @GetMapping({"/","/list.html"})
    public String indexPage(SearchParam searchParam, Model model, HttpServletRequest request){
        String queryString = request.getQueryString();
        SearchResult searchResult = mallSearchService.search(searchParam,queryString);
        model.addAttribute("result",searchResult);
        return "index";
    }
}
