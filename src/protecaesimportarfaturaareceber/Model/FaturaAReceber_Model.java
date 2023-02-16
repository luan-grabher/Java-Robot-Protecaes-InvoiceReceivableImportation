package protecaesimportarfaturaareceber.Model;

import JExcel.JExcel;
import TemplateContabil.Model.Entity.LctoTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import Dates.Dates;

public class FaturaAReceber_Model {

    private File arquivo;
    private List<LctoTemplate> lctosFatura = new ArrayList<>();

    //string date format for d/m/y
    private String dateFormat = "dd/MM/yyyy";

    private Workbook wk;
    private Sheet sheet;

    public FaturaAReceber_Model(File arquivo) {
        this.arquivo = arquivo;
    }

    public void setLctosFatura() {
        //Definir workbook
        if (setWorkbook()) {
            //Percorrer arquivo
            if (buscarLctos()) {
            }
        }

        closeWorkbook();
    }

    private void closeWorkbook() {
        try {
            wk.close();
        } catch (Exception e) {
        }
    }

    private boolean setWorkbook() {
        try {
            wk = new HSSFWorkbook(new FileInputStream(arquivo));
            sheet = wk.getSheetAt(0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean buscarLctos() {
        try {
            String colunaData = Config.config.fetch("faturaReceberColunas", "data");
            String colunaValor = Config.config.fetch("faturaReceberColunas", "valor");
            String colunaCliente = Config.config.fetch("faturaReceberColunas", "cliente");

            for (int i = 0; i <= sheet.getLastRowNum(); i++) {
                try {
                    Row row = sheet.getRow(i);

                    //Se tiver data
                    Cell cellData = row.getCell(JExcel.Cell(colunaData));
                    if (JExcel.isDateCell(cellData)) {
                        Date date = cellData.getDateCellValue();
                        Calendar dateCalendar = Calendar.getInstance();
                        dateCalendar.setTime(date);

                        //Se tiver valor
                        Cell cellValor = row.getCell(JExcel.Cell(colunaValor));
                        try {
                            BigDecimal valor = new BigDecimal(cellValor.getNumericCellValue());
                            if (valor.compareTo(BigDecimal.ZERO) != 0) {
                                //Se tiver Historico
                                String cliente = row.getCell(JExcel.Cell(colunaCliente)).getStringCellValue();

                                if (cliente.equals("")) continue;

                                lctosFatura.add(
                                    new LctoTemplate(
                                        Dates.getCalendarInThisStringFormat(dateCalendar, dateFormat),
                                        "",
                                        "FAT",
                                        cliente,
                                        valor
                                    )
                                );                                
                            }
                        } catch (Exception e) {
                        }
                    }
                } catch (Exception e) {
                }
            }

            return true;
        } catch (Exception e) {
            System.out.println("Erro: " + e);
            e.printStackTrace();
            return false;
        }
    }

    public List<LctoTemplate> getLctosFatura() {
        return lctosFatura;
    }

}
