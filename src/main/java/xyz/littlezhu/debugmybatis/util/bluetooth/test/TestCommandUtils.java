package xyz.littlezhu.debugmybatis.util.bluetooth.test;

import xyz.littlezhu.debugmybatis.util.bluetooth.CommandResultVO;
import xyz.littlezhu.debugmybatis.util.bluetooth.CommandUtils;
import xyz.littlezhu.debugmybatis.util.command.ByteUtils;
import xyz.littlezhu.debugmybatis.util.command.CommandTlv;

/**
 * @author zhul
 * @date 2019/12/7 14:33
 */
public class TestCommandUtils {
    public static void main(String[] args) {
        byte[] key3 = ByteUtils.hexStringtobyteArray("993383EAB92DD1DB4D1591F78A32CCFC");
//        testGetRandCode(key3);
//        testRandReport(key3);
        testOpenLockReport(key3);
    }

    private static void testOpenLockReport(byte[] key3) {
        byte[] report = ByteUtils.hexStringtobyteArray("6a01002c001d0028d1def633bc168340a2aae5d52489fbd014851276cbf607197b6f94ea13cb8f1be2abed74bb481a14b2");
        CommandResultVO vo = CommandUtils.parseReport(report, key3);
        System.out.println(vo.getResultCode());
        System.out.println(vo.getBattery());
    }

    private static void testRandReport(byte[] key3) {
        byte[] report = ByteUtils.hexStringtobyteArray("6a010024001d002094dd735c33e5de57f6da258a7b69a7330ffdf420328c13be599750eba1674eb432");
        CommandResultVO commandResultVO = CommandUtils.parseReport(report, key3);
        System.out.println(ByteUtils.toHexStr(commandResultVO.getRandCode()));
    }

    private static void testGetRandCode(byte[] key3) {
        byte[] randNum = {10, 10, 10, 10};
        CommandTlv randCodeTlv = CommandUtils.getRangeCode(randNum);
        byte[] communicationPackage = CommandUtils.getCommunicationPackage(key3, randCodeTlv);
        // 6a010014001d001072ec8d3810c4dd31dbe157ba11e4ade40a
        System.out.println(ByteUtils.toHexStr(communicationPackage));
    }
}
