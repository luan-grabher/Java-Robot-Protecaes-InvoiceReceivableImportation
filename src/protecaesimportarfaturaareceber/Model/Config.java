package protecaesimportarfaturaareceber.Model;

import java.io.File;

import org.ini4j.Ini;

public class Config {
    private static String configFilePath = "./robot-protecaes-invoice-receivable-importation.ini";
    public static final Ini config = loadConfig();

    public static Ini loadConfig() {
        File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            throw new Error("Não foi possível carregar o arquivo de configuração " + configFilePath
                    + ". Verifique se o arquivo existe.");
        }

        try {
            return new Ini(configFile);
        } catch (Exception e) {
            throw new Error("Não foi possível carregar o arquivo de configuração " + configFilePath
                    + ". Verifique se o arquivo está no formato correto.");
        }
    }
}
