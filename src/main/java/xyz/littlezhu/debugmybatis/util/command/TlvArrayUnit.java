package xyz.littlezhu.debugmybatis.util.command;

import java.util.ArrayList;
import java.util.List;

public class TlvArrayUnit implements ITLVUnit {

    protected int tag;
    protected List<ITLVUnit> lst = new ArrayList<>();

    public TlvArrayUnit(int tag) {
        super();
        this.tag = tag;
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
        TlvWrap.writeLength(b, l - TlvWrap.TAG_LENGTH_LENGTH);
        return b;
    }

    @Override
    public int getTotalLength() {
        int l = 0;

        for (ITLVUnit t : lst)
            l += t.getTotalLength();
        return l + TlvWrap.TAG_LENGTH_LENGTH;
    }

    public void addUnit(ITLVUnit tlv) {
        lst.add(tlv);
    }

    public void addOrReplaceUnit(ITLVUnit tlv) {
        ITLVUnit t = null;
        for (ITLVUnit tt : lst) {
            if (tt.getTag() != 0 && tt.getTag() == tlv.getTag()) {
                t = tt;
                break;
            }
        }
        if (t != null) {
            lst.remove(t);
        }
        this.addUnit(tlv);
    }

    @Override
    public int getTag() {
        return tag;
    }
}
