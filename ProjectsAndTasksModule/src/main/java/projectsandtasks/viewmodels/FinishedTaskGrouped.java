package projectsandtasks.viewmodels;

import java.util.Date;

/**
 * Created by Vejsil on 15.04.2017..
 */
public class FinishedTaskGrouped {

    private int taskStatusName;
    private String name;
    private String weightName;
    private int weightValue;
    private Date finishedOn;

    public int getTaskStatus() {
        return taskStatusName;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatusName = taskStatus;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getWeightValue() {
        return weightValue;
    }

    public void setWeightValue(int weightValue) {
        this.weightValue = weightValue;
    }

    public String getWeightName() {
        return weightName;
    }

    public void setWeightName(String weightName) {
        this.weightName = weightName;
    }

    public Date getFinishedOn() {
        return finishedOn;
    }

    public void setFinishedOn(Date finishedOn) {
        this.finishedOn = finishedOn;
    }

   public FinishedTaskGrouped(int taskStatusName, String name, Date finishedOn, String weightName, int weightValue){
        this.taskStatusName = taskStatusName;
        this.name = name;
        this.weightValue =weightValue;
        this.finishedOn = finishedOn;
        this.weightName=weightName;
    }
}
