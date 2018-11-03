package com.gnss.rtcm.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public abstract class AbsCumulativeProtocolDecoder extends CumulativeProtocolDecoder{

	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		return handle(session,in,out);
	}
	
	public abstract boolean handle(IoSession session, IoBuffer in, ProtocolDecoderOutput out);

}
