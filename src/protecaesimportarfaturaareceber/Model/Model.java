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

    private Integer ano;

    private String mesMM;
    private String nomeEmpresa;
    private String pastaEmpresa;

    private File pastaMovimento = null;
    private File arquivoFatura = null;

    private List<LctoTemplate> lctosFatura = new ArrayList<>();

    private StringBuilder textoImportação = new StringBuilder();

    public Model(int mes, int ano, int empresa) {
        this.ano = ano;
        this.mesMM = (mes < 10 ? "0" : "") + mes;

        definirEmpresa(empresa);
    }

    public void definirEmpresa(int empresaCodigo) {
        this.nomeEmpresa = Config.config.fetch("enterprisesNames", empresaCodigo + "");
        this.pastaEmpresa = Config.config.fetch("enterprisesFolders", empresaCodigo + "");
    }

    public boolean setPastaMovimento() {
        try {
            String movimentoPath = Config.config.fetch("folders", "movimento");
            movimentoPath = movimentoPath.replaceAll(":pastaEmpresa:", this.pastaEmpresa);
            movimentoPath = movimentoPath.replaceAll(":mes:", this.mesMM);
            movimentoPath = movimentoPath.replaceAll(":ano:", this.ano.toString());

            pastaMovimento = new File(movimentoPath);
            if (!pastaMovimento.exists()) {
                obs = roboView.mensagemNaoEncontrado(pastaMovimento);
            }

            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean setArquivoFatura() {
        String filtroNomeArquivoFatura = Config.config.fetch("files", "fatura");
        try {
            arquivoFatura = Selector.getFileOnFolder(pastaMovimento, filtroNomeArquivoFatura, ".xlsx");

            if (arquivoFatura.exists()) return true;
        } catch (Exception e) {
        }

        obs = filtroNomeArquivoFatura;
        return false;
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
            int deb     = Integer.valueOf(Config.config.fetch("codigos", "debito"));
            int cred    = Integer.valueOf(Config.config.fetch("codigos", "credito"));
            int hp      = Integer.valueOf(Config.config.fetch("codigos", "historicoPadrao"));

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

    public boolean salvarTextoImportação() {
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

    public File getPastaMovimento() {
        return pastaMovimento;
    }

    public File getArquivoFatura() {
        return arquivoFatura;
    }

}
