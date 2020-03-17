### vagrant操作  
参考   
https://www.cnblogs.com/liaosp/p/11611563.html   
https://www.vagrantup.com/docs/vagrantfile/   

> 下载对应操作系统的vagrant-box
>
>1. 到[vagrant-box官网](https://app.vagrantup.com/boxes/search)或者命令行输入vagrant box list寻找需要的box   
>2. 命令行输入vagrant init \[box-name\]   
>3. 输入vagrant-up直接启动镜像
> (如果不存在上述box-name则shell会自动下载，若下载较慢的话可以在命令行中复制下载链接到迅雷下载。切记，第二条命令的box-name否则shell会重新下载同名的box)
```shell script
$ vagrant box list #查看现有的box
$ vagrant init [box-name]
$ vagrant plugin install vagrant-vbguest
$ vagrant up
```
------------------
> 修改现有box的参数
>
>1.打开Vagrantfile   
>2.[修改参数](https://blog.csdn.net/u011781521/article/details/80291765)   
---------------------

> 安装docker   
> ---[国内镜像源](https://www.cnblogs.com/X-knight/p/10598498.html)   
> ---[docker-ce安装问题](https://blog.csdn.net/weixin_30512089/article/details/95421017)   
> [centos安装docker](https://www.cnblogs.com/lianstyle/p/10434890.html)
>将vagrant添加到docker组，之后在本地重启vagrant
```shell script
$ sudo groupadd docker
$ sudo gpasswd -a vagrant docker
$ sudo service docker restart
```

-------------------------
> 安装docker-compose   
> 参考 https://my.oschina.net/thinwonton/blog/2985886