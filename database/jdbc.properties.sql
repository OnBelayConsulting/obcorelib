# Properties file with JDBC-related settings.
spring.datasource.driverClassName=com.microsoft.sqlserver.jdbc.SQLServerDriver

# Change the jdbc.url property to the database URL where your schema exists!
jdbc.url=jdbc:sqlserver://localhost:1433;databaseName=obcore;trustServerCertificate=true

# Change the username, password and connectString properties to your specific schema details:
# Select one of the schemas from the VM Environment.
jdbc.username=obcore
jdbc.password=obcore

# Property that determines the Hibernate dialect
# (only applied with "applicationContext-hibernate.xml")
hibernate.dialect=org.hibernate.dialect.SQLServer2012Dialect