package com.ddoong2.learningtest.archunit.application;

import com.ddoong2.learningtest.archunit.domain.MyMember;

public class MyService {
    MyMember myMember;

    void run() {
        myMember = new MyMember();
        System.out.println(myMember);
    }
}
