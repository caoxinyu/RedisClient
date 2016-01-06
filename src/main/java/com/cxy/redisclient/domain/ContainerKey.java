package com.cxy.redisclient.domain;

import com.cxy.redisclient.integration.ConfigFile;

public class ContainerKey {
	private String containerKey;
	private final String separator = ConfigFile.getSeparator();
	
	public ContainerKey(String containerKey) {
		this.containerKey = containerKey == null?"":containerKey;
	}
	
	public boolean isKey() {
		if(containerKey.length() == 0)
			return false;
		
		if (containerKey.endsWith(separator))
			return false;
		else
			return true;
	}
	
	public String getContainerOnly(){
		return getUpperContainer();
	}
	
	public String getKeyOnly() {
		assert(isKey());
		String[] containers = containerKey.split(separator);
		
		return containers[containers.length - 1];
	}
	
	public String getRelativeContainer(String beginContainer) {
		String[] beginContainers = beginContainer.split(separator);
		String lastContainer = beginContainers[beginContainers.length-1] + separator;
		if(lastContainer.equals(separator))
			lastContainer = "";
		return containerKey.replaceFirst(beginContainer, lastContainer);
	}
	
	public String getLastContainer() {
		String[] containers = containerKey.split(separator);
		if(!isKey()) {
			if(containers.length > 0)
				return containers[containers.length-1] + separator;
			else
				return "";
		} else {
			if(containers.length > 1)
				return containers[containers.length-2] + separator;
			else
				return "";
		}
		
	}
	
	public String getUpperContainer() {
		String[] containers = containerKey.split(separator);
		
		String upperContainer = "";
		
		for(int i = 0; i < containers.length - 1; i ++){
			upperContainer += containers[i] + separator;
		}
		
		return upperContainer;
	}
	
	public String appendLastContainer(String appendStr) {
		String target = "";
		String[] containers = containerKey.split(separator);
		containers[containers.length - 1] += appendStr;
		
		for(int i = 0; i < containers.length; i ++) {
			target += containers[i] + separator;
		}
		
		return target;
	}
	
	public String getContainerKey() {
		return containerKey;
	}
}
