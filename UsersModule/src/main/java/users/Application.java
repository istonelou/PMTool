package users;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class Application {

	static final Logger logger = LogManager.getLogger(Application.class.getName());

	public static void main(String[] args) {
		logger.info("Users Module started");
		SpringApplication.run(Application.class, args);
	}
}

@RefreshScope
@RestController
class MessageRestController {

	
	@Value("${password}")
    private String password;
 

    public String whoami(@PathVariable("username") String username) {
        return String.format("Hello! You're %s your password is '%s'!\n", username, password);
    }

}