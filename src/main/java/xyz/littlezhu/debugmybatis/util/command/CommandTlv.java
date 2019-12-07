package xyz.littlezhu.debugmybatis.util.command;


public class CommandTlv extends TlvArrayUnit {

    public CommandTlv(int commandclass, int command) {
        super(commandclass * 256 + command);
    }

    @Override
    public int getTotalLength() {
        // a last byte for check byte.
        return super.getTotalLength() + 1;
    }

    @Override
    public byte[] getByte() {
        int l = getTotalLength();
        byte[] b = new byte[l];
        int index = TlvWrap.TAG_LENGTH_LENGTH;
        for (ITLVUnit t : lst) {
            byte[] ub = t.getByte();
            int ul = t.getTotalLength();
            System.arraycopy(ub, 0, b, index, ul);
            index += ul;
        }
        TlvWrap.writeTag(b, tag);
        TlvWrap.writeLength(b, l - 1 - TlvWrap.TAG_LENGTH_LENGTH);
        return b;
    }

    public byte[] getByteWithXor() {
        byte[] aByte = getByte();
        aByte[aByte.length - 1] = ByteUtils.xor(aByte);
        return aByte;
    }
}
