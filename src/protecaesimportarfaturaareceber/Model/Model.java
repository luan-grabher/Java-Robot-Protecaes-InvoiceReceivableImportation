package protecaesimportarfaturaareceber.Model;

import Robo.View.roboView;
import TemplateContabil.Model.Entity.LctoTemplate;
import fileManager.FileManager;
import fileManager.Selector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Model {

    public String obs = "";

    private Integer mes;
    private Integer ano;
    private Integer empresa;

    private String mesMM;
    private String nomeEmpresa;
    private String pastaEmpresa;

    private String pastaMovimentoPadrao = "\\\\HEIMERDINGER\\DOCS\\Contábil\\Clientes\\#NOMEEMPRESA#\\Escrituração mensal\\#ANO#\\Movimento\\#MES#.#ANO#";
    private File pastaMovimento = null;

    private File arquivoFatura = null;

    private List<LctoTemplate> lctosFatura = new ArrayList<>();

    private StringBuilder textoImportação = new StringBuilder();

    public Model(int mes, int ano, int empresa) {
        this.mes = mes;
        this.ano = ano;
        this.empresa = empresa;

        this.mesMM = (mes < 10 ? "0" : "") + mes;

        definirEmpresa();
    }

    public void definirEmpresa() {
        this.nomeEmpresa = empresa == 332 ? "Proterisco" : "Protecães";
        this.pastaEmpresa = empresa == 332 ? "Proterisco Locações e Soluções em Segurança Ltda" : "Protecães Locação de Cães e Sistemas de Segurança Eireli";
    }

    public boolean setPastaMovimento() {
        try {
            String path = pastaMovimentoPadrao.replaceAll("#NOMEEMPRESA#", this.pastaEmpresa);
            path = path.replaceAll("#MES#", mesMM).replaceAll("#ANO#", ano.toString());

            pastaMovimento = new File(path);
            if (!pastaMovimento.exists()) {
                obs = roboView.mensagemNaoEncontrado(pastaMovimento);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setArquivoFatura() {
        String filtroNomeArquivoFatura = "Fatura;Receber;.xls";
        try {
            arquivoFatura = Selector.getFileOnFolder(pastaMovimento, filtroNomeArquivoFatura, ".xlsx");

            if (arquivoFatura.exists()) {
                return true;
            } else {
                obs = filtroNomeArquivoFatura;
                return false;
            }
        } catch (Exception e) {
            obs = filtroNomeArquivoFatura;
            return false;
        }
    }

    public boolean setLctosFatura() {
        try {
            FaturaAReceber_Model modeloFatura = new FaturaAReceber_Model(arquivoFatura);

            modeloFatura.setLctosFatura();
            lctosFatura = modeloFatura.getLctosFatura();
            return lctosFatura.size() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean setTextoImportação() {
        try {
            int deb = 11;
            int cred = 5849;
            int hp = 51;

            for (LctoTemplate lctoTemplate : lctosFatura) {
                textoImportação.append(lctoTemplate.getData()).append(";");
                textoImportação.append(deb).append(";");
                textoImportação.append(cred).append(";");
                textoImportação.append(hp).append(";");
                textoImportação.append(lctoTemplate.getHistorico()).append(";");
                textoImportação.append(lctoTemplate.getValor().toPlainString());
                textoImportação.append("\r\n");
            }
            
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public boolean salvarTextoImportação(){
        String pathArquivoFaturaTxt = arquivoFatura.getAbsolutePath().replaceAll(".xls", ".csv");
        obs = pathArquivoFaturaTxt; 
        return FileManager.save(pathArquivoFaturaTxt, textoImportação.toString());
    }

    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    public String getPastaEmpresa() {
        return pastaEmpresa;
    }

    public String getPastaMovimentoPadrao() {
        return pastaMovimentoPadrao;
    }

    public File getPastaMovimento() {
        return pastaMovimento;
    }

    public File getArquivoFatura() {
        return arquivoFatura;
    }

}
