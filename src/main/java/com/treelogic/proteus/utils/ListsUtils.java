package com.treelogic.proteus.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ignacio.g.fernandez on 9/05/17.
 */
public class ListsUtils {


    public static <T> List<T> copy (List<T> list){
        return new ArrayList<T>(list);
    }
}
