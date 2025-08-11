package com.plazoletadecomidas.plazoleta_ms_trazabilidad;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.plazoletadecomidas.plazoleta_ms_trazabilidad.infrastructure.client")
public class PlazoletaMsTrazabilidadApplication {

	public static void main(String[] args) {
		try {
			// Carga variables del archivo .env (ignora si no existe)
			Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

			// --- MongoDB ---
			// Prioriza MONGO_URI del entorno/.env; si no está, no pisa application-*.properties
			if (dotenv.get("MONGO_URI") != null) {
				System.setProperty("spring.data.mongodb.uri", dotenv.get("MONGO_URI"));
			}

			// --- JWT ---
			if (dotenv.get("JWT_SECRET_KEY") != null) {
				System.setProperty("jwt.secret", dotenv.get("JWT_SECRET_KEY"));
			}

			SpringApplication.run(PlazoletaMsTrazabilidadApplication.class, args);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
