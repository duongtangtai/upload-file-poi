package com.hieulexuan.uploadexcelfile.project.service;

import com.hieulexuan.uploadexcelfile.project.model.Project;
import com.hieulexuan.uploadexcelfile.project.repository.ProjectRepository;
import com.hieulexuan.uploadexcelfile.task.model.Task;
import com.hieulexuan.uploadexcelfile.task.repository.TaskRepository;
import com.hieulexuan.uploadexcelfile.user.model.User;
import com.hieulexuan.uploadexcelfile.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.*;

public interface ProjectService {
    List<Project> importMulFile(MultipartFile multipartFile) throws IOException;
}

@Service
@Transactional
@RequiredArgsConstructor
class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final EntityManager entityManager;

    private void saveEntity(Object entity) {
        this.entityManager.persist(entity);
        this.entityManager.flush();
        this.entityManager.refresh(entity);
    }

    public List<Project> importMulFile(MultipartFile multipartFile) throws IOException {
        List<Project> listResults = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(multipartFile.getInputStream());
        XSSFSheet sheet = workbook.getSheetAt(0);
        int rows = sheet.getLastRowNum();
        for (int r = 1; r <= rows; r++) {
            //HANDLE DATA
            XSSFRow row = sheet.getRow(r);
            String cell0 = row.getCell(0).toString(); //user
            String cell1 = row.getCell(1).toString(); //project
            String cell2 = row.getCell(2).toString(); //task
            //HANDLE USER
            System.out.println("HANDLE USER");
            User currentUser;
            Optional<User> oldUser = userRepository.findById(row.getCell(0).getStringCellValue());
            if (oldUser.isEmpty()) {
                currentUser = User.builder() //create new user to save
                        .id(cell0)
                        .build();
                saveEntity(currentUser);
            } else {
                currentUser = oldUser.get();
            }
            System.out.println("HANDLE PROJECT");
            //HANDLE PROJECT
            Project currentProject;
            Optional<Project> oldProject = projectRepository.findByName(cell1);
            if (oldProject.isEmpty()) {
                currentProject = Project.builder()
                        .name(cell1)
                        .build();
                saveEntity(currentProject);
            } else {
                currentProject = oldProject.get();
            }

            System.out.println("HANDLE TASK");
            //HANDLE TASK
            Task currentTask;
            Optional<Task> oldTask = taskRepository.findByName(cell2);
            if (oldTask.isEmpty()) {
                currentTask = Task.builder()
                        .name(cell2)
                        .build();
                saveEntity(currentTask);
            } else {
                currentTask = oldTask.get();
            }

            System.out.println("HANDLE RELATIONSHIP");
            //HANDLE RELATIONSHIPS
            System.out.println("Project & User:");
            currentProject.saveUser(currentUser);
            System.out.println("Task & User:");
            currentTask.saveUser(currentUser);
            System.out.println("Task & Project:");
            currentTask.setProject(currentProject);
            //TRANSACTIONAL WILL UPDATE OUR ENTITIES :D
        }

        return null;
    }

}
