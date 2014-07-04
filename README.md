# RedisClient


This is a redis client GUI tool written by Java SWT and Jedis. It's my objective to build the most convenient redis client GUI tool in the world. In the first place, it will facilitate in editing redis data, such as: add, update, delete, search, cut, copy, paste etc.

![My image](https://github.com/caoxinyu/RedisClient/raw/master/src/main/resources/screen.png)

--------

## Features

**Windows Explorer style UI**

**Multiple Redis version adaptive**

 1. Manage redis server
 2. Manage redis data favorite
 3. Manage redis data
 	* <i class="icon-file"></i>New redis data: string, list, hash, set, sorted set
 	* Rename redis data 
 	* <i class="icon-trash"></i>Delete redis data
 	* <i class="icon-edit"></i>Update redis data
 	* <i class="icon-paste"></i>Cut, copy paste redis data
 	* <i class="icon-cloud-download"></i>Import, export redis data
 	* <i class="icon-search"></i>Search redis data
 	* Order redis data by key, data type, size
 	* Navigation history


## Install & run

### For 64 bit windows
 1. Download the zip file RedisClient-master.zip from [here](https://github.com/caoxinyu/RedisClient/archive/master.zip)
 2. Extract the runable jar file **redisclient-win32.x86_64.1.0.jar** in directory /RedisClient-master/target from the zip file
 3. Run the redisclient-win32.x86_64.1.0.jar
 	* You can run it by double clicking it if your registry for jar file is configured correctly.
 	* Or you can run it from command line, and input `java -jar redisclient-win32.x86_64.1.0.jar`. Please pay attention to run it as administrator in windows 8.
 	
### For 32 bit windows
 1. Download the zip file RedisClient-master.zip from [here](https://github.com/caoxinyu/RedisClient/archive/master.zip)
 2. Extract the runable jar file **redisclient-win32.x86.1.0.jar** in directory /RedisClient-master/target from the zip file
 3. Run the redisclient-win32.x86.1.0.jar
 	* You can run it by double clicking it if your registry for jar file is configured correctly.
 	* Or you can run it from command line, and input `java -jar redisclient-win32.x86.1.0.jar`. 