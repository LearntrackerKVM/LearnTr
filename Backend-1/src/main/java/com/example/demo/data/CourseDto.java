package com.example.demo.data;

import java.util.List;

public class CourseDto {
    private String id;
    private String name;
    private String code;
    private List<TaskDto> tasks;

    // Constructor
    public CourseDto(String id, String name, String code, List<TaskDto> tasks) {
        this.id = id;
        this.name = name;
        this.tasks = tasks;
        this.code = code;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TaskDto> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDto> tasks) {
        this.tasks = tasks;
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}

