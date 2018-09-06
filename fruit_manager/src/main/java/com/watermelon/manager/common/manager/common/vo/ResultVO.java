package com.watermelon.manager.common.manager.common.vo;

import java.util.List;

public class ResultVO {
	List<String> succ;
	List<String> fail;

	public List<String> getSucc() {
		return this.succ;
	}

	public void setSucc(List<String> succ) {
		this.succ = succ;
	}

	public List<String> getFail() {
		return this.fail;
	}

	public void setFail(List<String> fail) {
		this.fail = fail;
	}
}