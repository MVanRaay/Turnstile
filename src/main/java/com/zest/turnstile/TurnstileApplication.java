package com.zest.turnstile;

import com.zest.turnstile.qr.QrHandler;
import com.zest.turnstile.scanner.TurnstileClient;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TurnstileApplication {

	private final TurnstileClient client;
	private final QrHandler handler;

	public TurnstileApplication(TurnstileClient client, QrHandler handler) {
		this.client = client;
		this.handler = handler;
	}

	@PostConstruct
	public void init() {
		client.setQrCallback(handler::handleQr);
	}

	public static void main(String[] args) {
		SpringApplication.run(TurnstileApplication.class, args);
	}

}
