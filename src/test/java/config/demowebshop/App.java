package config.demowebshop;

import config.demowebshop.AppConfig;
import org.aeonbits.owner.ConfigFactory;

public class App {
    public static AppConfig config = ConfigFactory.create(AppConfig.class);
}
