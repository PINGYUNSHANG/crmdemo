package com.pys.crm.uuid;

import org.junit.Test;

import java.util.UUID;

public class UUIDTest {
    @Test
    public void test(){
        String s =UUID.randomUUID().toString().replaceAll("-","");
        System.out.println(s);
    }
}
