package plugins

import groovy.sql.Sql

import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * @author marina.bunin
 * @since 10/05/2017
 */

intervalDays = 7
def execute() {
    println("Vacuum Stats Plugin Greeting")

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

    def hasProblem = false
    sql.eachRow(vacuumStatsQuery) { row ->
        println row
        if (row.relname.toString().startsWith("cp_stats") || row.relname.toString().startsWith("nst")){
            if((row.last_vacuum == null || calcDiff(row.last_vacuum.toString()) > intervalDays*24*3600)
                    && (row.last_autovacuum == null || calcDiff(row.last_autovacuum.toString()) > intervalDays*24*3600))
                hasProblem = true;
        }
    }

    if(!hasProblem)
    {
        return ["status":"OK", "details":"test succeeded", "solution_suggestion":""]
    }
    return ["status":"Failed", "details":"One or more tables requires vacuum", "solution_suggestion":"Run vacuum on large tables"]
}

def getDescription()
{
    return "Checks if there was no vacuum in last " + intervalDays + " days currently limited to nst and cp_stats tables"
}


def calcDiff(String givenDate){
    long given = LocalDateTime.parse(givenDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSS")).toEpochSecond(ZoneOffset.UTC)
    long curr = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
    return curr - given
}
this

//println execute()