package com.cxy.redisclient.domain;

public class Container {
	private String container;
	
	public Container(String container) {
		this.container = container == null?"":container;
	}
	
	public boolean isKey() {
		char lastChar = container.charAt(container.length() - 1);
		
		if (lastChar == ':')
			return false;
		else
			return true;
	}
	
	public String getContainerOnly(){
		return getUpperContainer();
	}
	
	public String getKeyOnly() {
		assert(isKey());
		String[] containers = container.split(":");
		
		return containers[containers.length - 1];
	}
	
	public String getRelativeContainer(String beginContainer) {
		String[] beginContainers = beginContainer.split(":");
		String lastContainer = beginContainers[beginContainers.length-1] + ":";
		if(lastContainer.equals(":"))
			lastContainer = "";
		return container.replaceFirst(beginContainer, lastContainer);
	}
	
	public String getLastContainer() {
		String[] containers = container.split(":");
		if(!isKey()) {
			if(containers.length > 0)
				return containers[containers.length-1] + ":";
			else
				return "";
		} else {
			if(containers.length > 1)
				return containers[containers.length-2] + ":";
			else
				return "";
		}
		
	}
	
	public String getUpperContainer() {
		String[] containers = container.split(":");
		
		String upperContainer = "";
		
		for(int i = 0; i < containers.length - 1; i ++){
			upperContainer += containers[i] + ":";
		}
		
		return upperContainer;
	}
	
	public String appendLastContainer(String appendStr) {
		String target = "";
		String[] containers = container.split(":");
		containers[containers.length - 1] += appendStr;
		
		for(int i = 0; i < containers.length; i ++) {
			target += containers[i] + ":";
		}
		
		return target;
	}
}
