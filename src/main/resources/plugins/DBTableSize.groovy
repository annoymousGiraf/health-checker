package plugins

import groovy.sql.Sql

/**
 * @author marina.bunin
 * @since 10/05/2017
 */
thresholdBase = 100
def execute() {
    println("DB Table Size Plugin Greeting")
    def threshold = thresholdBase * 1024 * 1024;
    println "This plugin display tables that size bigger then " + threshold + " bytes"

    def prop = new Properties()
    prop.setProperty("db.driverClassName", "org.postgresql.Driver");
    prop.setProperty("db.url", "jdbc:postgresql://10.100.16.233:5432/securetrack")
    prop.setProperty("db.username", "postgres")
    prop.setProperty("db.password", "")
 //   prop.setProperty("db.initialPoolSize", 1)
//
//    new File("../db.properties").withInputStream { p ->
//            prop.load(p)
//    }

    println "Going to connect to " + prop.getProperty("db.url")

    def sql = Sql.newInstance(prop.getProperty("db.url"), prop.getProperty("db.username"), prop.getProperty("db.password"), prop.getProperty("db.driverClassName"))
    println("ST DB connected")
    sql.connection.autoCommit = false



    def tableSizeQuery =
            "SELECT nspname || '.' || relname AS \"relation\",\n" +
                    "    pg_size_pretty(pg_total_relation_size(C.oid)) AS \"total_size\"\n" +
                    "  FROM pg_class C\n" +
                    "  LEFT JOIN pg_namespace N ON (N.oid = C.relnamespace)\n" +
                    "  WHERE nspname NOT IN ('pg_catalog', 'information_schema')\n" +
                    "    AND C.relkind <> 'i'\n" +
                    "    AND nspname !~ '^pg_toast'\n" +
                    "    AND pg_total_relation_size(C.oid) > " + threshold + " \n" +
                    "  ORDER BY pg_total_relation_size(C.oid) DESC;"

    def resultList = []
    sql.eachRow(tableSizeQuery) { row ->
        println row
        resultList << row.relation
    }

    if(resultList.size() > 0)
    {
        return ["status":"Failed", "details":"Following tables exceeded "+thresholdBase+" MB: \n" + resultList +"\n", "solution_suggestion":"Run vacuum on large tables"]
    }
    return ["status":"OK", "details":"test succeeded", "solution_suggestion":""]

}

def getDescription()
{
    return "Check if there are tables larger than " + thresholdBase + " MB"
}

this
