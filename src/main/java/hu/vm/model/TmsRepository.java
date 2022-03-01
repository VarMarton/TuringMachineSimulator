package hu.vm.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hu.vm.controller.data.RuleProcessor;
import hu.vm.controller.data.SettingsController;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import hu.vm.entity.SaveImage;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Log4j2
@RequiredArgsConstructor
public class TmsRepository {

    private final SettingsController settingsController;
    private final RuleProcessor ruleProcessor;

    private SaveImage currentSaveImage;

    public void save(String path) {
        currentSaveImage = createNewSaveImage();
        try {
            String toSave = getSaveImageAsJson(currentSaveImage);
            String toSaveBytes = convertStringToBinaryString(toSave);
            byte[] byteToSave = toSaveBytes.getBytes(StandardCharsets.UTF_8);
            Path file = Paths.get(path);
            Files.write(file, byteToSave, StandardOpenOption.CREATE);
        } catch (IOException e) {
            log.error(e);
        }
    }

    public void load(String path) {
        Path file = Paths.get(path);
        try {
            List<String> lines = Files.readAllLines(file);
            String toProcess = convertBinaryStringLinesToString(lines);
            currentSaveImage = getJsonAsSaveImage(toProcess);
            loadSaveImage(currentSaveImage);
        } catch (IOException e) {
            log.error(e);
        }
    }

    private SaveImage createNewSaveImage() {
        return SaveImage
                .builder()
                .states(settingsController.getRawStates())
                .startState(settingsController.getRawStartState())
                .endState(settingsController.getRawEndStates())
                .heads(settingsController.getAllHeadPositions())
                .contents(settingsController.getRawContents())
                .rules(ruleProcessor.getRawRules())
                .signature(getSignature())
                .build();
    }

    private void loadSaveImage(SaveImage image) {
        settingsController.setRawStates(image.getStates());
        settingsController.setRawStartState(image.getStartState());
        settingsController.setRawEndStates(image.getEndState());
        settingsController.setAllHeadPositions(image.getHeads());
        settingsController.setRawContents(image.getContents());
        ruleProcessor.setRawRules(image.getRules());
    }

    private String getSaveImageAsJson(SaveImage image) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(image);
    }

    private SaveImage getJsonAsSaveImage(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, SaveImage.class);
    }

    private String convertStringToBinaryString(String input) {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            output.append(Integer.toBinaryString(input.charAt(i)));
            output.append("\n");
        }
        return output.toString();
    }

    private String convertBinaryStringLinesToString(List<String> input) {
        StringBuilder output = new StringBuilder();
        for (String line : input) {
            String s = String.valueOf((char)Integer.parseInt(line, 2));
            output.append(s);
        }
        return output.toString();
    }

    private String getSignature() {
        String signature;
        if (currentSaveImage != null && StringUtils.isNotEmpty(currentSaveImage.getSignature())) {
            signature = currentSaveImage.getSignature();
        } else {
            signature = createSignature();
        }
        return signature;
    }

    private String createSignature() {
        Date date = new Date();
        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp.toString();
    }
}
