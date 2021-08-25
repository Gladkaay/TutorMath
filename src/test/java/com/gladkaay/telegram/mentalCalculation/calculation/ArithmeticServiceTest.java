package com.gladkaay.telegram.mentalCalculation.calculation;

import com.gladkaay.telegram.mentalCalculation.telegram.nonCommand.Settings;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.gladkaay.telegram.mentalCalculation.enums.OperationEnum;
import com.gladkaay.telegram.mentalCalculation.exceptions.IllegalSettingsException;
import com.gladkaay.telegram.mentalCalculation.fileProcessor.WordFileProcessorImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ArithmeticServiceTest {
    Logger logger = LoggerFactory.getLogger(ArithmeticServiceTest.class);
    ArithmeticService service;

    ArithmeticServiceTest() {
        this.service = new ArithmeticService(new WordFileProcessorImpl(), new Calculator());
    }

    @Test
    void getArithmeticFileTest() throws IOException {
        logger.info("Начинаем создание настроек");
        List<Settings> settingsList = createSettings();

        logger.info("Начинаем проверку формирования файлов");
        checkSettings(settingsList);

        logger.info("Успех");
    }

    /**
     * Создание списка настроек выгружаемого файла
     */
    private List<Settings> createSettings() {
        List<Settings> settingsList = new ArrayList<>();
        List<Integer> numbers = getNumbers();
        List<String> illegalNumbers = new ArrayList<>();

        for (Integer i : numbers) {
            for (Integer j : numbers) {
                try {
                    settingsList.add(new Settings(i, j, 1));
                } catch (IllegalSettingsException e) {
                    illegalNumbers.add(String.format("%s и %s", i, j));
                }
            }
        }
        logger.info("Не удалось создать настройки для следующих пар значений:");
        illegalNumbers.forEach(logger::info);
        return settingsList;
    }

    /**
     * Создания списка используемых чисел
     */
    private List<Integer> getNumbers() {
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i < 21; i++) {
            numbers.add(i);
        }
        return numbers;
    }

    /**
     * Проверка получения стрима на основе сформированных настроек
     */
    private void checkSettings(List<Settings> settingsList) throws IOException {
        for (Settings settings : settingsList) {
            logger.info(String.format("Проверяем пару значений %s - %s", settings.getMin(), settings.getMax()));
            Assertions.assertNotNull(service.getFile(Collections.singletonList(OperationEnum.ADDITION),
                    settings));
            Assertions.assertNotNull(service.getFile(Collections.singletonList(OperationEnum.SUBTRACTION),
                    settings));
            Assertions.assertNotNull(service.getFile(Collections.singletonList(OperationEnum.MULTIPLICATION),
                    settings));
            Assertions.assertNotNull(service.getFile(Collections.singletonList(OperationEnum.DIVISION),
                    settings));
            logger.info(String.format("Проверка пары значений %s - %s прошла успешно", settings.getMin(),
                    settings.getMax()));
        }
    }
}