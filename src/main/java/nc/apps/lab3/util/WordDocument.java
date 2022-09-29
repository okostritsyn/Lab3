package nc.apps.lab3.util;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

public class WordDocument {
    private  XWPFDocument document;

    public WordDocument() {
        document = new XWPFDocument();
    }

    public  void addTitle(String text) {
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText(text);
        titleRun.setColor("009933");
        titleRun.setBold(true);
        titleRun.setFontFamily("Courier");
        titleRun.setFontSize(20);
    }

    public  void addText(String text) {
        XWPFParagraph paragraph = document.createParagraph();
        paragraph.setAlignment(ParagraphAlignment.LEFT);
        XWPFRun paragRun = paragraph.createRun();
        paragRun.setText(text);
    }

    public  void addSubTitle(String text) {
        XWPFParagraph subTitle = document.createParagraph();
        subTitle.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun subTitleRun = subTitle.createRun();
        subTitleRun.setText(text);
        subTitleRun.setColor("00CC44");
        subTitleRun.setFontFamily("Courier");
        subTitleRun.setFontSize(16);
        subTitleRun.setTextPosition(20);
        subTitleRun.setUnderline(UnderlinePatterns.DOT_DOT_DASH);
    }

    public  void addImage(Path imagePath) throws IOException, InvalidFormatException, URISyntaxException {
        XWPFParagraph image = document.createParagraph();
        image.setAlignment(ParagraphAlignment.CENTER);

        XWPFRun imageRun = image.createRun();
        imageRun.setTextPosition(20);

        imageRun.addPicture(Files.newInputStream(imagePath),
                XWPFDocument.PICTURE_TYPE_JPEG, imagePath.getFileName().toString(),
                Units.toEMU(50), Units.toEMU(50));
    }

    public  void writeToFile(String output) throws IOException {
        FileOutputStream out = new FileOutputStream(output);
        document.write(out);
        out.close();
    }
}
