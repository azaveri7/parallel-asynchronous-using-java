package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;

import static com.learnjava.util.CommonUtil.*;
import static com.learnjava.util.LoggerUtil.log;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParallelStreamsExample {

    public List<String> stringTransform(List<String> nameList){
        return nameList.parallelStream().map(this::addNameLengthTransform)
                .collect(Collectors.toList());
        // if we run using stream, it will take 500 * 4 = 2000 ms
        // to execute
        /*return nameList.stream().map(this::addNameLengthTransform)
                .collect(Collectors.toList());*/
    }

    public List<String> stringTransform_parallel(List<String> nameList, boolean isParallel){
        Stream<String> nameStream = nameList.stream();

        if(isParallel)
            nameStream.parallel();
        // default behavior of streams
        /*else
            nameStream.sequential();*/

        return nameStream.map(this::addNameLengthTransform)
                .collect(Collectors.toList());
        // if we run using stream, it will take 500 * 4 = 2000 ms
        // to execute
        /*return nameList.stream().map(this::addNameLengthTransform)
                .collect(Collectors.toList());*/
    }

    public static void main(String[] args){
        List<String> nameList = DataSet.namesList();
        ParallelStreamsExample p = new ParallelStreamsExample();
        startTimer();
        List<String> resultList = p.stringTransform(nameList);
        log(resultList);
        timeTaken();
    }

    private String addNameLengthTransform(String name){
        delay(500);
        return name.length() + "-" + name;
    }

}
