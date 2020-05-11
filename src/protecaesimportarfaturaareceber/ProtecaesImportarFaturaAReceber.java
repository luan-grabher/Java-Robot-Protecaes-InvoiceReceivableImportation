package protecaesimportarfaturaareceber;

import Robo.AppRobo;
import protecaesimportarfaturaareceber.Control.Control;

public class ProtecaesImportarFaturaAReceber {

    public static void main(String[] args) {
        String nomeRobo = " Importar Fatura a Receber ";
        
        AppRobo robo = new AppRobo(nomeRobo);
        
        robo.definirParametros();
        int mes = robo.getParametro("mes").getInteger();
        int ano = robo.getParametro("ano").getInteger();
        int empresa = robo.getParametro("empresa").getInteger();
        
        robo.setNome(empresa + nomeRobo + mes + "/" + ano);
        robo.executar(Control.run(mes, ano, empresa)
        );
    }
    
}
