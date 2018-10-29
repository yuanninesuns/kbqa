package org.horizon.controller;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.apache.catalina.connector.Request;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sang on 2017/12/29.
 */
@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @RequestMapping(value = "/basic")
    public String basic() {
//        str.getRequest();
        System.out.println("14");
//        System.out.println("15" + str);
        return "basic";
    }
    @RequestMapping("/")
    public String  hello() {
        System.out.println("18");
        return "Hello";
    }
}
