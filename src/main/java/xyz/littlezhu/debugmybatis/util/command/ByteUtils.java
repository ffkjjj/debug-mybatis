package xyz.littlezhu.debugmybatis.util.command;

public class ByteUtils {
    //加校验位
    public static byte xor(byte[] frame) {
        byte xorResult = frame[0];
        int len = frame.length - 1;
        for (int i = 1; i < len; i++) {
            xorResult ^= frame[i];
        }
        return xorResult;
    }

    //检查校验位
    public static boolean checkXor(byte[] data) {
        int len = data.length;
        byte xorResult = data[0];
        for (int i = 1; i < len - 1; i++) {
            xorResult ^= data[i];
        }
        return data[len - 1] == xorResult;
    }

    // 通过TEA算法加密信息
    public static byte[] encryptByTea(byte[] content, byte[] key, int times) {
        // 若temp的位数不足8的倍数,需要填充的位数
        int n = 8 - content.length % 8;
        byte[] encryptStr = new byte[content.length + n];
        System.arraycopy(content, 0, encryptStr, 0, content.length);
        fillChar(encryptStr, (byte) n, content.length, n);
        byte[] result = new byte[encryptStr.length];
        for (int offset = 0; offset < result.length; offset += 8) {
            byte[] tempEncrypt = encrypt(encryptStr, offset, key, times);
            System.arraycopy(tempEncrypt, 0, result, offset, 8);
        }
        return result;
    }

    // 通过TEA算法解密信息
    public static byte[] decryptByTea(byte[] secretInfo, byte[] key, int times) {
        byte[] tempDecrypt = new byte[secretInfo.length];
        for (int offset = 0; offset < secretInfo.length; offset += 8) {
            byte[] decryptStr = decrypt(secretInfo, offset, key, times);
            System.arraycopy(decryptStr, 0, tempDecrypt, offset, 8);
        }
        return tempDecrypt;
    }

    private static void fillChar(byte[] content, byte byteChar, int offset, int length) {
        for (int i = 0; i < length; i++) {
            content[offset + i] = byteChar;
        }
    }

    /**
     * tea加密
     */
    private static byte[] encrypt(byte[] content, int offset, byte[] key0, int times) {// times为加密轮数
        int[] tempInt = byteToInt(content, offset);
        int y = tempInt[0], z = tempInt[1], sum = 0, i;
        // 这是算法标准给的值
        int delta = 0x9e3779b9;
        int[] key = byteToInt(key0, 0);
        int a = key[0], b = key[1], c = key[2], d = key[3];
        for (i = 0; i < times; i++) {
            sum += delta;
            y += ((z << 4) + a) ^ (z + sum) ^ ((z >>> 5) + b);
            z += ((y << 4) + c) ^ (y + sum) ^ ((y >>> 5) + d);
        }
        tempInt[0] = y;
        tempInt[1] = z;
        return intToByte(tempInt, 0);
    }

    /**
     * tea解密
     */
    private static byte[] decrypt(byte[] encryptContent, int offset, byte[] key0, int times) {
        int[] tempInt = byteToInt(encryptContent, offset);
        int y = tempInt[0], z = tempInt[1], sum, i;
        // 这是算法标准给的值
        int delta = 0x9e3779b9;
        int[] key = byteToInt(key0, 0);
        int a = key[0], b = key[1], c = key[2], d = key[3];
        sum = delta << (int) (Math.log(times) / Math.log(2));
        for (i = 0; i < times; i++) {
            z -= ((y << 4) + c) ^ (y + sum) ^ ((y >>> 5) + d);
            y -= ((z << 4) + a) ^ (z + sum) ^ ((z >>> 5) + b);
            sum -= delta;
        }
        tempInt[0] = y;
        tempInt[1] = z;

        return intToByte(tempInt, 0);
    }

    // byte[]型数据转成int[]型数据, 高字节在前
    private static int[] byteToInt(byte[] content, int offset) {
        // 除以2的n次方 == 右移n位 即
        int[] result = new int[content.length >> 2];
        for (int i = 0, j = offset; j < content.length; i++, j += 4) {
            result[i] = transform(content[j + 3]) | transform(content[j + 2]) << 8 | transform(content[j + 1]) << 16
                    | (int) content[j] << 24;
        }
        return result;

    }

    // int[]型数据转成byte[]型数据, 高字节在前
    private static byte[] intToByte(int[] content, int offset) {
        byte[] result = new byte[content.length << 2];
        for (int i = 0, j = offset; j < result.length; i++, j += 4) {
            result[j + 3] = (byte) (content[i] & 0xff);
            result[j + 2] = (byte) ((content[i] >> 8) & 0xff);
            result[j + 1] = (byte) ((content[i] >> 16) & 0xff);
            result[j] = (byte) ((content[i] >> 24) & 0xff);
        }
        return result;
    }

    // 若某字节被解释成负的则需将其转成无符号正数
    private static int transform(byte temp) {
        int tempInt = (int) temp;
        if (tempInt < 0) {
            tempInt += 256;
        }
        return tempInt;
    }

    public static int byteArrayToInt(byte[] bytes) {
        if (bytes.length == 1) {
            return (int) bytes[0];
        }
        if (bytes.length == 2) {
            return byteArrayToShort(bytes);
        }
        int value = 0;
        //由高位到低位
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;//往高位游
        }
        return value;
    }

    public static short byteArrayToShort(byte[] bytes) {
        if (bytes.length == 1) {
            return (short) bytes[0];
        }
        short value = 0;
        //由高位到低位
        for (int i = 0; i < 2; i++) {
            int shift = (2 - 1 - i) * 8;
            value += (bytes[i] & 0x000000FF) << shift;//往高位游
        }
        return value;
    }

    //十六进制字符串转byte数组
    public static byte[] hexStringtobyteArray(String content) {
        if (content == null || content.length() == 0) {
            return new byte[]{};
        }
        byte[] rst = new byte[content.length() / 2];
        for (int i = 0; i < content.length() - 1; i += 2) {
            rst[i / 2] = Integer.valueOf(content.substring(i, i + 2), 16).byteValue();
        }
        return rst;
    }

    //byte数组转十六进制字符串
    public static String toHexStr(byte[] content) {
        if (content == null || content.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < content.length; i++) {
            if (content[i] >= 0 && content[i] <= 0x0f) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(content[i] & 0xff));
        }
        return sb.toString();
    }

}
