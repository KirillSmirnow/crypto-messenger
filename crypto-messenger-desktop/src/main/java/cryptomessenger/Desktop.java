package cryptomessenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class Desktop {

    public static void main(String[] args) {
        SpringApplication.run(Desktop.class);
    }
}
