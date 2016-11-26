# RedisClient


This is a redis client GUI tool written based on Java SWT and Jedis. It's my objective to build the most convenient redis client GUI tool in the world. In the first place, it will facilitate in editing redis data, such as: add, update, delete, search, cut, copy, paste etc.

![My image](https://github.com/caoxinyu/RedisClient/raw/windows/src/main/resources/screen.png)

--------

## Features

**Windows Explorer style UI**

**Multiple Redis version adaptive**

 1. Manage redis server, support server password authentication
 2. Manage redis data favorite
 3. Manage redis data
 	* New redis data: string, list, hash, set, sorted set
 	* Rename redis data 
 	* Delete redis data
 	* Update redis data
 	* Cut, copy paste redis data
 	* Import, export redis data
 	* Search redis data
 	* Order redis data by key, data type, size
 	* Navigation history
 	* Support time to live
 	* Support paging query redis data
 	* Support multiple selection to delete, cut, copy, export redis data
 	* Support flat view and hierarchy view to list redis data
 	* Support multiple language, now support English and Chinese
 	* Support run redis command in console
 	* Support run publish/subscribe
 	* Watch redis data for different format(plain text, json, xml, base64 image)


## Install & run for Windows
(For Linux, please switch [branch](https://github.com/caoxinyu/RedisClient/tree/linux). For Mac, please switch [branch](https://github.com/caoxinyu/RedisClient/tree/OSX))
### If you don't want to install JDK

1. Please download [redisclient-win32.x86.2.0.exe](https://raw.githubusercontent.com/caoxinyu/RedisClient/windows/release/redisclient-win32.x86.2.0.exe)
2. Double click redisclient-win32.x86.2.0.exe to install it to directory you choosed
3. Run it by double click redisclient-win32.x86.2.0.exe


### If you have installed the JDK or JRE 5+ 

#### For 64 bit windows
 1. Download the runable jar file [redisclient-win32.x86_64.2.0.jar](https://github.com/caoxinyu/RedisClient/blob/windows/release/redisclient-win32.x86_64.2.0.jar?raw=true)
 2. Run the redisclient-win32.x86_64.2.0.jar
 	* You can run it by double clicking it if your registry for jar file is configured correctly.
 	* Or you can run it from command line, and input `java -jar redisclient-win32.x86_64.2.0.jar`. Please pay attention to run it as administrator in windows 8.
 	
#### For 32 bit windows
 1. Download the runable jar file [redisclient-win32.x86.2.0.jar](https://github.com/caoxinyu/RedisClient/blob/windows/release/redisclient-win32.x86.2.0.jar?raw=true)
 2. Run the redisclient-win32.x86.2.0.jar
 	* You can run it by double clicking it if your registry for jar file is configured correctly.
 	* Or you can run it from command line, and input `java -jar redisclient-win32.x86.2.0.jar`. 

## Donate
 
If you find this software useful and would like to support it, you can do so simply by scanning my Alipay two-dimension code and donating whatever you like.

![My code](https://github.com/caoxinyu/RedisClient/raw/windows/src/main/resources/code.png)
 
