package com.app.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;


@Component
public class PdfGeneratorUtil {
    @Value("${pdf.path}")
    private String pdfPath;
    @Autowired
    private TemplateEngine templateEngine;

    private String createFilename() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
    }

    public String createPdf(String templateName, Map map) throws Exception {
        Assert.notNull(templateName, "The templateName can not be null");
        Context ctx = new Context();
        if (map != null) {
            Iterator itMap = map.entrySet().iterator();
            while (itMap.hasNext()) {
                Map.Entry pair = (Map.Entry) itMap.next();
                ctx.setVariable(pair.getKey().toString(), pair.getValue());
            }
        }

        String processedHtml = templateEngine.process(templateName, ctx);
        FileOutputStream os = null;
        String fileName = createFilename();
        try {
            String tDir = System.getProperty("java.io.tmpdir");
            final File outputFile = new File(pdfPath + fileName + ".pdf");
            os = new FileOutputStream(outputFile);

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(os, false);
            renderer.finishPDF();
            System.out.println("PDF created successfully");
            return fileName + ".pdf";
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                }
            }
        }
    }
}