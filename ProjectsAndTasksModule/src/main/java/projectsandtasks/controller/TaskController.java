package projectsandtasks.controller;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import projectsandtasks.models.UserModel;
import projectsandtasks.models.WeightEnum;
import projectsandtasks.helpers.ResponseModel;
import projectsandtasks.models.Comment;
import projectsandtasks.models.Project;
import projectsandtasks.models.Task;
import projectsandtasks.models.TaskStatusEnum;
import projectsandtasks.repository.CommentRepository;
import projectsandtasks.repository.TaskRepository;
import projectsandtasks.repository.UsersRepository;
import projectsandtasks.viewmodels.FinishedTask;
import projectsandtasks.viewmodels.FinishedTaskGrouped;
import projectsandtasks.viewmodels.FinishedTaskGroupedTotal;
import projectsandtasks.viewmodels.TaskModel;
import projectsandtasks.viewmodels.UsersIds;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vejsil on 28.03.2017..
 */
@Controller
@RestController
@RequestMapping(value = "/task", produces = "application/json")
public class TaskController {

    @Autowired
    private TaskRepository repository;
    @Autowired
    private UsersRepository uRepository;
    @Autowired
    private CommentRepository cRepository;
    
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Task> Insert(@RequestBody Task task) {
        if (task == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        try {
            repository.save(task);
        } catch (Exception e) {
            return new ResponseEntity(new ResponseModel(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Task>(task, HttpStatus.OK);
    }
    
    @RequestMapping(value="/comment", method = RequestMethod.POST)
    public ResponseEntity<Comment> Insert(@RequestBody Comment comment) {
    	System.out.println("TUUUU SAAAAM");
    	if(comment == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
    	try {
    		cRepository.save(comment);

	    } catch (Exception e) {
	        return new ResponseEntity(new ResponseModel(e.getMessage()), HttpStatus.BAD_REQUEST);
	    }
    	return new ResponseEntity<Comment>(comment, HttpStatus.OK);
    }
	@RequestMapping(value = "/deletecomment/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseModel> DeleteComment(@PathVariable Long id) {
    	List<Comment> allcomments = cRepository.findAll();
    	List<Comment> comments=new ArrayList<>();
    	for (Comment c:allcomments)
		{
			if (c.getTask().getId()==id)
			{
				comments.add(c);
			}
		}
    	if (comments==null) return new ResponseEntity<ResponseModel>(new ResponseModel("Something went wrong"), HttpStatus.BAD_REQUEST);
		try {
			System.out.println("SIZEEE" + comments.size());
			for (Comment c:comments)
			{
				System.out.println("TUUUUUUUUUUUUUUUU");
				cRepository.delete(c);
			}
		} catch (Exception e) {
			return new ResponseEntity<ResponseModel>(new ResponseModel(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<ResponseModel>(new ResponseModel("OK"), HttpStatus.OK);
	}
    
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Task> Update(@PathVariable Long id,@RequestBody TaskModel model) {
    	Task task = repository.findById(id).get(0);
        if (task == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        try {
        	task.setName(model.getName());
        	task.setDescription(model.getDescription());
        	task.setStartedOn(model.getStartedOn());
        	task.setEndOn(model.getEndOn());
        	task.setTaskStatus(model.getTaskStatus());
        	task.setWeight(model.getWeight());
        	if(model.getTaskStatus() == TaskStatusEnum.DONE) task.setFinishedOn(new Date());
        	else task.setFinishedOn(null);
            repository.save(task);
        } catch (Exception e) {
            return new ResponseEntity(new ResponseModel(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Task>(task, HttpStatus.OK);
    }
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<ResponseModel> Update(@PathVariable Long id) {
    	Task task = repository.findById(id).get(0);
        if (task == null) return new ResponseEntity<ResponseModel>(new ResponseModel("Something went wrong"), HttpStatus.BAD_REQUEST);
        try {
            repository.delete(id);
        } catch (Exception e) {
            return new ResponseEntity<ResponseModel>(new ResponseModel(e.getMessage()), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<ResponseModel>(new ResponseModel("OK"), HttpStatus.OK);
    }

	@RequestMapping(value = "/finished", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<List<projectsandtasks.viewmodels.FinishedTask>> finishedTasks(@RequestParam(value="projectId") Long projectId) {
		List<projectsandtasks.models.Task> tasks = new ArrayList<>();
		repository.findAll().stream().filter(x -> {return (x.getProject().getId() == projectId && x.getFinishedOn() != null);}).map(x -> new projectsandtasks.models.Task(x))
				.forEach(x -> tasks.add(x));
    	ArrayList<FinishedTask> finishedTasks = new ArrayList<FinishedTask>();
    	ArrayList<Long> idovi = new ArrayList<Long>();
    	for(projectsandtasks.models.Task task: tasks)
    		idovi.add(task.getId());
    	UsersIds userIDs = new UsersIds();
        userIDs.setIds(idovi);
    	List<UserModel> sviUseri = uRepository.GetByIds(userIDs).getBody();
    	for(projectsandtasks.models.Task task: tasks){
    		finishedTasks.add(new FinishedTask(task.getId(), this.getUserName(sviUseri, task.getId()), task.getName(), task.getFinishedOn()));
    	}
    	return new ResponseEntity<List<projectsandtasks.viewmodels.FinishedTask>>(finishedTasks, HttpStatus.OK);
	}

	@RequestMapping(value = "/finished/grouped", method = RequestMethod.GET, produces = "application/json")
	public ResponseEntity<FinishedTaskGroupedTotal> finishedTasksGroupedBy (@RequestParam(value="taskStatus") Long taskStatus){
		Date dateBefore30Days = DateUtils.addDays(new Date(),-30); //day 30 days ago
		ArrayList<FinishedTaskGrouped> finishedTasksGroupedList = new ArrayList<FinishedTaskGrouped>();
    	repository.findAll().stream().filter(x -> {return (x.getTaskStatus().getValue() == taskStatus && x.getFinishedOn() != null && x.getFinishedOn().compareTo(dateBefore30Days) > 0);}).map(x -> new FinishedTaskGrouped(x.getTaskStatus().getValue(),x.getName(),x.getFinishedOn(),x.getWeight().toString(),x.getWeight().getValue())).forEach((x -> finishedTasksGroupedList.add(x)));
    	int totalWeight = 0;
    	for(FinishedTaskGrouped ftG: finishedTasksGroupedList){
    		totalWeight += ftG.getWeightValue();
		}
		FinishedTaskGroupedTotal finishedTaskGroupedTotal = new FinishedTaskGroupedTotal(finishedTasksGroupedList, totalWeight);
    	return new ResponseEntity<FinishedTaskGroupedTotal>(finishedTaskGroupedTotal, HttpStatus.OK);
	}

	private String getUserName(List<UserModel> sviUseri, Long id){
    	for(UserModel user: sviUseri){
    		if(user.getId() == id) return user.getName();
    	}
		return null;
    }

}
