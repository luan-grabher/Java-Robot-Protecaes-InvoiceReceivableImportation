package protecaesimportarfaturaareceber;

import Robo.AppRobo;
import protecaesimportarfaturaareceber.Control.Control;

public class ProtecaesImportarFaturaAReceber {

    public static void main(String[] args) {
        String nomeRobo = " Importar Fatura a Receber ";
        try {
            AppRobo robo = new AppRobo(nomeRobo);

            //regex diferent of number
            String regex = "[^0-9]";
        
            robo.definirParametros();
            int mes = Integer.valueOf(robo.getParametro("mes").replaceAll(regex, ""));
            int ano = Integer.valueOf(robo.getParametro("ano").replaceAll(regex, ""));
            int empresa = Integer.valueOf(robo.getParametro("empresa").replaceAll(regex, ""));
            
            robo.setNome(empresa + nomeRobo + mes + "/" + ano);
            robo.executar(Control.run(mes, ano, empresa)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
