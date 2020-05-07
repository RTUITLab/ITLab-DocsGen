package com.service.generatexls.service;

import com.service.generatexls.dto.Event;
import lombok.val;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RestTemplateService {
    //  static String q = "?end=2020-05-04T00:00:00Z&begin=2020-01-01T00:00:00Z";
    @Autowired
    private RestTemplate restTemplate;

    public List<Event> getJson() {

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "tmWdXwVzGNy94jwMUXYtApadJUFChYuknEUxrkzsyqUBpfKksDNTpRbh7u22EEJx7pE4t4ThjKf");

        HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);

        ResponseEntity<List<Event>> response = restTemplate.exchange(
                "https://dev.rtuitlab.ru/api/event/docsGen?end=2020-07-04T00:00:00Z&begin=2019-01-01T00:00:00Z",
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<Event>>() {
                });

        HashMap<String, HashMap<Date, String>> data = new HashMap<>();
        Set<String> userSet = new HashSet<>();
        ArrayList<Date> dateSet = new ArrayList<>();

        for (val event : response.getBody()) {
            for (val shift : event.getShifts()) {
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

        quickSort(list,0,list.size()-1); // Быстрая сортировка по фамилиям

        list.stream().forEach(user -> {
            val userSplitted = user.split(" ");
            val row = sheet1.createRow(rowNum.getAndIncrement());
            AtomicInteger col = new AtomicInteger();
            row.createCell(col.getAndIncrement()).setCellValue(userSplitted[1]); // Фамилия
            row.createCell(col.getAndIncrement()).setCellValue(userSplitted[0]); // Имя
            dateSet.stream().forEach(date -> {
                val cell = row.createCell(col.getAndIncrement());
                val cellValue = data.get(user).get(date);
                cell.setCellValue(cellValue != null ? cellValue : "-");
            });
        });


        try (FileOutputStream outputStream = new FileOutputStream("сводка.xlsx")) {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.getBody();
    }


    private void quickSort(List<String> list,int low, int high) {
        if (low >= high)
            return;
        int i = low, j = high;
        int cur = i - (i - j) / 2;
        while (i < j) {
            while (i < cur && (list.get(i).split(" ")[1].compareTo(list.get(cur).split(" ")[1])<=0)) {
                i++;
            }
            while (j > cur && (list.get(cur).split(" ")[1].compareTo(list.get(j).split(" ")[1])<=0)) {
                j--;
            }
            if (i < j) {
                String temp = list.get(i);
                list.set(i,list.get(j));
                list.set(j,temp);
                if (i == cur)
                    cur = j;
                else if (j == cur)
                    cur = i;
            }
        }
        quickSort(list,low, cur);
        quickSort(list,cur+1, high);

    }
}
