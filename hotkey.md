类搜索     Ctrl+n

代码补全    Ctrl+Space

快速查询所有地方的某个类、方法、变量  Alt+F7

快速查看文档  Ctrl+Q

导航类、方法、变量的声明    Ctrl+B

快速重命名类、方法、变量    Shift+F6

重写方法    Ctrl+o

实现方法   Ctrl+I

SmartType代码     Ctrl+Shift+Space

getter和setter方法快速   Alert+Insert

Override/implement      Ctrl+o

重命名     Shift+F6


类继承视图   Ctrl+H      （常用）

Ctrl+Alt+Home  相关文件切换    （常用）

ctrl+alt+h      查看某个方法的调用路径

Alt+2       收藏

 Alt+1      Project

ctrl+f12  调出当前文件的大纲（File_Structure）

ctrl+shift+a  对于没有设置快捷键或者忘记快捷键的菜单或者动作（Action），可能通过输入其名字快速调用。神技！！！
              例如想要编译，只需要输入"release"，则列表框中就会出现"assembleRelease"选项，选择就可以进行编译。

alt+shift+up/down  上下移动行，这个没什么好说的，肯定会用到。

ctrl+y，ctrl+x, ctrl+d   删除行，删除并复制行，复制行并粘贴，必备。




##### 查询
全局文件查找  双击Shift

当前文件查询  Ctrl+F  （常用）

全局文件Path查询  Ctrl+Shfit+F    （常用）

拉出当前编辑窗口    Shfit+F4    （常用）

ctrl+shift+i        不离开当前文件当前类的情况下快速查看某个方法或者类的实现。通过大概预览下调用的方法，可以避免许多未知的坑。
## 代码片段抽取 方法抽取
Ctrl+Alt+M       （常用）


## 代码管理控制
- `Alt+``(是1左边的那个键)
此快捷键会显示一个版本管理常用的一个命令，可以通过命令前面的数字或者模糊匹配来快速选择命令。
极大的提高了工作效率，快速提交代码、暂存代码、切分支等操作操作如鱼得水。

## Hide All Panels
- ctrl+shift+f12
关闭或者恢复其他窗口。在编写代码的时候非常方便的全屏编辑框，可以更加专心的coding...

## Parameter Info
- ctrl+p
在调用一些方法的时候免不了会忘记或者不知道此方法需要哪些参数。ctrl+p可以显示出此方法需要的参数。必备技能之一

## 条件断点
- 通过右键断点，可以对一个断点加入条件。只有当满足条件时，才会进入到断点中。调试神技，只对自己关心的情况进行调试，不浪费时间。

## 迅速查看变量值
- 按住Alt点击想要查看的变量或者语句。如果想查看更多，则可以按Alt+f8调出Evaluate Expression窗口来自行输入自定义的语句。

## 分析堆栈信息
Find Actions(ctrl+shift+a)输入"analyze stacktrace"即可查看堆栈信息。

## 分析某个值的来源
Find Actions(ctrl+shift+a)输入"Analyze Data Flow to Here"，可以查看某个变量某个参数其值是如何一路赋值过来的。
对于分析代码非常有用。

## 列编辑
在vim中叫作块编辑，同样神技！使用方法：按住Alt加鼠标左键拉框即可
PS：发现Ubuntu下不可用，代替方法为按Alt+Shift+Insert之后拖框选择。
但是经过这么操作之后，神技就大打折扣了。估计是与Ubuntu的快捷键冲突了。
