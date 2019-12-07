package xyz.littlezhu.debugmybatis.service.impl;

import org.springframework.stereotype.Service;
import xyz.littlezhu.debugmybatis.data.dao.CompanyDao;
import xyz.littlezhu.debugmybatis.data.domain.Company;
import xyz.littlezhu.debugmybatis.service.ICompanyService;

/**
 * @author zhul
 * @date 2019/11/30 10:50
 */
@Service
public class CompanyServiceImpl implements ICompanyService {
    private final CompanyDao companyDao;

    public CompanyServiceImpl(CompanyDao companyDao) {
        this.companyDao = companyDao;
    }

    @Override
    public void insert() {
        Company company = new Company();
        company.setName("jwzh");
        company.setAddress("gdsz");
        company.setAge(25);
        company.setSalary(1000.2f);
        companyDao.insert(company);
    }

    @Override
    public int count() {
        return companyDao.count();
    }
}
