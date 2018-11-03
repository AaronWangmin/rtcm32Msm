package com.gnss.rtcm.codec;

import java.nio.charset.Charset;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class ByteArrayEncoder extends ProtocolEncoderAdapter {
	@Override
	public void encode(IoSession session, Object msg, ProtocolEncoderOutput out)	throws Exception {
        if(msg instanceof IoBuffer){ out.write(msg);return; }
        IoBuffer ioBuffer = IoBuffer.allocate(1024).setAutoExpand(true);
		if (msg instanceof String) ioBuffer.putString((String)msg,Charset.forName("UTF-8").newEncoder());
		if (msg instanceof byte[]) ioBuffer.put((byte[]) msg);
		ioBuffer.flip();
		out.write(ioBuffer);
		ioBuffer = null;
	}
}
