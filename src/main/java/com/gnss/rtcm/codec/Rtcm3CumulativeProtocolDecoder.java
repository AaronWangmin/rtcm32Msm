package com.gnss.rtcm.codec;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.gnss.rtcm.common.util.BitUtil;
import com.gnss.rtcm.common.util.CRC24;

public class Rtcm3CumulativeProtocolDecoder extends AbsCumulativeProtocolDecoder {

	@Override
	public boolean handle(IoSession session, IoBuffer in, ProtocolDecoderOutput out) {
		int start = in.position();
		while (in.hasRemaining()) {
			byte first = in.get();
			if (first == (byte) 211 && in.remaining() > 2) {
				int msgStart = in.position() - 1;
				byte[] legnthArray = new byte[2];
				in.get(legnthArray);
				int legnth = (int) BitUtil.bytesDecodeR(legnthArray, 0, 16);
				if ((legnth > 10 && legnth < 1000) && in.remaining() >= legnth + 3) {
					byte[] target = new byte[legnth];
					byte[] crc = new byte[3];
					in.get(target);
					in.get(crc);
					if (arrayEqual(crc, CRC24.crc24gen(target, legnthArray))) {
						in.position(msgStart);
						byte[] bs = new byte[6 + legnth];
						in.get(bs);
						out.write(bs);
						if (in.remaining() > 0) {
							return true;
						} else {
							return false;
						}
					} else
						in.position(in.position() - legnth - 3);
				} else
					in.position(in.position() - 2);
			}
		}
		in.position(start);
		return false;
	}
	
	private static boolean arrayEqual(byte[] crc, byte[] check) {
		return crc[0] == check[0] && crc[1] == check[1] && crc[2] == check[2];
	}

}
