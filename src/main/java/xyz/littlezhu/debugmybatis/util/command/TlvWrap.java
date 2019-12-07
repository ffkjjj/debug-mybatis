package xyz.littlezhu.debugmybatis.util.command;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TlvWrap {
    public static final int TAG_LENGTH_LENGTH = 4;

    public static byte[][] splitTag(byte content[], int index) {
        if (content == null) {
            return null;
        }
        List<byte[]> lst = new ArrayList<>();

        for (; index < content.length; ) {
            int l = getLength(content, index);

            byte[] b = new byte[TAG_LENGTH_LENGTH + l];
            System.arraycopy(content, index, b, 0, b.length);
            lst.add(b);

            index += 4;
            index += l;
        }
        return lst.toArray(new byte[0][]);
    }

    public static byte[] readTag(byte content[], int tag, int index) {
        if (content == null) {
            return null;
        }
        for (; index < content.length; ) {
            int t = getTag(content, index);
            if (t == tag) {
                return getValue(content, index);
            }
            int l = getLength(content, index);
            index += 4;
            index += l;
        }
        return null;
    }

    public static int readInt(byte content[], int tag, int index, int defaultvalue) {
        Integer i = readInteter(content, tag, index);
        if (i != null) {
            return i;
        }
        return defaultvalue;
    }

    public static int readInt(byte content[], int tag, int index) {
        byte[] b = readTag(content, tag, index);
        if (b == null) {
            return Integer.MIN_VALUE;
        }
        return readint(b);
    }

    public static Integer readInteter(byte content[], int tag, int index) {
        byte[] b = readTag(content, tag, index);
        if (b == null) {
            return null;
        }
        return readint(b);
    }

    public static String readString(byte content[], int tag, int index, String defaultvalue) {
        byte[] b = readTag(content, tag, index);
        if (b == null) {
            return defaultvalue;
        }
        return new String(b);
    }

    public static String readString(byte content[], int tag, int index) {
        byte[] b = readTag(content, tag, index);
        if (b == null) {
            return "";
        }
        return new String(b);
    }

    public static Date readTime(byte content[], int tag, int index) {
        int t = TlvWrap.readInt(content, tag, index);
        if (t == Integer.MIN_VALUE || t < 365 * 24 * 3600) {
            return new Date();
        }
        return parseTime(t);
    }

    public static int getTag(byte content[], int index) {
        if (content == null || content.length < index + 2) {
            return 0;
        }
        return byte2int(content[index]) * 256 + byte2int(content[index + 1]);
    }

    public static int getLength(byte content[], int index) {
        if (content == null || content.length < index + 4) {
            return 0;
        }
        return byte2int(content[index + 2]) * 256 + byte2int(content[index + 3]);
    }

    public static byte[] getValue(byte content[], int index) {
        int l = getLength(content, index);
        if (l == 0) {
            return new byte[0];
        }
        byte b[] = new byte[l];
        System.arraycopy(content, index + 4, b, 0, l);
        return b;
    }

    public static void writeTag(byte[] content, int tag) {
        writeInt(content, 0, tag, 2);
    }

    public static void writeLength(byte[] content, int length) {
        writeInt(content, 2, length, 2);
    }

    public static void writeIntValue(byte[] content, int value, int length) {
        writeInt(content, TAG_LENGTH_LENGTH, value, length);
    }

    public static void writeInt(byte[] content, int index, int value, int length) {
        for (int i = length - 1; i >= 0; i--) {
            content[index + i] = (byte) (value % 256);
            value /= 256;
        }
    }

    public static void writeByteArrayValue(byte[] content, byte[] value) {
        System.arraycopy(value, 0, content, TAG_LENGTH_LENGTH, value.length);
    }

    private static int byte2int(byte b) {
        return b & 0xff;
    }

    public static int readint(byte[] b, int beginindex, int endindex) {
        int value = 0;
        for (int i = 0; i < b.length && beginindex + i < endindex; i++) {
            value *= 256;
            value += b[beginindex + i] & 0xff;
        }
        return value;
    }

    public static int readint(byte[] b) {
        return readint(b, 0, b.length);
    }

    public static Date parseTime(int second) {
        Calendar d = Calendar.getInstance();
        d.setTimeInMillis(second * 1000L);
        return d.getTime();
    }
}
