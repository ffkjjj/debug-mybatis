package xyz.littlezhu.debugmybatis.util.bluetooth;

import xyz.littlezhu.debugmybatis.util.command.ByteUtils;
import xyz.littlezhu.debugmybatis.util.command.CommandTlv;
import xyz.littlezhu.debugmybatis.util.command.TlvByteUnit;

/**
 * @author zhul
 * @date 2019/12/7 10:36
 */
public class CommandUtils {
    /**
     * 获取随机串
     * @param rangeCode 随机数，在加密前附加到内容中，提高加密数据的安全性。
     */
    public static CommandTlv getRangeCode(byte[] rangeCode) {
        CommandTlv ct = new CommandTlv(31, 3);
        ct.addUnit(new TlvByteUnit(25, rangeCode));
        return ct;
    }

    /**
     * 开蓝牙门锁(V2)
     *
     * @param codePackage 开锁包
     * @param rangeString 获取到的随机串
     * @param rangeCode 随机数
     * @param autoLock 是否自动回锁
     */
    public static CommandTlv openLock(byte[] codePackage, byte[] rangeString, byte[] rangeCode, byte[] autoLock) {
        CommandTlv ct = new CommandTlv(31, 7);
        ct.addUnit(new TlvByteUnit(101, codePackage));
        ct.addUnit(new TlvByteUnit(100, rangeString));
        ct.addUnit(new TlvByteUnit(25, rangeCode));
        ct.addUnit(new TlvByteUnit(66, autoLock));
        return ct;
    }

    /**
     * 蓝牙关锁
     *
     * @param rangeString 获取到的随机串
     * @param rangeCode 随机数
     */
    public static CommandTlv closeLock(byte[] rangeString, byte[] rangeCode) {
        CommandTlv ct = new CommandTlv(31, 9);
        ct.addUnit(new TlvByteUnit(100, rangeString));
        ct.addUnit(new TlvByteUnit(25, rangeCode));
        return ct;
    }

    /**
     * 将要下发的命令使用通讯包加密
     * @param key3 密钥3
     * @param commandTlv 要下发的命令包
     */
    public static byte[] getCommunicationPackage(byte[] key3, CommandTlv commandTlv) {
        byte[] value = ByteUtils.encryptByTea(commandTlv.getByteWithXor(), key3, 16);
        return wrapCommunicationTag(value).getByteWithXor();
    }

    /**
     * 将要下发的命令使用通讯包加密
     * @param key3 密钥3
     * @param commandData 要下发的命令包的byte数组
     */
    public static byte[] getCommunicationPackage(byte[] key3, byte[] commandData) {
        commandData[commandData.length - 1] = ByteUtils.xor(commandData);
        byte[] value = ByteUtils.encryptByTea(commandData, key3, 16);
        return wrapCommunicationTag(value).getByteWithXor();
    }

    private static CommandTlv wrapCommunicationTag(byte[] value) {
        CommandTlv ct = new CommandTlv(106, 1);
        ct.addUnit(new TlvByteUnit(29, value));
        return ct;
    }

    /**
     * 解析设备上报的 report
     * @param report 设备上报的 report
     * @param key3 密钥3
     */
    public static CommandResultVO parseReport(byte[] report, byte[] key3) {
        return BlueToothCommandParse.parseBlueToothResult(report, key3);
    }
}
