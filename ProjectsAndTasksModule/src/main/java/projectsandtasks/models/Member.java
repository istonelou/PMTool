package projectsandtasks.models;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;

/**
 * Created by bake on 3/20/17.
 */
@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Member {
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private Long id;
  private Long userId;
  @ManyToOne
  @JoinColumn(name = "projectId")
  private Project project;

  protected Member() {
  }

  public Member(Long userId, Project project) {
    this.userId = userId;
    this.project = project;
  }
}
