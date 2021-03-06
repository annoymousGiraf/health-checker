package plugins

import groovy.sql.Sql

/**
 * @author marina.bunin
 * @since 10/05/2017
 */
def execute() {
    println("Not ready device latest revision plugin")

    def prop = new Properties()
    prop.setProperty("db.driverClassName", "org.postgresql.Driver");
    prop.setProperty("db.url", "jdbc:postgresql://10.100.16.233:5432/securetrack")
    prop.setProperty("db.username", "postgres")
    prop.setProperty("db.password", "")
//    prop.setProperty("db.initialPoolSize", 1)

//    new File("../db.properties").withInputStream { p ->
//        prop.load(p)
//    }

    println "Going to connect to " + prop.getProperty("db.url")

    def sql = Sql.newInstance(prop.getProperty("db.url"), prop.getProperty("db.username"), prop.getProperty("db.password"), prop.getProperty("db.driverClassName"))
    println("ST DB connected")
    sql.connection.autoCommit = false

    def query = "SELECT id, mgmt_id, date, time, ready, management_name, management_ip, management_type \n" +
            "FROM cp_version cp join managements mgmt on cp.mgmt_id=mgmt.management_id \n" +
            "WHERE ready = false and date < now()::date - 1 and id in\n" +
            "    (SELECT max(id) FROM nst_configuration GROUP BY mgmt_id) ORDER BY id;"

    def resultList = []
    sql.eachRow(query) { row ->
        resultList << row
        println row
    }

    if(resultList.size() > 0)
    {
        return ["status":"Failed", "details":"One or more revisions are not ready", "solution_suggestion":"Wait for revisions to complete"]
    }
    return ["status":"OK", "details":"test succeeded", "solution_suggestion":""]

}

def getDescription()
{
    return "Verify the revisions are ready"
}

this
