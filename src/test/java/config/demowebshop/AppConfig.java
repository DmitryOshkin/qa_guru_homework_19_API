package config.demowebshop;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/demowebshop/app.properties")
public interface AppConfig extends Config {

    String webUrl();

    String apiUrl();

    String userLogin();

    String userPassword();
}
