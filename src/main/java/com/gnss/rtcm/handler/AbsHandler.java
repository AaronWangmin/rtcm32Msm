package com.gnss.rtcm.handler;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbsHandler extends IoHandlerAdapter{

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		super.sessionClosed(session);
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
		log.warn("No message received!");
		super.sessionIdle(session, status);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		log.error(cause.getMessage());
	}
	

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		byte[] rawBytes = (byte[])message;
		if(rawBytes != null) {
			handle(session,rawBytes);
		}
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		super.messageSent(session, message);
	}
	
	public abstract void handle(IoSession session, byte[] rawBytes);
	

}
