package com.learnjava.parallelstreams;

import com.learnjava.util.CommonUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LinkedListSpliteratorExample {

    public List<Integer> multiplyEachValue(LinkedList<Integer> input
            , int multiplyFactor, boolean isParallel){
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
