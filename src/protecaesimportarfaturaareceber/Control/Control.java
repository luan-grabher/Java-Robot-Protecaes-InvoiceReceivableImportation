package protecaesimportarfaturaareceber.Control;

import Robo.View.roboView;
import java.io.File;
import protecaesimportarfaturaareceber.Model.Model;

public class Control {
    private static int mes;
    private static int ano;
    private static int empresa;
    
    
    
    public static String run(int mes, int ano, int empresa){
        Control.mes = mes;
        Control.ano = ano;
        Control.empresa = empresa;
        
        Model modelo= new Model(mes, ano, empresa);
        
        String r = "";
        if(modelo.setPastaMovimento()){
            if(modelo.setArquivoFatura()){
                if(modelo.setLctosFatura()){
                    if(modelo.setTextoImportação()){
                        if(modelo.salvarTextoImportação()){
                            r = "Arquivo CSV salvo em: " + roboView.link(new File(modelo.obs)) + "<br>";
                        }else{
                            r = "Erro ao salvar o arquivo " + roboView.link(new File(modelo.obs)) + ", você está com ele aberto?<br>";
                        }
                    }else{
                        r = "Não consegui criar o texto para o arquivo CSV por algum motivo. Os arquivos estão corretos?<br>";
                    }
                }else{
                    r = "Não existe lançamentos no arquivo " + roboView.link(modelo.getArquivoFatura()) + " ou ocorreu algum erro ao buscar os lançamentos.<br>";
                }
            }else{
                r = roboView.mensagemNaoEncontrado(modelo.obs, modelo.getPastaMovimento());
            }
        }else{
            r = modelo.obs;
        }
        
        
        return r;
    }
}
