package com.gnss.rtcm.connector;

import java.net.InetSocketAddress;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.gnss.rtcm.codec.ByteArrayEncoder;
import com.gnss.rtcm.codec.Rtcm3CumulativeProtocolDecoder;
import com.gnss.rtcm.handler.Rtcm32Handler;
import com.gnss.rtcm.handler.Rtcm32Handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TcpClient {

	public void build() {
		NioSocketConnector connector = new NioSocketConnector();

		// log.info("TcpClientConnector..............................");

		// TODO 需要更改为动态创建
		connector.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(new ByteArrayEncoder(), new Rtcm3CumulativeProtocolDecoder()));
		connector.getFilterChain().addLast("logger", new LoggingFilter());

		connector.setHandler(new Rtcm32Handler());

		connector.setConnectTimeoutMillis(10000);
		connector.setConnectTimeoutCheckInterval(200);

		SocketSessionConfig sessionConfig = connector.getSessionConfig();
		sessionConfig.setSendBufferSize(1024 * 1024);
		sessionConfig.setReadBufferSize(1024 * 1024);
		sessionConfig.setMinReadBufferSize(65536);
		sessionConfig.setMaxReadBufferSize(1024 * 1024);
		sessionConfig.setReceiveBufferSize(1024 * 1024);
		sessionConfig.setReuseAddress(true);
		sessionConfig.setTcpNoDelay(true);
		sessionConfig.setKeepAlive(true);
		sessionConfig.setReaderIdleTime(1000); // 需要自定义

		try {
			InetSocketAddress socketAddress = new InetSocketAddress("192.168.5.17", 9903);
			ConnectFuture connectFuture = connector.connect(socketAddress);
			log.info(socketAddress.toString());
			connectFuture.awaitUninterruptibly();

		} catch (RuntimeIoException e) {
			if (log.isErrorEnabled()) {
				log.error("Fail to add the station, code: {}");
				throw e;
			}
		}
	}

}
