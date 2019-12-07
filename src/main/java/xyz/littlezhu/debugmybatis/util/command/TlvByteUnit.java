package xyz.littlezhu.debugmybatis.util.command;

public class TlvByteUnit implements ITLVUnit {
    private int tag;
    private byte[] value;

    public TlvByteUnit(int tag, byte[] value) {
        super();
        this.tag = tag;
        this.value = value;
    }

    @Override
    public byte[] getByte() {
        byte[] content = new byte[this.getTotalLength()];
        TlvWrap.writeTag(content, tag);
        TlvWrap.writeLength(content, value.length);
        TlvWrap.writeByteArrayValue(content, value);
        return content;
    }

    @Override
    public int getTotalLength() {
        return value.length + TlvWrap.TAG_LENGTH_LENGTH;
    }

    @Override
    public int getTag() {
        return tag;
    }

}
