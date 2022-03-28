package hu.vm.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveImage {

    private String states;
    private String startState;
    private String endState;
    private ArrayList<ArrayList<Integer>> heads;
    private ArrayList<String> contents;
    private String rules;
    private String signature;

}
