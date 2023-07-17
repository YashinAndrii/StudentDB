package com.example.StudentDB.Controllers;

import com.example.StudentDB.Entities.Student;
import com.example.StudentDB.Repository.StudentRepository;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;

@Controller
public class MainController {
    final
    StudentRepository repository;

    public MainController(StudentRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/students")
    public String mainPage(ModelMap model) {
        model.put("students",repository.findAll());
        return "studentList";
    }

    @GetMapping("/")
    public String mainPage() {
        return "redirect:/students";
    }

    @GetMapping("add-student")
    public String addStudent(){
        return "addStudent";
    }

    @PostMapping("/add-student")
    public String addStudent(@RequestParam("lname") String lname,
                             @RequestParam("fname") String fname,
                             @RequestParam("mname") String mname,
                             @RequestParam("faculty") String faculty,
                             @RequestParam("department") String department,
                             @RequestParam("groupName") String group) {
        Student student = new Student();
        student.setDepartment(department);
        student.setFaculty(faculty);
        student.setFname(fname);
        student.setLname(lname);
        student.setMname(mname);
        student.setGroupName(group);
        repository.save(student);
        return "redirect:/students";
    }

    @GetMapping("delete")
    public String deleteStudent(@RequestParam("id") long id){
        Optional<Student> student = repository.findById(id);
        student.ifPresent(repository::delete);
        return "redirect:/students";
    }

    @PostMapping ("/generate-report")
    public String generateReport(@RequestParam("studentId") long studentId, HttpServletResponse response) throws DocumentException, IOException {
        Optional<Student> student = repository.findById(studentId);
        String fileName = student.get().getLname() + ".pdf";

        String downloadsDirectory = System.getProperty("user.home") + "/Downloads/";
        String filePath = downloadsDirectory + fileName;
        BaseFont baseFont = BaseFont.createFont("SourceSansPro-ExtraLight.otf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font font = new Font(baseFont, 12);

        Document document = new Document();
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));

        writer.setPageEvent(new PdfPageEventHelper() {
            boolean isFirstPage = true;

            @Override
            public void onStartPage(PdfWriter writer, Document document) {
                if (isFirstPage) {
                    Rectangle pageRect = document.getPageSize();
                    PdfContentByte cb = writer.getDirectContent();
                    cb.saveState();
                    cb.beginText();
                    cb.setFontAndSize(baseFont, 12);
                    cb.showTextAligned(Element.ALIGN_CENTER, "ХАРКІВСЬКИЙ НАЦІОНАЛЬНИЙ УНІВЕРСИТЕТ ІМЕНІ В.Н. КАРАЗІНА", pageRect.getWidth() / 2, pageRect.getHeight() - 30, 0);
                    cb.showTextAligned(Element.ALIGN_CENTER, "ЩОДЕННИК ПРАКТИКИ", pageRect.getWidth() / 2, pageRect.getHeight() - 60, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "_____________________________виробнича____________________________", 50, pageRect.getHeight() - 90, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "(вид і назва практики)", 50, pageRect.getHeight() - 110, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "студента " + student.get().getLname() + " " + student.get().getFname() + " " + student.get().getMname(), 50, pageRect.getHeight() - 140, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "(прізвище, ім’я, по батькові)", 50, pageRect.getHeight() - 160, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "Факультет  комп’ютерних наук", 50, pageRect.getHeight() - 190, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "Кафедра  безпеки інформаційних систем і технологій", 50, pageRect.getHeight() - 210, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "освітньо-кваліфікаційний рівень бакалавр", 50, pageRect.getHeight() - 240, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "напрям підготовки", 50, pageRect.getHeight() - 260, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "спеціальність  " + student.get().getDepartment(), 50, pageRect.getHeight() - 280, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "(назва)", 50, pageRect.getHeight() - 300, 0);
                    cb.showTextAligned(Element.ALIGN_LEFT, "4-й  курс,  група  "+ student.get().getGroupName(), 50, pageRect.getHeight() - 330, 0);
                    cb.endText();
                    cb.restoreState();
                    isFirstPage = false;
                }
            }

            @Override
            public void onEndPage(PdfWriter writer, Document document) {

            }
        });

        document.open();
        Chunk chunk = new Chunk("");
        document.add(chunk);

        document.newPage();
        PdfContentByte cb = writer.getDirectContent();
        cb.saveState();
        cb.beginText();
        cb.setFontAndSize(baseFont, 12);
        cb.showTextAligned(Element.ALIGN_LEFT, "Студент " + student.get().getLname() + " " + student.get().getFname() + " " + student.get().getMname(), 50, document.top() - 30, 0);cb.showTextAligned(Element.ALIGN_LEFT, "(прізвище, ім’я, по батькові)", 50, document.top() - 50, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "прибув на підприємство, організацію, установу", 50, document.top() - 80, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "____________________________________________________", 50, document.top() - 100, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "____________________________________________________", 50, document.top() - 120, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "Печатка", 50, document.top() - 150, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "підприємства, організації, установи \"___\" ________________ 2023  року", 50, document.top() - 170, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "____________     ________________________________________________", 50, document.top() - 200, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "(підпис)                   (посада, прізвище та ініціали відповідальної особи)", 50, document.top() - 220, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "Вибув з підприємства, організації, установи", 50, document.top() - 250, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "____________________________________________________", 50, document.top() - 270, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "____________________________________________________", 50, document.top() - 290, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "Печатка", 50, document.top() - 320, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "підприємства, організації, установи \"___\" _______________ 2023  року", 50, document.top() - 340, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "_____________     ________________________________________________", 50, document.top() - 370, 0);
        cb.showTextAligned(Element.ALIGN_LEFT, "(підпис)                    (посада, прізвище та ініціали відповідальної особи)", 50, document.top() - 390, 0);
        cb.endText();
        cb.restoreState();
        document.newPage();

        Paragraph title = new Paragraph("Календарний графік проходження практики", font);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(20);
        document.add(title);

        float[] columnWidths = { 1, 3, 1, 1,1,1,1,2};

        PdfPTable table = new PdfPTable(columnWidths);
        table.setSpacingBefore(20);
        table.setWidthPercentage(100);

        font = new Font(baseFont, 11);

        PdfPCell cell1 = new PdfPCell(new Phrase("№ з/п", font));
        cell1.setRowspan(2);
        table.addCell(cell1);

        cell1 = new PdfPCell(new Phrase("Назви робіт", font));
        cell1.setRowspan(2);
        table.addCell(cell1);

        cell1 = new PdfPCell(new Phrase("Тривалість практики 150 годин", font));
        cell1.setColspan(5);
        table.addCell(cell1);

        cell1 = new PdfPCell(new Phrase("Відмітки про виконання", font));
        cell1.setRowspan(2);

        table.addCell(cell1);

        table.addCell(String.valueOf(1));
        table.addCell(String.valueOf(2));
        table.addCell(String.valueOf(3));
        table.addCell(String.valueOf(4));
        table.addCell(String.valueOf(5));
        table.addCell(String.valueOf(1));
        table.addCell(String.valueOf(2));
        table.addCell(String.valueOf(3));
        table.addCell(String.valueOf(4));
        table.addCell(String.valueOf(5));
        table.addCell(String.valueOf(6));
        table.addCell(String.valueOf(7));
        table.addCell(String.valueOf(8));

        table.addCell(String.valueOf(2));
        table.addCell(new PdfPCell(new Phrase("Отримання інструктажів з техніки безпеки",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("Вик.",font)));

        table.addCell(String.valueOf(3));
        table.addCell(new PdfPCell(new Phrase("Ознайомлений зі службовими обов'язками",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("Вик.",font)));

        table.addCell(String.valueOf(4));
        table.addCell(new PdfPCell(new Phrase("Ознайомлений з діяльністю Технологічного центру ",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("Вик.",font)));

        table.addCell(String.valueOf(5));
        table.addCell(new PdfPCell(new Phrase("Отримання завдання",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("Вик.",font)));

        table.addCell(String.valueOf(6));
        table.addCell(new PdfPCell(new Phrase("Визначення слабких місць сайту",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("Вик.",font)));

        table.addCell(String.valueOf(7));
        table.addCell(new PdfPCell(new Phrase("Розробка рекомендацій для відповідно сайту",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("Вик.",font)));

        table.addCell(String.valueOf(8));
        table.addCell(new PdfPCell(new Phrase("Створення захищеної онлайн бази даних студентів",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("Вик.",font)));

        table.addCell(String.valueOf(9));
        table.addCell(new PdfPCell(new Phrase("Підготовка матеріалів звіту та оформлення звіту за практику",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("Вик.",font)));

        table.addCell(String.valueOf(10));
        table.addCell(new PdfPCell(new Phrase("Захист матеріалів звіту",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("",font)));
        table.addCell(new PdfPCell(new Phrase("*",font)));
        table.addCell(new PdfPCell(new Phrase("Вик.",font)));

        document.add(table);
        Paragraph leadersParagraph = new Paragraph("Керівники практики:", font);
        document.add(leadersParagraph);

        Paragraph universityLeaderParagraph = new Paragraph("від вищого навчального закладу ______\t\t_____________", font);
        universityLeaderParagraph.setSpacingAfter(10); // Расстояние после строки
        document.add(universityLeaderParagraph);

        Paragraph universitySignatureParagraph = new Paragraph("(підпис)\t\t(прізвище та ініціали)", font);
        universitySignatureParagraph.setSpacingAfter(10); // Расстояние после строки
        document.add(universitySignatureParagraph);

        Paragraph companyLeaderParagraph = new Paragraph("від підприємства, організації, установи ______\t\t_____________", font);
        document.add(companyLeaderParagraph);

        Paragraph companySignatureParagraph = new Paragraph("(підпис)\t\t(прізвище та ініціали)", font);
        document.add(companySignatureParagraph);

        document.close();
        writer.close();
        document.close();

        return "redirect:/students";
    }
}
