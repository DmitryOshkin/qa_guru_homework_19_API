package config.selenoid;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/selenoid/credentials.properties")
public interface CredentialsConfig extends Config {

    String user();

    String password();
}
