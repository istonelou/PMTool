package reports.repository;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import reports.FeignConfiguration;
import reports.viewmodels.FinishedTask;
import reports.viewmodels.FinishedTaskGroupedTotal;

import java.util.List;

/**
 * Created by Emina on 14.04.2017..
 */

@FeignClient(name="pt-module", configuration=FeignConfiguration.class)
public interface TaskRepository {
    @RequestMapping("/tasks")
    public ResponseEntity<List<FinishedTask>> getFinishedTasks();

    @RequestMapping(value = "/task/numberoftasks", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> finishedTasksGroupedBy(@RequestParam(value = "projectId") Long projectId, @RequestParam(value = "userId") Long userId);

    @RequestMapping(value = "/task/finished", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<List<FinishedTask>> finishedTasks(@RequestParam(value = "projectId") Long projectId);

    @RequestMapping(value = "/task/finished/grouped", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<FinishedTaskGroupedTotal> finishedTasksGroupedBy (@RequestParam(value="taskStatus") Long taskStatus);


    }