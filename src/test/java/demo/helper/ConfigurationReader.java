package demo.helper;

import demo.helper.factory.LoggerFactory;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.util.Properties;

public class ConfigurationReader {


        private static final Logger logger = LoggerFactory.getLogger(ConfigurationReader.class);
        private static final String CONFIG_PATH;
        private static Properties properties;

        public static String env;

        static {
            CONFIG_PATH = "config/config.properties";

            try {
                FileInputStream input = new FileInputStream(CONFIG_PATH);
                logger.info("We are using as property: " + CONFIG_PATH);
                properties = new Properties();
                properties.load(input);

                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public static String get(String keyName) {
            return properties.getProperty(keyName);
        }
}
