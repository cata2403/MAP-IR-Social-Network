package com.ubb.domain_layer.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Message extends Entity<Long>{

    private User from;
    private final List<User> to = new LinkedList<User>();
    private String message;
    private LocalDateTime date =  LocalDateTime.now();
    private Message reply = null;

    public Message(Long id, String message) {
        super(id);
        this.message = message;
    }

    public Message getReply() {
        return reply;
    }

    public void setReply(Message reply) {
        this.reply = reply;
    }

    public void setSender(User from) {
        this.from = from;
    }
    public void addReceiver(User to) {
        this.to.add(to);
    }

    public User getSender(){
        return this.from;
    }

    public List<User> getReceiver(){
        return this.to;
    }

    public String getMessage(){
        return this.message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public LocalDateTime getDate(){
        return this.date;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }

    @Override
    public String toString(){
        String forReply = "";
        if(reply != null)
            forReply = "[Replied to]" + reply.getMessage().substring(0, Math.min(10, reply.getMessage().length())) + " ...";
        return forReply + "\n" +from.getUsername() + " : " + message + "\n" + "[" + date.toString() + "]";
    }
}

