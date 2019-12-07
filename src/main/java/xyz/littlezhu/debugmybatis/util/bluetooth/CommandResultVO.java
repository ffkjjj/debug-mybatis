package xyz.littlezhu.debugmybatis.util.bluetooth;

/**
 * @author zhul
 * @date 2019/12/7 11:48
 */
public class CommandResultVO {
    /**
     * 非 0 为失败
     * -10 参数错误, 可能是上报的报告数据有问题
     * -1 操作失败, 具体意义根据 type 判断
     * -2 不支持的报告, 即不为 type 中的任意一种类型的报告
     */
    private int resultCode;
    private Integer battery;
    private byte[] randCode;
    /**
     * 1: 随机串
     * 2: 开锁结果
     * 3: 关锁结果
     */
    private int type;

    public CommandResultVO() {
    }

    public CommandResultVO(int resultCode) {
        this.resultCode = resultCode;
    }

    public static CommandResultVO ofRandCode(byte[] randCode) {
        CommandResultVO vo = new CommandResultVO();
        if (randCode == null || randCode.length == 0) {
            vo.resultCode = -1;
        }
        vo.randCode = randCode;
        vo.type = 1;
        return vo;
    }

    public static CommandResultVO ofCloseLockResult(boolean success, Integer battery) {
        CommandResultVO vo = new CommandResultVO();
        vo.setType(3);
        vo.setBattery(battery);
        vo.setResultCode(success ? 0 : -1);
        return vo;
    }

    public static CommandResultVO ofOpenLockResult(boolean success, Integer battery) {
        CommandResultVO vo = new CommandResultVO();
        vo.setType(2);
        vo.setBattery(battery);
        vo.setResultCode(success ? 0 : -1);
        return vo;
    }

    public static CommandResultVO fail(int resultCode) {
        return new CommandResultVO(resultCode);
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Integer getBattery() {
        return battery;
    }

    public void setBattery(Integer battery) {
        this.battery = battery;
    }

    public byte[] getRandCode() {
        return randCode;
    }

    public void setRandCode(byte[] randCode) {
        this.randCode = randCode;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
