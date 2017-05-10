package plugins

import groovy.sql.Sql

/**
 * @author marina.bunin
 * @since 10/05/2017
 */
println("Vacuum Stats Plugin Greeting")
def intervalDays = 7
println "no vacuum in last " + intervalDays + " days"
println "currently limited to nst and cp_stats tables"

def prop = new Properties()
new File("../db.properties").withInputStream { p ->
    prop.load(p)
}

println "Going to connect to " + prop.getProperty("db.url")

def sql = Sql.newInstance(prop.getProperty("db.url"), prop.getProperty("db.username"), prop.getProperty("db.password"), prop.getProperty("db.driverClassName"))
println("ST DB connected")
sql.connection.autoCommit = false


//def vacuumStatsQuery = "SELECT relname, last_vacuum, last_autovacuum, last_analyze, last_autoanalyze FROM pg_stat_user_tables;"
def vacuumStatsQuery =
        "SELECT relname, last_vacuum, last_autovacuum, last_analyze, last_autoanalyze \n" +
        "FROM pg_stat_user_tables \n" +
        "WHERE (last_autovacuum < now()::date - " + intervalDays + " or \n" +
                "last_vacuum < now()::date - " + intervalDays + " or \n" +
                "(last_autovacuum is null and last_vacuum is null)) \n" +
                "AND (relname LIKE 'nst_%' or relname LIKE 'cp_stats_%');"

sql.eachRow(vacuumStatsQuery) { row ->
    println row
}

def getDescription(){
    return "stub"
}