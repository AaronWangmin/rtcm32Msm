package com.gnss;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import com.gnss.rtcm.connector.TcpClient;

@SpringBootApplication
public class SpringbootDdapMain {
	public static void main(String[] args) {
		SpringApplication application = new SpringApplication(SpringbootDdapMain.class);
		application.setWebEnvironment(false);
		application.run(args);
	}
	
	@Component
	public static class NoWebRunner implements CommandLineRunner{

		@Override
		public void run(String... arg0) throws Exception {
			
			TcpClient tcpClient = new TcpClient();
			tcpClient.build();
			
		}
		
	}


}
