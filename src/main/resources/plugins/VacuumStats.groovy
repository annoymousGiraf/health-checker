package plugins

import groovy.sql.Sql

/**
 * @author marina.bunin
 * @since 10/05/2017
 */
println("Vacuum Stats Plugin Greeting")

def prop = new Properties()
new File("../db.properties").withInputStream { p ->
    prop.load(p)
}

println "Going to connect to " + prop.getProperty("db.url")

def sql = Sql.newInstance(prop.getProperty("db.url"), prop.getProperty("db.username"), prop.getProperty("db.password"), prop.getProperty("db.driverClassName"))
println("ST DB connected")
sql.connection.autoCommit = false


def vacuumStatsQuery = "SELECT relname, last_vacuum, last_autovacuum, last_analyze, last_autoanalyze FROM pg_stat_user_tables;"

sql.eachRow(vacuumStatsQuery) { row ->
    println row
}
