<!DOCTYPE html>
<html>
<head>
    <meta charset="utf‐8">
    <title>Hello World!</title>
</head>
<body>
Hello ${name}!

<br/>
1. 遍历数据模型中list中学生的信息(数据模型中的名称为strus)
<table>
    <tr>
        <td>序号</td>
        <td>姓名</td>
        <td>年龄</td>
        <td>金额</td>
        <td>出生日期</td>
    </tr>
    <#--list标签，对应于java中的list-->
    <#list stus as stu>
        <tr>
            <td>${stu_index+1}</td>     <#--${stu_index+1}这个可以解决序号问题-->
            <td>${stu.name}</td>
            <td>${stu.age}</td>
            <td>${stu.money}</td>
<#--            <td>${stu.birthday}</td>-->
        </tr>
    </#list>
</table>
<br/>
2.遍历数据模型中的stuMap(map数据)，第一种方法：在中括号中填写map的key,点跟上对象属性
                              第二种方法：在map后面直接加“点key”,点跟上对象属性
<br/>
姓名：${stuMap['stu1'].name}<br/>
年龄：${stuMap['stu1'].age}<br/>
金额：${stuMap.stu1.money}<br/>

3.遍历map中的key,keys或者k就是一个列表。然后拿到key的属性
<br/>
<#list stuMap?keys as k>
    姓名：${stuMap[k].name}<br/>
    年龄：${stuMap[k].age}<br/>
</#list>
</body>
</html>
<br/>
4.ftl的if应用
<#list stus as stu>
    <table>
        <tr>
            <td>${stu_index+1}</td>     <#--${stu_index+1}这个可以解决序号问题-->
            <td <#if stu.name=="小明">style="background:red;"</#if>>${stu.name}</td>
            <td <#if stu.age gt 18>style="background:#88ff67;"</#if>>${stu.age}</td>
            <td>${stu.money}</td>
            <td>${stu.birthday?date}</td>
        </tr>
    </table>
</#list>

5.ftl的if的空值处理,stus??表示判断此值是否为空
<#if stus1??>
    <#list stus1 as stu>
        <table>
            <tr>
                <td>${stu_index+1}</td>     <#--${stu_index+1}这个可以解决序号问题-->
                <td <#if stu.name=="小明">style="background:red;"</#if>>${stu.name}</td>
                <td <#if stu.age gt 18>style="background:#88ff67;"</#if>>${stu.age}</td>
                <td>${stu.money}</td>
                <#--            <td>${stu.birthday}</td>-->
            </tr>
        </table>
    </#list>
</#if>

6.if缺省值的应用,表示如果map这里面某个值是缺省的,则就显示为空""，而if那个是先判读有没用这个list
姓名：${(stuMap['stu1'].name)!""}<br/>
年龄：${stuMap['stu1'].age}<br/>
金额：${stuMap.stu1.money}<br/>

6.内建函数
(1)内建函数之size的应用
map大小：${(stuMap)?size}<br/>
list大小${stus?size}<br/>
(2)日期格式
<#--显示年月日: ${today?date}-->
<#--显示时分秒：${today?time}-->
<#--显示日期+时间：${today?datetime} <br>-->
<#--自定义格式化： ${today?string("yyyy年MM月")}-->
<#list stus as stu>
    <table>
        <tr>
            <td>${stu.birthday?date}</td>
        </tr>
    </table>
</#list>
(3) c，将数字完整呈现<br/>
${num}
${num?c}

(4)将json字符串转成对象,assign的作用是定义一个变量
<#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
<#assign data=text?eval />
开户行：${data.bank} 账号：${data.account}


































