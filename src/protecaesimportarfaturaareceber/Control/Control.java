package protecaesimportarfaturaareceber.Control;

import Robo.View.roboView;
import java.io.File;
import protecaesimportarfaturaareceber.Model.Model;

public class Control {
    public static String run(int mes, int ano, int empresa){
        Model modelo= new Model(mes, ano, empresa);
        
        if(!modelo.setPastaMovimento())     return modelo.obs;
        if(!modelo.setArquivoFatura())      return roboView.mensagemNaoEncontrado(modelo.obs, modelo.getPastaMovimento());
        if(!modelo.setLctosFatura())        return "Não existe lançamentos no arquivo " + roboView.link(modelo.getArquivoFatura()) + " ou ocorreu algum erro ao buscar os lançamentos.<br>";
        if(!modelo.setTextoImportação())    return "Não consegui criar o texto para o arquivo CSV por algum motivo. Os arquivos estão corretos?<br>";
        if(!modelo.salvarTextoImportação()) return "Erro ao salvar o arquivo " + roboView.link(new File(modelo.obs)) + ", você está com ele aberto?<br>";
        
        return "Arquivo CSV salvo em: " + roboView.link(new File(modelo.obs)) + "<br>";
    }
}
