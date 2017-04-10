package com.cross.cxf;

import javax.jws.WebService;

@WebService
public interface HelloWorld {

	public String say(String str);
}
