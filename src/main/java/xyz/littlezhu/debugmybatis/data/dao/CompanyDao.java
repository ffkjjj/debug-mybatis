package xyz.littlezhu.debugmybatis.data.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import xyz.littlezhu.debugmybatis.data.domain.Company;

import java.util.List;

/**
 * @author zhul
 * @date 2019/11/30 10:35
 */
@Repository
@Mapper
public interface CompanyDao {
    void insert(Company company);

    int count();
}
