package xyz.littlezhu.debugmybatis.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.littlezhu.debugmybatis.service.ICompanyService;
import xyz.littlezhu.debugmybatis.util.Result;

/**
 * @author zhul
 * @date 2019/11/30 10:49
 */
@RestController
@RequestMapping("/company")
public class CompanyController {

    private final ICompanyService companyService;

    public CompanyController(ICompanyService companyService) {
        this.companyService = companyService;
    }

    @RequestMapping("/insert")
    public Result<?> insert() {
        companyService.insert();
        return Result.success();
    }

    @RequestMapping("/count")
    @ResponseBody
    public Result<Integer> count() {
        return Result.success(companyService.count());
    }
}
