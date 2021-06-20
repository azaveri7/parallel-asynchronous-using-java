package com.learnjava.parallelstreams;

import com.learnjava.util.CommonUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.params.provider.ValueSource;

class ParallelStreamsExampleTest {

    @Test
    void stringTransform(){
        // given
        ParallelStreamsExample p = new ParallelStreamsExample();
        List<String> inputList = DataSet.namesList();

        // when
        CommonUtil.startTimer();
        List<String> resultList = p.stringTransform(inputList);
        CommonUtil.timeTaken();
        // then
        assertEquals(4, resultList.size());
        resultList.forEach(name -> {
            assertTrue(name.contains("-"));
        });
    }

    @Test
    @ValueSource(booleans = {false, true})
    void stringTransform_parallel(boolean isParallel){
        // given
        ParallelStreamsExample p = new ParallelStreamsExample();
        List<String> inputList = DataSet.namesList();

        // when
        CommonUtil.startTimer();
        List<String> resultList = p.stringTransform_parallel(inputList, isParallel);
        CommonUtil.timeTaken();
        // then
        assertEquals(4, resultList.size());
        resultList.forEach(name -> {
            assertTrue(name.contains("-"));
        });
    }
}