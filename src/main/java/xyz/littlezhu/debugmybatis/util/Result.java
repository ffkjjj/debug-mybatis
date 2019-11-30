package xyz.littlezhu.debugmybatis.util;

/**
 * @author zhul
 * @date 2019/11/30 10:57
 */
public class Result<T> {
    private int code;
    private String msg = SUCCESS;
    private T data;

    private static final String SUCCESS = "success";
    private static final String FAILED = "failed";

    public static Result<String> success() {
        return new Result<>();
    }
}
