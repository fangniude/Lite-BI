package bi.lite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author lewis
 */
@SpringBootApplication
@EnableJpaAuditing
public class LiteBiApplication {

    public static void main(String[] args) {
        SpringApplication.run(LiteBiApplication.class, args);
    }

}
