#auto-dao


`auto-dao` is a simple, scalable framework that database access .

##Quick Start

<a href='https://github.com/liaokailin/auto-dao-sample'>more detail</a> ;


##  Configuration

`auto-dao` dependent on spring framework

1. basic spring config  <a href='https://github.com/liaokailin/auto-dao/blob/master/src/main/java/com/enniu/cloud/common/po/config/ContextConfig.java'>detail</a> ;
  `DataSource` 、`JdbcTemplate` 、`DataSourceTransactionManager`

2. auto-dao config
 	
 * The parse of entity-table-relation,default is <a href='https://github.com/liaokailin/auto-dao/blob/master/src/main/java/com/enniu/cloud/common/po/parse/HumpUnderLineParse.java'> HumpUnderLineParse</a> ;
 
 * The detail operator of db,default is <a href='https://github.com/liaokailin/auto-dao/blob/master/src/main/java/com/enniu/cloud/common/po/operator/MySqlJdbcTemplateOperator.java'>MySqlJdbcTemplateOperator</a> ;
 
 * The type of db,default is <a href='https://github.com/liaokailin/auto-dao/blob/master/src/main/java/com/enniu/cloud/common/po/mysql/Mysql.java'>Mysql</a> ;
  
`auto-dao `create auto-config by spring boot ;  <a href='https://github.com/liaokailin/auto-dao/blob/master/src/main/java/com/enniu/cloud/common/po/auto/POFactoryAutoConfig.java'>detail</a>

 

##Expand

default `auto-dao` support access mysql by wrapper JdbcTemplate ，entity-table-relation is  hump-underline(class CusUser ;table T_Cus_User) ;


Expand need to complete two steps：
1. implements `Operator`
2. implements `Parse`


more about spring boot is <a href='http://blog.csdn.net/liaokailin/article/category/5765237'>here</a>




 

