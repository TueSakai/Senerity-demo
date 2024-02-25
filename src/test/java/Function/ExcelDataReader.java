package Function;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static Variable.Variables.excelFilePath;

public class ExcelDataReader {
    public static Object[][] readExcelData(String filePath, String sheetName) {
        try {
            FileInputStream file = new FileInputStream(new File(filePath));
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet(sheetName);

            int rowCount = sheet.getLastRowNum();
            int colCount = sheet.getRow(0).getLastCellNum();

            Object[][] data = new Object[rowCount][colCount];
            for (int i = 1; i <= rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    data[i - 1][j] = cell.getStringCellValue();
                }
            }

            return data;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void readExcel1(String filePath, String sheetName) {

        try {
            FileInputStream fis = new FileInputStream(new File(filePath));
            Workbook workbook = WorkbookFactory.create(fis);

            Sheet sheet = workbook.getSheet(sheetName);
            Row row = sheet.getRow(2);

//            for (Row row : sheet) {
                for (Cell cell : row) {
                    switch (cell.getCellType()) {
                        case STRING:
                            System.out.print(cell.getStringCellValue() + "\t|");
                            break;
                        case NUMERIC:
                            System.out.print(cell.getNumericCellValue() + "\t|");
                            break;
                        case BOOLEAN:
                            System.out.print(cell.getBooleanCellValue() + "\t|");
                            break;
                        default:
                            System.out.print("\t");
                    }

//                }
//                System.out.println();

                }

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Object[]> readExcel(String filePath, String sheetName) throws IOException , InvalidFormatException{
        List<Object[]> data = new ArrayList<>();

        FileInputStream fis = new FileInputStream(new File(filePath));
        Workbook workbook = WorkbookFactory.create(fis);
        Sheet sheet = workbook.getSheet(sheetName);
        Iterator<Row> rowIterator = sheet.iterator();

        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            List<Object> rowData = new ArrayList<>();
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                switch (cell.getCellType()) {
                    case STRING:
                        rowData.add(cell.getStringCellValue());
                        break;
                    case NUMERIC:
                        rowData.add(cell.getNumericCellValue());
                        break;
                    case BOOLEAN:
                        rowData.add(cell.getBooleanCellValue());
                        break;
                    default:
                        rowData.add(null);
                        break;
                }
            }
            data.add(rowData.toArray());
        }
        workbook.close();
        fis.close();

        return data;
    }

    public static void main(String[] args) {
        String filePath = excelFilePath;
//        readExcel1(filePath,"Search flight");

        try {
            List<Object[]> data = readExcel(filePath, "Passenger info");
            Object[] row = data.get(data.size()-1);
            for (Object cellData : row) {
                System.out.print(cellData + "\t\t");

            }

            System.out.println();

        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

}
