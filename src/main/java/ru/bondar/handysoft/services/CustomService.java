package ru.bondar.handysoft.services;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;

@Service
public class CustomService {

    private int[] arr;

    public ResponseEntity<?> extractValue(String pathToFile, Integer n) {

        if (!isFileXlsx(pathToFile)) {
            return new ResponseEntity<>("Не верный формат указанного файла", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        }

        try {
            readFileToArray(pathToFile);
        } catch (IOException e) {
            return new ResponseEntity<>("Ошибка при попытке чтения файла: " + e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }

        if (arr.length == 0) {
            return new ResponseEntity<>("Указанный файл пуст", HttpStatus.NOT_FOUND);
        }

        if (n > arr.length || n <= 0) {
            return new ResponseEntity<>("Приведённое число вне диапазона (значение больше чем кол-во чисел в файле или не больше нуля)", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Результат поиска указанного значения в файле: " + quickSort(arr, 0, arr.length - 1, n), HttpStatus.OK);
    }

    public Boolean isFileXlsx(String pathToFile) {
        return pathToFile.endsWith("xlsx");
    }

    private Integer quickSort(int[] array, int start, int end, int n) {
        if (start <= end) {
            int pivotIndex = partition(array, start, end);

            if (pivotIndex == n) {
                return array[pivotIndex];
            } else if (pivotIndex > n) {
                return quickSort(array, start, pivotIndex - 1, n);
            } else {
                return quickSort(array, pivotIndex + 1, end, n);
            }
        }

        return null;
    }

    private int partition(int[] array, int start, int end) {
        int pivot = array[end];

        int i = start - 1;

        for (int j = start; j < end; j++) {
            if (array[j] <= pivot) {
                i++;
                swap(array, i, j);
            }
        }
        swap(array, i + 1, end);
        return i + 1;
    }

    private void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private void readFileToArray(String pathToFile) throws IOException {
        FileInputStream fis = new FileInputStream(pathToFile);
        Workbook workbook = new XSSFWorkbook(fis);
        Sheet sheet = workbook.getSheetAt(0);
        arr = new int[sheet.getPhysicalNumberOfRows()];
        for (int i = 0; i < sheet.getPhysicalNumberOfRows() - 1; i++) {
            arr[i] = (int) sheet.getRow(i).getCell(sheet.getRow(i).getLastCellNum() - 1).getNumericCellValue();
        }
    }
}
