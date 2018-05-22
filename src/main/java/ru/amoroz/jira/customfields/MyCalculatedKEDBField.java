package ru.amoroz.jira.customfields;

import com.atlassian.jira.issue.Issue;
import com.atlassian.jira.issue.customfields.impl.CalculatedCFType;
import com.atlassian.jira.issue.customfields.impl.FieldValidationException;
import com.atlassian.jira.issue.fields.CustomField;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
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
        String filename = "C:/kedbexcel.xlsx";
        List<KedbItem> kedbItemList = getKedbItemListFromXlsx(filename);
        if (kedbItemList == null || kedbItemList.isEmpty()) {
            return "Исходный файл KEDB не сконфигурирован";
        } else {
            return kedbItemList.stream()
                    .filter(e -> e.getKey().trim().toUpperCase().equals(issue.getSummary().trim().toUpperCase()))
                    .findFirst()
                    .map(e -> e.getProblem())
                    .orElse("Dont match");
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

    public List<KedbItem> getKedbItemListFromXlsx(String xlsxFileNameAddress) {
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(xlsxFileNameAddress)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            List<KedbItem> kedbItemList = new ArrayList<>();
            int rownum = 0;
            XSSFCell cell;
            XSSFRow row;

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
                    for (KedbItem k : kedbItemList) {
                        //System.out.println(k.getKey() + " | " + k.getParam1() + " | " + k.getParam2() + " | " + k.getProblem() + " | " + k.getLink());
                    }
                    return kedbItemList;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<KedbItem> getKedbItemListFromXls(String xlsFileNameAddress) {
        try {
            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(xlsFileNameAddress)));
            HSSFSheet sheet = workbook.getSheetAt(0);

            List<KedbItem> kedbItemList = new ArrayList<>();
            int rownum = 0;
            HSSFCell cell;//Cell cell;
            HSSFRow row;//Row row;

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
                    for (KedbItem k : kedbItemList) {
                        //System.out.println(k.getKey() + " | " + k.getParam1() + " | " + k.getParam2() + " | " + k.getProblem() + " | " + k.getLink());
                    }
                    return kedbItemList;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}