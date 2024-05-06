package md.fuel.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"md.fuel.bot", "md.telegram.lib"})
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }
}
