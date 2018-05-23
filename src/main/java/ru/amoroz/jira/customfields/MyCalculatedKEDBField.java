package ru.amoroz.jira.customfields;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.fields.CustomField;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.amoroz.jira.customfields.kedbmodel.KedbItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyCalculatedKEDBField extends CalculatedCFType {
    private static final Logger log = LoggerFactory.getLogger(MyCalculatedKEDBField.class);

    @Override
    public Object getValueFromIssue(CustomField customField, Issue issue) {
        String filename = "C:/kedbexcelold.xls";

        if (!(filename.toUpperCase().endsWith(".XLSX") || filename.toUpperCase().endsWith(".XLS"))) {
            return "Некорректный файл. Д.б. *.xls или *.xlsx";
        }

        List<KedbItem> kedbItemList = getKedbItemListFromExcel(filename);

        if (kedbItemList == null || kedbItemList.isEmpty()) {
            return "Файл с KEDB отсутствует или не сконфигурирован";
        } else {
            return kedbItemList.stream()
                    .filter(e -> e.getKey().trim().toUpperCase().equals(issue.getSummary().trim().toUpperCase()))
                    .findFirst()
                    .map(e -> e.getProblem())
                    .orElse("Нет соответствий");
        }
    }

    @Override
    public String getStringFromSingularObject(Object o) {
        return o.toString();
    }

    @Override
    public Object getSingularObjectFromString(String s) throws FieldValidationException {
        return s;
    }

    public List<KedbItem> getKedbItemListFromExcel(String excelFileNameAddress) {
        try {
            Workbook workbook = null;

            if (excelFileNameAddress.toUpperCase().endsWith(".XLSX")) {
                workbook = new XSSFWorkbook(new FileInputStream(new File(excelFileNameAddress)));
            } else if (excelFileNameAddress.toUpperCase().endsWith(".XLS")) {
                workbook = new HSSFWorkbook(new FileInputStream(new File(excelFileNameAddress)));
            } else {
                return null;
            }

            Sheet sheet = workbook.getSheetAt(0);

            List<KedbItem> kedbItemList = new ArrayList<>();
            int rownum = 0;
            Cell cell;
            Row row;

            row = sheet.getRow(rownum);

            while (true) {
                rownum++;
                row = sheet.getRow(rownum);

                if (row != null && row.getCell(0).getStringCellValue() != null && !row.getCell(0).getStringCellValue().isEmpty()) {
                    kedbItemList.add(new KedbItem(
                            row.getCell(0).getStringCellValue(),
                            row.getCell(1).getStringCellValue(),
                            row.getCell(2).getStringCellValue(),
                            row.getCell(3).getStringCellValue(),
                            row.getCell(4).getStringCellValue())
                    );
                } else {
                    return kedbItemList;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}