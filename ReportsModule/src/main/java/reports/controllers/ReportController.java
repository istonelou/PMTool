package reports.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import reports.models.Report;
import reports.models.ReportDao;
import reports.models.ResponseModel;
import reports.models.UserModel;
import reports.models.UsersIds;
import reports.repository.TaskRepository;
import reports.repository.UsersRepository;
import reports.viewmodels.TaskModel;
import reports.viewmodels.UserModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Emina on 21.03.2017..
 */
@Controller
public class ReportController {

	
	static final Logger logger = LogManager.getLogger(ReportController.class.getName());
	
    @Autowired
    private TaskRepository repository;

    @Autowired
    private ReportDao reportDao;
    
    @Autowired
    private UsersRepository usersRepository;
    
    @RequestMapping(value = "user/{id}", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<UserModel> Get(@PathVariable("id") Long id){
        try {
            return new ResponseEntity<UserModel>(usersRepository.Get(id).getBody(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity(new ResponseModel("Error while fetching data"), HttpStatus.BAD_REQUEST);
    }
    
    @RequestMapping(value = "user/ids", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<List<UserModel>> Get(@RequestBody UsersIds model){
        try {
            return new ResponseEntity<List<UserModel>>(usersRepository.GetByIds(model).getBody(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ResponseEntity(new ResponseModel("Error while fetching data"), HttpStatus.BAD_REQUEST);
    }

    public Long getUserName(List<UserModel> sviUseri, Long id){
        for(UserModel user: sviUseri){
            if(user.getId() == id) return user.getId();
        }
        return null;
    }
    public ResponseEntity<List<reports.viewmodels.TaskModel>> tasksById(int id){
        List<TaskModel> tasks =(List<TaskModel>) repository.getFinishedTasks();
        ArrayList<TaskModel> taskoviZaUsera = new ArrayList<>();
        for (TaskModel task: tasks)
            if (task.getUserId()==id)
            {
                taskoviZaUsera.add(task);
            }
            return new ResponseEntity<List<TaskModel>>(taskoviZaUsera,HttpStatus.OK);

    }
    public ResponseEntity<List<reports.viewmodels.TaskModel>> tasksByDateAndMembers(int id, Date date){
        List<TaskModel> tasks =(List<TaskModel>) repository.getFinishedTasks();
        ArrayList<TaskModel> taskoviZaUsera = new ArrayList<>();
        for (TaskModel task: tasks)
            if (task.getUserId()==id && task.getFinishedOn()==date)
            {
                taskoviZaUsera.add(task);
            }
        return new ResponseEntity<List<TaskModel>>(taskoviZaUsera,HttpStatus.OK);

    }
    public ResponseEntity<List<reports.viewmodels.TaskModel>> tasksByDateWeight(int weight, Date date){
        List<TaskModel> tasks =(List<TaskModel>) repository.getFinishedTasks();
        ArrayList<TaskModel> taskovi = new ArrayList<>();

        for (TaskModel task: tasks)
            if (task.getWeightValue()==weight && task.getFinishedOn()==date)
            {
                taskovi.add(task);
            }
        return new ResponseEntity<List<TaskModel>>(taskovi,HttpStatus.OK);

    }


    @RequestMapping(value = "", method = RequestMethod.PATCH, produces = "application/json")
    public ResponseEntity<Report> create(@RequestBody Report report){
        String reportId="";
        try{
            reportDao.save(report);
            reportId=String.valueOf(report.getId());
        }
        catch(Exception e){
            return new ResponseEntity(new ResponseModel(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Report>(report, HttpStatus.OK);

    }

    @RequestMapping("/delete")
    @ResponseBody
    public String delete(long id) {
    try{
        Report report = new Report(id);
        reportDao.delete(report);
    }
    catch (Exception ex){
        return "Error deleting the report: " + ex.toString();
    }
    return "Report succesfully deleted!";
}


    @RequestMapping("/get-by-name")
    @ResponseBody
    public String getByName(String name) {
        String reportId="";
        try {
            List<Report>reports = reportDao.findByName(name);
            Report report=reports.get(0);
            reportId=String.valueOf(report.getId());
        }
        catch (Exception ex) {
            return "Report not found: "+ex;
        }
        return "The report id is: "+reportId;
    }


    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<Report> updateReport(@RequestBody Report report) {
        try {

            reportDao.save(report);
        }
        catch (Exception e) {
            return new ResponseEntity(new ResponseModel(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Report>(report,HttpStatus.OK);
    }


}

