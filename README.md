## oneCMDB导入本地maven库的流程.
1.打开cmd
2.将onecmdb-core-2.1.0.jar拷贝至E盘根目录或者其它盘(如放在其它盘,则下面的命令行中的-Difle后面应该跟上jar包所在的路径)
3.cmd输入:`mvn install:install-file -Dfile=E:\onecmdb-core-2.1.0.jar -DgroupId=org.onecmdb -DartifactId=onecmdb-core -Dversion=2.1.0 -Dpackaging=jar`
4.上面的maven命令只能修改-Dfile后面的jar路径!



