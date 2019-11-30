package xyz.littlezhu.debugmybatis.data.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author zhul
 * @date 2019/11/30 10:28
 */
@Data
public class Company implements Serializable {
    private int id;
    private String name;
    private int age;
    private String address;
    private float salary;
}
