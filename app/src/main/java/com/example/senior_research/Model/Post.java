package com.example.senior_research.Model;

public class Post {
    private String postid;
    private String workout;
    private String notes;
    private String publisher;
    private String title;

    public Post(String postid, String title, String workout, String notes, String publisher){
        this.postid = postid;
        this.workout = workout;
        this.notes = notes;
        this.publisher = publisher;
        this.title = title;
    }
    public Post(){

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getWorkout() {
        return workout;
    }

    public void setWorkout(String workout) {
        this.workout = workout;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
