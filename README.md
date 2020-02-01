# 恐怖袭击事件危害评级系统
## 概述
对地震，交通事故，气象灾害等灾难性事件进行分级是社会管理的重要任务，也是实现对突发性事件快速响应和制订有效应对措施的基础，一般对灾难性事件分级采用主观方法，由权威机构或部门选择几个主要指标并强制执行评级标准。例如，《中国道路交通事故处理措施》第六条规定的交通事故分类标准主要基于伤亡结果和经济损失程度。但是，恐怖袭击的危害性不仅取决于伤亡和经济损失两个方面，还取决于时间、地理、目标对象等因素，难以通过使用上述分类方法形成统一标准，因此，通过主观与客观相互结合的方法，综合考虑恐怖袭击事件的各个评级指标，利用指标体系、数据挖掘技术以及管理信息系统分析与设计方法，实现恐怖袭击事件危害评级系统。
恐怖袭击事件危害评级系统利用指标体系和数据挖掘方法构建恐怖袭击数据危害评级模型，该模型建立在GTD数据集的基础上，经过数据预处理后，运用Delphi法、层次分析法和模糊综合评价法，构建恐怖袭击事件危害指标体系，并通过指标体系计算各事件的综合评分，然后通过K-Means聚类算法将事件划分出不同的等级，最后运用聚类模型对测试集进行评级并以可视化方式展现结果。集成了恐怖袭击风险评级模型和管理信息系统分析、设计，并根据管理信息系统的开发过程实现了恐怖袭击危害评级体系。该系统实现了通过调用Weka的应用程序接口的基本数据挖掘过程，基于Java Swing图形用户界面，后台使用MySQL数据库并使用MyBatis作为框架。

## 流程图 
将恐怖袭击事件数据集做为模型输入，然后对数据集进行数据预处理，主要包括过滤缺失数量超过阈值的属性和实例和不能被模型处理的数据类型，并通过相关性分析的方法去除一些冗余属性，然后在经过预处理的数据集基础上建立指标体系并计算综合分值，最后对评分结果进行聚类，并根据聚类结果的反馈来修正模型以达到最优效果

![alt 系统功能](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/%E7%A8%8B%E5%BA%8F%E6%B5%81%E7%A8%8B%E5%9B%BE.jpg)

## 系统功能模块
根据系统分析将系统划分为恐怖事件信息查询、恐怖事件信息编辑、恐怖事件信息添加、恐怖事件危害评级四个功能模块
![alt 系统功能](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/%E7%A8%8B%E5%BA%8F%E6%B5%81%E7%A8%8B%E5%9B%BE.jpg)
### 主界面
![alt main](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i1.png)
### 恐怖事件信息查询界面
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i2.png)
### 恐怖事件信息添加界面
![](https://github.com/dpoqb/Terrorist-Attack-Hazard-Rating-System/blob/master/img/i3.png)
### 恐怖信息编辑界面
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i4.png)
### 恐怖事件危害评级模型数据预处理界面
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i5.png)
### 属性信息
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i6.png)
### 指标体系处理界面
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i7.png)
### 聚类器及结果输出界面
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i8.png)
### 聚类器参数设置界面
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i9.png)
### 可视化
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i10.png)

## 恐怖袭击事件危害评级流程
### 数据准备以及预处理
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i11.png)
### 模型参数设置
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i12.png)
### 输出
#### 聚类结果
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i13.png)
#### 评级结果
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i14.png)
#### 每个等级包含的事件数
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i15.png)
#### 可视化
![](https://github.com/dpoqb/Terrorist_Attack_Hazard_Rating_System/blob/master/img/i16.png)
