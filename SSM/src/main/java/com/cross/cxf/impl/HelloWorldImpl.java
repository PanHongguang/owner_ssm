package com.cross.cxf.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.cross.cxf.HelloWorld;

@Component(value="helloWorld")
@WebService(endpointInterface="com.cross.cxf.HelloWorld")
public class HelloWorldImpl implements HelloWorld {

	@Override
	public String say(String str) {
		return "hello:"+str;
	}

}
