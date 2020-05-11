package Teste;

import java.math.BigDecimal;
import protecaesimportarfaturaareceber.Control.Control;

public class teste {

    public static void main(String[] args) {
        int mes = 12;
        int ano = 2019;
        int empresa = 332;
        
        System.out.println(Control.run(mes, ano, empresa).replaceAll("<br>", "\n"));
    }
    
}
