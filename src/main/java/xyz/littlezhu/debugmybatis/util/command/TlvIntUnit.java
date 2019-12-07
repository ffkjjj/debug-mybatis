package xyz.littlezhu.debugmybatis.util.command;

public class TlvIntUnit implements ITLVUnit {

	private int tag;
	private int length ;
	private int value;

	public TlvIntUnit(int tag, int value, int length) {
		super();
		this.tag = tag;
		this.length = length;
		this.value = value;
	}

	@Override
	public int getTotalLength() {
		return length + TlvWrap.TAG_LENGTH_LENGTH;
	}

	@Override
	public byte[] getByte() {
		byte[] content = new byte[length + TlvWrap.TAG_LENGTH_LENGTH];
		TlvWrap.writeTag(content, tag);
		TlvWrap.writeLength(content, length);
		TlvWrap.writeIntValue(content, value, length);
		return content;
	}

	@Override
	public int getTag()
	{
		return tag;
	}
}
