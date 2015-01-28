package com.linpeng.plugin.index;

import java.util.HashMap;
import java.util.Map;

import com.linpeng.micro.action.AbstractAction;
import com.linpeng.micro.action.Action;

@Action("index")
public class Index extends AbstractAction {

	public void index() {
		System.out.println("I am index method");

		Map<String, Object> model = new HashMap<String, Object>();
		render(model);
	}

}
