package xyz.littlezhu.debugmybatis.util.bluetooth;

import xyz.littlezhu.debugmybatis.util.command.ByteUtils;
import xyz.littlezhu.debugmybatis.util.command.TlvWrap;

/**
 * @author zhul
 * @date 2019/12/7 11:21
 */
class BlueToothCommandParse {
    static CommandResultVO parseBlueToothResult(byte[] report, byte[] key3) {
        if (report == null || report.length < 4 || key3 == null || !ByteUtils.checkXor(report)) {
            return CommandResultVO.fail(-10);
        }
        if (!isEncryptCmdReport(report[0], report[1])) {
            return CommandResultVO.fail(-2);
        }
        byte[] decryptValue;
        try {
            decryptValue = ByteUtils.decryptByTea(TlvWrap.readTag(report, 29, 4), key3, 16);
        } catch (Exception e) {
            return CommandResultVO.fail(-10);
        }
        int requestClass = decryptValue[0] & 0xff;
        int requestSubClass = decryptValue[1] & 0xff;
        if (isRandStrReport(requestClass, requestSubClass)) {
            byte[] randStr = TlvWrap.readTag(decryptValue, 100, 4);
            return CommandResultVO.ofRandCode(randStr);
        } else if (isOpenLockReport(requestClass, requestSubClass)) {
            Integer rst = TlvWrap.readInteter(decryptValue, 1, 4);
            Integer battery = TlvWrap.readInteter(decryptValue, 64, 4);
            return CommandResultVO.ofOpenLockResult(rst != null && rst == 0, battery);
        } else if (isCloseLockReport(requestClass, requestSubClass)) {
            Integer rst = TlvWrap.readInteter(decryptValue, 1, 4);
            Integer battery = TlvWrap.readInteter(decryptValue, 64, 4);
            return CommandResultVO.ofCloseLockResult(rst != null && rst == 0, battery);
        }
        return CommandResultVO.fail(-2);
    }

    private static boolean isRandStrReport(int requestClass, int requestSubClass) {
        return requestClass == 0x1f && requestSubClass == 0x04;
    }

    private static boolean isCloseLockReport(int requestClass, int requestSubClass) {
        return requestClass == 0x1f && requestSubClass == 0x08;
    }

    private static boolean isOpenLockReport(int requestClass, int requestSubClass) {
        return requestClass == 0x1f && requestSubClass == 0x08;
    }

    private static boolean isEncryptCmdReport(int requestClass, int requestSubClass) {
        return requestClass == 0x6a && requestSubClass == 0x01;
    }
}
