package cryptomessenger.desktop.infrastructure.client;

import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;

@Component
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    @SneakyThrows
    public Exception decode(String methodKey, Response response) {
        var message = new BufferedReader(response.body().asReader(response.charset())).readLine();
        return new RuntimeException(message);
    }
}
