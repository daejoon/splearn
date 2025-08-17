package com.ddoong2.learningtest.archunit.adapter;

import com.ddoong2.learningtest.archunit.application.MyService;
import com.ddoong2.learningtest.archunit.domain.MyMember;

public class MyAdapter {
    MyService myService;
    MyMember myMember;

    void run() {
        myService = new MyService();
        System.out.println(myService);
        myMember = new MyMember();
        System.out.println(myMember);
    }
}
