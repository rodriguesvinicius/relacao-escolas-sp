package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.Normalizer;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
public class ExcelUtils {

    private static final String FILE_PATH = "todas-escolas.xlsx";
    private static final String OUTPUT_FILE = "escolas_processadas.xlsx";


    public static List<Escola> readSchoolList() {
        try (InputStream inputStream = getClassLoader().getResourceAsStream(FILE_PATH)) {

            assert inputStream != null;

            try (Workbook workbook = WorkbookFactory.create(inputStream)) {

                Sheet sheet = workbook.getSheetAt(0);
                return readSheetRows(sheet);

            }
        } catch (IOException e) {

            e.printStackTrace();
        }
        return List.of();
    }

    private static ClassLoader getClassLoader() {
        return ExcelUtils.class.getClassLoader();
    }

    private static List<Escola> readSheetRows(Sheet sheet) {
        return IntStream.range(1, sheet.getPhysicalNumberOfRows())
                .mapToObj((rowIndex) -> {
                    Row row = sheet.getRow(rowIndex);
                    Escola escola = new Escola();

                    escola.setDre(cellToString(row.getCell(0)));
                    escola.setCodesc(String.valueOf(row.getCell(1) != null && !row.getCell(1).toString().isEmpty() ? Integer.parseInt(cellToString(row.getCell(1))) : 0));
                    escola.setTipoesc(cellToString(row.getCell(2)));
                    escola.setNomes(cellToString(row.getCell(3)));
                    escola.setNomescofi(cellToString(row.getCell(4)));
                    escola.setCeu(cellToString(row.getCell(5)));
                    escola.setDiretoria(cellToString(row.getCell(6)));
                    escola.setSubpref(cellToString(row.getCell(7)));
                    escola.setEndereco(cellToString(row.getCell(8)));
                    escola.setNumero(cellToString(row.getCell(9)));
                    escola.setBairro(cellToString(row.getCell(10)));
                    escola.setCep(String.valueOf(row.getCell(11) != null && !row.getCell(11).toString().isEmpty() ? Integer.parseInt(cellToString(row.getCell(11))) : 0));
                    escola.setTel1(cellToString(row.getCell(12)));
                    escola.setTel2(cellToString(row.getCell(13)));
                    escola.setFax(cellToString(row.getCell(14)));
                    escola.setSituacao(cellToString(row.getCell(15)));
                    escola.setCoddist(String.valueOf(row.getCell(16) != null && !row.getCell(16).toString().isEmpty() ? Integer.parseInt(cellToString(row.getCell(16))) : 0));
                    escola.setDistrito(cellToString(row.getCell(17)));
                    escola.setSetor(String.valueOf(row.getCell(18) != null && !row.getCell(18).toString().isEmpty() ? Integer.parseInt(cellToString(row.getCell(18))) : 0));
                    escola.setCodinep(String.valueOf(row.getCell(19) != null && !row.getCell(19).toString().isEmpty() ? Integer.parseInt(cellToString(row.getCell(19))) : 0));
                    escola.setCdCie(String.valueOf(row.getCell(20) != null && !row.getCell(20).toString().isEmpty() ? Integer.parseInt(cellToString(row.getCell(20))) : 0));
                    escola.setEh(String.valueOf(row.getCell(21) != null && !row.getCell(21).toString().isEmpty() ? Long.parseLong(cellToString(row.getCell(21))) : 0));
                    escola.setFxEtaria(cellToString(row.getCell(22)));
                    escola.setDtCriacao(cellToString(row.getCell(23)));
                    escola.setAtoCriacao(cellToString(row.getCell(24)));
                    escola.setDomCriacao(cellToString(row.getCell(25)));
                    escola.setDtIniConv(cellToString(row.getCell(26)));
                    escola.setDtAutoriza(cellToString(row.getCell(27)));
                    escola.setDtExtincao(cellToString(row.getCell(28)));
                    escola.setNomeAnt(cellToString(row.getCell(29)));
                    escola.setRede(cellToString(row.getCell(30)));
                    escola.setLatitude(row.getCell(31) != null && !row.getCell(31).toString().isEmpty() ? Double.parseDouble(cellToString(row.getCell(31))) : 0.0);
                    escola.setLongitude(row.getCell(32) != null && !row.getCell(32).toString().isEmpty() ? Double.parseDouble(cellToString(row.getCell(32))) : 0.0);
                    escola.setDatabase(cellToString(row.getCell(33)));

                    System.out.println("Escola processed: " + escola);
                    return escola;
                })
                .collect(Collectors.toList());
    }

