package com.alan.shiro.test.comm;

import java.util.List;

public class CommUtils {

    public static boolean isEmpty(Object o){
        if(o==null){
            return true;
        }
        if(o instanceof String){
            if (((String) o).length()==0) {
                return true;
            }
        }
        if(o instanceof List){
            if (((List) o).size()==0) {
                return true;
            }
        }
        return false;
    }
}
