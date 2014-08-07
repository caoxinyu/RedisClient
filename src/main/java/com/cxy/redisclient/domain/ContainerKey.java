package com.cxy.redisclient.domain;

public class ContainerKey {
	private String containerKey;
	
	public ContainerKey(String containerKey) {
		this.containerKey = containerKey == null?"":containerKey;
	}
	
	public boolean isKey() {
		if(containerKey.length() == 0)
			return false;
		char lastChar = containerKey.charAt(containerKey.length() - 1);
		
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
		String[] containers = containerKey.split(":");
		
		return containers[containers.length - 1];
	}
	
	public String getRelativeContainer(String beginContainer) {
		String[] beginContainers = beginContainer.split(":");
		String lastContainer = beginContainers[beginContainers.length-1] + ":";
		if(lastContainer.equals(":"))
			lastContainer = "";
		return containerKey.replaceFirst(beginContainer, lastContainer);
	}
	
	public String getLastContainer() {
		String[] containers = containerKey.split(":");
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
		String[] containers = containerKey.split(":");
		
		String upperContainer = "";
		
		for(int i = 0; i < containers.length - 1; i ++){
			upperContainer += containers[i] + ":";
		}
		
		return upperContainer;
	}
	
	public String appendLastContainer(String appendStr) {
		String target = "";
		String[] containers = containerKey.split(":");
		containers[containers.length - 1] += appendStr;
		
		for(int i = 0; i < containers.length; i ++) {
			target += containers[i] + ":";
		}
		
		return target;
	}
	
	public String getContainerKey() {
		return containerKey;
	}
}
