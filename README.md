# dynamic-evaluation-system
算法文件示例在 [getScore.py](springboot_01_02_quickstart/src/main/resources/bin/getScore.py)中\
其中[data.json](springboot_01_02_quickstart/target/classes/bin/data.json)是系统传给算法处理程序的数据\
[postScore.json](springboot_01_02_quickstart/target/classes/bin/postScore.json)和[userScore.json](springboot_01_02_quickstart/target/classes/bin/userScore.json)是系统要求算法得到的结果\
使用pyinstaller生成exe文件如果报错可以使用以下命令行:\
``
pyinstaller getScore.py --onefile
``\
注：生成的exe文件名一定为[getScore.exe](springboot_01_02_quickstart/src/main/resources/bin/getScore.exe),且放在该路径下。