    private static String cellToString(Cell cell) {
        return (cell == null || cell.getCellType() == CellType.BLANK) ? ""
                : (cell.getCellType() == CellType.STRING) ? cell.getStringCellValue()
                : (DateUtil.isCellDateFormatted(cell)) ? cell.getDateCellValue().toString()
                : String.valueOf((int) cell.getNumericCellValue());
    }

    public static String removerAcentos(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("[^\\p{ASCII}]", "");
    }


    public static void createExcel(List<Escola> escolas) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Escolas Processadas");

            createHeaderRow(sheet);
            fillSheetWithData(sheet, escolas);

            try (FileOutputStream fileOut = new FileOutputStream(OUTPUT_FILE)) {
                workbook.write(fileOut);
                System.out.println("Arquivo Excel criado com sucesso: " + OUTPUT_FILE);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {"DRE", "CODESC", "TIPOESC", "NOMES", "NOMESCOFI", "CEU", "DIRETORIA",
                "SUBPREF", "ENDERECO", "NUMERO", "BAIRRO", "CEP", "TEL1", "TEL2", "FAX", "SITUACAO",
                "CODDIST", "DISTRITO", "SETOR", "CODINEP", "CD_CIE", "EH", "FX_ETARIA", "NOME_ANT",
                "REDE", "LATITUDE", "LONGITUDE", "DISTANCIA_SUA_CASA_KM"};
        IntStream.range(0, headers.length)
                .forEach(i -> headerRow.createCell(i).setCellValue(headers[i]));
    }

    private static void fillSheetWithData(Sheet sheet, List<Escola> escolas) {
        IntStream.range(0, escolas.size())
                .forEach(i -> {
                    Row row = sheet.createRow(i + 1);

                    Escola escola = escolas.get(i);
                    String[] data = {
                            escola.getDre(),
                            escola.getCodesc(),
                            escola.getTipoesc(),
                            escola.getNomes(),
                            escola.getNomescofi(),
                            escola.getCeu(),
                            escola.getDiretoria(),
                            escola.getSubpref(),
                            escola.getEndereco(),
                            escola.getNumero(),
                            escola.getBairro(),
                            escola.getCep(),
                            escola.getTel1(),
                            escola.getTel2(),
                            escola.getFax(),
                            escola.getSituacao(),
                            escola.getCoddist(),
                            escola.getDistrito(),
                            escola.getSetor(),
                            escola.getCodinep(),
                            escola.getCdCie(),
                            escola.getEh(),
                            escola.getFxEtaria(),
                            escola.getNomeAnt(),
                            escola.getRede(),
                            String.valueOf(escola.getLatitude()),
                            String.valueOf(escola.getLongitude()),
                            decimalFormat(CalculadoraDistancia.calcularDistancia(escola.getMinhaLatitude(), escola.getMinhaLongitude(), escola.getLatitude(), escola.getLongitude())) + " KM"
                    };
                    IntStream.range(0, data.length)
                            .forEach(j -> row.createCell(j).setCellValue(data[j]));
                });
    }

    private static String decimalFormat(Double value) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
        decimalFormat.setRoundingMode(RoundingMode.DOWN);
        return decimalFormat.format(value);
    }

}
