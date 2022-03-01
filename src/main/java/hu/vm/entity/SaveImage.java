package hu.vm.entity;

import lombok.*;

import java.util.ArrayList;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveImage {
    private String states;
    private String startState;
    private String endState;
    private ArrayList<ArrayList<Integer>> heads;
    private ArrayList<String> contents;
    private String rules;
    private String signature;
}
