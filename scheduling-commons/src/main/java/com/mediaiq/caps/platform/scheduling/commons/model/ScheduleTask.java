package com.mediaiq.caps.platform.scheduling.commons.model;

import java.time.ZonedDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;

import lombok.Builder;
import lombok.NoArgsConstructor;

/**
 * ScheduleTask
 */
@TypeDef(name = "jsonb-node",
         typeClass = JsonNodeBinaryType.class)
@TypeDef(name = "jsonb",
         typeClass = JsonBinaryType.class)
@Entity
@Table(name = "schedule_task_info",
       uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "groupname"},
                                              name = "uk_name_not_unique")})
@Validated
@NoArgsConstructor
public class ScheduleTask {
  @JsonProperty("id")
  @Id
  @GeneratedValue(generator = "uuid")
  @GenericGenerator(name = "uuid",
                    strategy = "uuid2")
  @Column(name = "id",
          unique = true)
  private String id = null;

  @NotBlank
  @NotNull
  @JsonProperty("name")
  @Column(name = "name")
  private String name = null;

  @NotBlank
  @NotNull
  @JsonProperty("group")
  @Column(name = "groupname")
  private String group;

  @NotBlank
  @NotNull
  @JsonProperty("description")
  @Column(name = "description")
  private String description = null;

  @NotBlank
  @NotNull
  @JsonProperty("creator")
  @Column(name = "creator")
  private String creator = null;

  @JsonProperty("status")
  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private StatusEnum status = StatusEnum.ACTIVE;

  @JsonProperty("created")
  @Column(name = "created",
          columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime created = null;

  @JsonProperty("updated")
  @Column(name = "updated",
          columnDefinition = "TIMESTAMP WITH TIME ZONE")
  private ZonedDateTime updated = null;

  @JsonProperty("lastExecution")
  @Transient
  private ZonedDateTime lastExecution = null;

  @JsonProperty("nextExecution")
  @Transient
  private ZonedDateTime nextExecution = null;

  @NotNull
  @Type(type = "jsonb")
  @JsonProperty("trigger")
  @Column(name = "trigger",
          columnDefinition = "jsonb")
  private Trigger trigger = null;

  @NotNull
  @Type(type = "jsonb")
  @JsonProperty("executor")
  @Column(name = "executor",
          columnDefinition = "jsonb")
  private Executor executor = null;

  @JsonProperty("zoneID")
  @Column(name = "zoneID")
  private String zoneID = null;

  @Builder(toBuilder = true)
  private ScheduleTask(@NotNull String name, @NotNull String description, @NotNull String creator,
      StatusEnum status, @NotNull Trigger trigger, @NotNull Executor executor,
      @NotNull String group) {
    this.name = name;
    this.description = description;
    this.creator = creator;
    if (status != null) {
      this.status = status;
    }
    this.trigger = trigger;
    this.executor = executor;
    this.group = group;
  }

  public String getZoneID() {
    return zoneID;
  }

  public void setZoneID(String zoneID) {
    this.zoneID = zoneID;
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
  }

  /**
   * Get id
   *
   * @return id
   **/


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  /**
   * Get name
   *
   * @return name
   **/


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get description
   *
   * @return description
   **/


  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Get creator
   *
   * @return creator
   **/

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  /**
   * Get status
   *
   * @return status
   **/


  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(StatusEnum status) {
    this.status = status;
  }

  /**
   * Get created
   *
   * @return created
   **/


  @Valid
  public ZonedDateTime getCreated() {
    return created;
  }

  public void setCreated(ZonedDateTime created) {
    this.created = created;
  }

  /**
   * Get updated
   *
   * @return updated
   **/


  @Valid
  public ZonedDateTime getUpdated() {
    return updated;
  }

  public void setUpdated(ZonedDateTime updated) {
    this.updated = updated;
  }

  /**
   * Get lastExecution
   *
   * @return lastExecution
   **/


  @Valid
  public ZonedDateTime getLastExecution() {
    return lastExecution;
  }

  public void setLastExecution(ZonedDateTime lastExecution) {
    this.lastExecution = lastExecution;
  }

  /**
   * Get nextExecution
   *
   * @return nextExecution
   **/


  @Valid
  public ZonedDateTime getNextExecution() {
    return nextExecution;
  }

  public void setNextExecution(ZonedDateTime nextExecution) {
    this.nextExecution = nextExecution;
  }

  /**
   * Get trigger
   *
   * @return trigger
   **/


  @Valid
  public Trigger getTrigger() {
    return trigger;
  }

  public void setTrigger(Trigger trigger) {
    this.trigger = trigger;
  }

  /**
   * Get executor
   *
   * @return executor
   **/


  @Valid
  public Executor getExecutor() {
    return executor;
  }

  public void setExecutor(Executor executor) {
    this.executor = executor;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScheduleTask scheduleTask = (ScheduleTask) o;
    return Objects.equals(this.id, scheduleTask.id) && Objects.equals(this.name, scheduleTask.name)
        && Objects.equals(this.group, scheduleTask.group) && Objects.equals(this.description,
        scheduleTask.description) && Objects.equals(this.creator, scheduleTask.creator)
        && Objects.equals(this.status, scheduleTask.status) && Objects.equals(this.created,
        scheduleTask.created) && Objects.equals(this.updated, scheduleTask.updated)
        && Objects.equals(this.lastExecution, scheduleTask.lastExecution) && Objects.equals(
        this.nextExecution, scheduleTask.nextExecution) && Objects.equals(this.trigger,
        scheduleTask.trigger) && Objects.equals(this.executor, scheduleTask.executor)
        && Objects.equals(this.zoneID, scheduleTask.zoneID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, group, description, creator, status, created, updated,
        lastExecution, nextExecution, trigger, executor, zoneID);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ScheduleTask {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    group: ").append(toIndentedString(group)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    creator: ").append(toIndentedString(creator)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    created: ").append(toIndentedString(created)).append("\n");
    sb.append("    updated: ").append(toIndentedString(updated)).append("\n");
    sb.append("    lastExecution: ").append(toIndentedString(lastExecution)).append("\n");
    sb.append("    nextExecution: ").append(toIndentedString(nextExecution)).append("\n");
    sb.append("    trigger: ").append(toIndentedString(trigger)).append("\n");
    sb.append("    executor: ").append(toIndentedString(executor)).append("\n");
    sb.append("    zoneId: ").append(toIndentedString(zoneID)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first
   * line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  /**
   * Gets or Sets status
   */
  public enum StatusEnum {
    ACTIVE("ACTIVE"),
    INACTIVE("INACTIVE");

    private String value;

    StatusEnum(String value) {
      this.value = value;
    }

    @JsonCreator
    public static StatusEnum fromValue(String text) {
      for (StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      throw new IllegalArgumentException("No enum constant for " + text + " found");
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }
  }
}
