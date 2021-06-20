package com.learnjava.parallelstreams;

import com.learnjava.util.DataSet;
import org.junit.jupiter.api.RepeatedTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArrayListSpliteratorExampleTest {

    ArrayListSpliteratorExample a = new ArrayListSpliteratorExample();

    @RepeatedTest(5)
    void multiplyEachValue_sequential() {
        // given
        int size = 1000000;
        ArrayList<Integer> input = DataSet.generateArrayList(size);

        // when
        List<Integer> resultList = a.multiplyEachValue(input, 2, false);
        // then
        assertEquals(size, resultList.size());
    }

    @RepeatedTest(5)
    void multiplyEachValue_parallel() {
        // given
        int size = 1000000;
        ArrayList<Integer> input = DataSet.generateArrayList(size);

        // when
        List<Integer> resultList = a.multiplyEachValue(input, 2, true);
        // then
        assertEquals(size, resultList.size());
    }
}