# ktor-saveps-service

### WAR打包
    ./gradlew :war

### Mac下上传文件到Linux
    scp 本地文件路径 root@119.91.136.187:远程文件路径

    scp ./Desktop/ktorpigletservice.war root@119.91.136.187:/usr/local/tomcat/apache-tomcat-10.1.9/webapps


### 手动搭建Java Web环境（ CentOS 7.6为例）
#### 一、登录Linux实例：ssh root@ip
    ssh root@119.91.136.187
#### 二、安装JDK（[jdk-17_linux-x64_bin.tar.gz](software%2Fjdk-17_linux-x64_bin.tar.gz)）
    1.新建JDK安装目录：mkdir /usr/java

    2.解压文件：tar xzf jdk-17_linux-x64_bin.tar.gz -C /usr/java

    3.环境变量配置：vim /etc/profile
        export JAVA_HOME=/usr/java/jdk-17.0.7
        export CLASSPATH=$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dt.jar:$JAVA_HOME/lib
        export PATH=$JAVA_HOME/bin:$PATH
    
    4、生效：source /etc/profile

    5、验证：java -version

### 三、安装Tomcat（[apache-tomcat-10.1.9.tar.gz](software%2Fapache-tomcat-10.1.9.tar.gz)）
    
    1.下载：wget https://dlcdn.apache.org/tomcat/tomcat-10/v10.1.9/bin/apache-tomcat-10.1.9.tar.gz
        或者scp上传

    2.解压：tar xzf apache-tomcat-10.1.9.tar.gz

    3.将内容移动到 /use/local/tomcat 下：mv apache-tomcat-10.1.9 /usr/local/tomcat/

    4.打开server.xml并修改appBase：
        vim /usr/local/tomcat/conf/server.xml
        找到<Host ... appBase="webapps">并修改 appBase="/usr/local/tomcat/webapps"

    5.新建setenv.sh，配置参数
        vi /usr/local/tomcat/bin/setenv.sh
        JAVA_OPTS='-Djava.security.egd=file:/dev/./urandom -server -Xms256m -Xmx496m -Dfile.encoding=UTF-8' 

    6.启动Tomcat：/usr/local/tomcat/bin/startup.sh

    7.验证配置：
        echo Hello World! > /usr/local/tomcat/webapps/ROOT/index.jsp
        http://119.91.136.187:8080

        
        
