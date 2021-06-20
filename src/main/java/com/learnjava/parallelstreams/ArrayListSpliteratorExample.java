package com.learnjava.parallelstreams;

import com.learnjava.util.CommonUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListSpliteratorExample {

    public List<Integer> multiplyEachValue(ArrayList<Integer> input
            ,int multiplyFactor, boolean isParallel){
        CommonUtil.stopWatchReset();
        CommonUtil.startTimer();
        Stream<Integer> inputIntStream = input.stream();

        if(isParallel)
            inputIntStream.parallel();

        List<Integer> resultList = inputIntStream.map(integer ->
                integer * multiplyFactor)
                .collect(Collectors.toList());
        CommonUtil.timeTaken();
        return resultList;
    }
}
