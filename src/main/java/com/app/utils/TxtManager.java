package com.app.utils;

import com.app.exceptions.MyException;
import com.app.model.entities.Doctor;
import com.app.model.entities.Patient;
import com.app.model.entities.Perscription;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TxtManager {
    @Value("${txt.path}")
    private String txtPath;

    private String createFilename() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")) + ".txt";
    }

    public String addFile(Doctor doctor, Perscription perscription) {
        final String filename = createFilename();
        final String fullPath = txtPath + filename;
        try {
            FileWriter fileWriter = new FileWriter(fullPath);
            PrintWriter pw = new PrintWriter(fileWriter);
            pw.println("Patient:");
            pw.println("First name: " + perscription.getPatient().getFirstName());
            pw.println("Last name: " + perscription.getPatient().getLastName());
            pw.println("------------------------------------------");
            pw.println("Medicine name: " + perscription.getMedicineName());
            pw.println("Quantity: " + perscription.getQuantity());
            pw.println("-------------------------------------------");
            pw.println("Doctor:");
            pw.println("First name: " + doctor.getFirstName());
            pw.println("Last name: " + doctor.getLastName());
            pw.println("Date: " + perscription.getDate());
            pw.close();
            return filename;
        } catch (Exception e) {
            throw new MyException("add txt file exception", LocalDateTime.now());
        }

    }
}
