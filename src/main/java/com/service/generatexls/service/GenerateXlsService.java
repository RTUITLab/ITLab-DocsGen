package com.service.generatexls.service;

import com.service.generatexls.dto.Event;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
@Service
public class GenerateXlsService {
    //  static String q = "?end=2020-05-04T00:00:00Z&begin=2020-01-01T00:00:00Z";


    public XSSFWorkbook getXls(List<Event> events) {


        HashMap<String, HashMap<Date, String>> data = new HashMap<>(); // Сотрудник, [Дата, Роль]

        HashMap<String, ArrayList<Date>> dataEvent = new HashMap<>(); // Название события, Даты события

        Set<String> userSet = new HashSet<>();
        ArrayList<Date> dateSet = new ArrayList<>();

        for (val event : events) {
            dataEvent.putIfAbsent(event.getTitle(), new ArrayList<>());
            val dateEventAndShift = dataEvent.get(event.getTitle());
            for (val shift : event.getShifts()) {
                dateEventAndShift.add(shift.getBeginTime());
                dateSet.add(shift.getBeginTime());
                for (val place : shift.getPlaces()) {
                    for (val participants : place.getParticipants()) {
                        userSet.add(participants.getUser().getFullName());
                        data.putIfAbsent(participants.getUser().getFullName(), new HashMap<>());
                        val dateAndShift = data.get(participants.getUser().getFullName());
                        dateAndShift.put(shift.getBeginTime(), participants.getEventRole().getTitle());
                    }
                }
            }
        }

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet1 = workbook.createSheet("Сводка");
        XSSFSheet sheet2 = workbook.createSheet("События");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd");

        AtomicInteger rowNum = new AtomicInteger();
        AtomicInteger colNum = new AtomicInteger();
        val firstRow = sheet1.createRow(rowNum.getAndIncrement());
        firstRow.createCell(colNum.getAndIncrement()).setCellValue("Фамилия");
        firstRow.createCell(colNum.getAndIncrement()).setCellValue("Имя");
        dateSet.stream().forEach(date -> {
            firstRow.createCell(colNum.getAndIncrement()).setCellValue(dateFormat.format(date));
        });

        List<String> list = new ArrayList<String>(userSet);
        quickSort(list, 0, list.size() - 1); // Быстрая сортировка по фамилиям

        list.stream().forEach(user -> {

            val userSplitted = user.split(" ");
            val row = sheet1.createRow(rowNum.getAndIncrement());
            AtomicInteger col = new AtomicInteger();
            row.createCell(col.getAndIncrement()).setCellValue(userSplitted[0]); // Фамилия
            row.createCell(col.getAndIncrement()).setCellValue(userSplitted[1]); // Имя
            sheet1.autoSizeColumn(0);
            sheet1.autoSizeColumn(1);
            dateSet.stream().forEach(date -> {
                val cell = row.createCell(col.get());
                val cellValue = data.get(user).get(date);
                cell.setCellValue(cellValue != null ? cellValue : "-");
                sheet1.autoSizeColumn(col.getAndIncrement());

            });

        });

        rowNum.set(0);
        colNum.set(0);

        dataEvent.forEach((key, value) -> {
            // Заполнение второго листа
            val row = sheet2.createRow(rowNum.getAndIncrement());
            AtomicInteger col = new AtomicInteger();
            row.createCell(col.get()).setCellValue(key);
            sheet2.autoSizeColumn(col.getAndIncrement());
            value.forEach(date -> {
                sheet2.autoSizeColumn(col.get());
                row.createCell(col.getAndIncrement()).setCellValue(dateFormat.format(date));
            });

        });


        return workbook;
    }


    private void quickSort(List<String> list, int low, int high) {
        if (low >= high)
            return;
        int i = low, j = high;
        int cur = i - (i - j) / 2;
        while (i < j) {
            while ((list.get(i).compareTo(list.get(cur)) <= 0) && i < cur) {
                i++;
            }
            while ((list.get(cur).compareTo(list.get(j)) <= 0) && j > cur) {
                j--;
            }
            if (i < j) {
                String temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
                if (i == cur)
                    cur = j;
                else if (j == cur)
                    cur = i;
            }
        }
        quickSort(list, low, cur);
        quickSort(list, cur + 1, high);

    }


}